package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Vanilla-style leaf decay, ported from {@code net.minecraft.block.BlockLeaves}: a leaf only keeps
 * existing while it can trace a chain of at most 4 leaf blocks back to a block that sustains leaves
 * (a log). A leaf does not actively check this on every tick - it only starts once a nearby sustaining
 * or leaf block is removed, exactly like vanilla oak/spruce/birch/jungle/acacia/dark oak leaves.
 */
public final class LeafDecay {
    public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");

    // Ticks run one at a time on the main world thread, so one shared scratch buffer for every BYG
    // leaf block is safe, the same way vanilla reuses one array per leaf block instance.
    private static final int[] SCRATCH = new int[32768];

    private LeafDecay() {
    }

    /** Call from the leaf block's breakBlock override, alongside the normal super call. */
    public static void breakBlock(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (!world.isAreaLoaded(new BlockPos(x - 2, y - 2, z - 2), new BlockPos(x + 2, y + 2, z + 2))) {
            return;
        }
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    BlockPos neighbor = pos.add(dx, dy, dz);
                    IBlockState state = world.getBlockState(neighbor);
                    if (state.getBlock().isLeaves(state, world, neighbor)) {
                        state.getBlock().beginLeavesDecay(state, world, neighbor);
                    }
                }
            }
        }
    }

    /** Marks a leaf as due for a decay check on its next tick. Call from beginLeavesDecay. */
    public static void beginLeavesDecay(World world, BlockPos pos, IBlockState state) {
        if (!state.getValue(CHECK_DECAY)) {
            world.setBlockState(pos, state.withProperty(CHECK_DECAY, true), 4);
        }
    }

    /** Runs the decay scan. Call from updateTick; the block may be air afterwards. */
    public static void updateTick(Block block, World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote || !state.getValue(CHECK_DECAY)) {
            return;
        }
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (!world.isAreaLoaded(pos, 1)) {
            return;
        }
        if (world.isAreaLoaded(pos, 6)) {
            for (int dx = -4; dx <= 4; ++dx) {
                for (int dy = -4; dy <= 4; ++dy) {
                    for (int dz = -4; dz <= 4; ++dz) {
                        BlockPos neighborPos = pos.add(dx, dy, dz);
                        IBlockState neighborState = world.getBlockState(neighborPos);
                        Block neighborBlock = neighborState.getBlock();
                        int index = (dx + 16) * 1024 + (dy + 16) * 32 + dz + 16;
                        if (!neighborBlock.canSustainLeaves(neighborState, world, neighborPos)) {
                            SCRATCH[index] = neighborBlock.isLeaves(neighborState, world, neighborPos) ? -2 : -1;
                        } else {
                            SCRATCH[index] = 0;
                        }
                    }
                }
            }
            for (int distance = 1; distance <= 4; ++distance) {
                for (int dx = -4; dx <= 4; ++dx) {
                    for (int dy = -4; dy <= 4; ++dy) {
                        for (int dz = -4; dz <= 4; ++dz) {
                            if (SCRATCH[(dx + 16) * 1024 + (dy + 16) * 32 + dz + 16] == distance - 1) {
                                spread(dx - 1, dy, dz, distance);
                                spread(dx + 1, dy, dz, distance);
                                spread(dx, dy - 1, dz, distance);
                                spread(dx, dy + 1, dz, distance);
                                spread(dx, dy, dz - 1, distance);
                                spread(dx, dy, dz + 1, distance);
                            }
                        }
                    }
                }
            }
        }
        if (SCRATCH[16912] >= 0) {
            world.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
        } else {
            block.dropBlockAsItemWithChance(world, pos, state, 1.0f, 0);
            world.setBlockToAir(pos);
        }
    }

    private static void spread(int dx, int dy, int dz, int distance) {
        int index = (dx + 16) * 1024 + (dy + 16) * 32 + dz + 16;
        if (SCRATCH[index] == -2) {
            SCRATCH[index] = distance;
        }
    }
}
