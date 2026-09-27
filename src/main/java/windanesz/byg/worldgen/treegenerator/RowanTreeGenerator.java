package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class RowanTreeGenerator extends WoodlandTreeGenerator {
    private static final int CENTRE = 7;
    private static final int SIZE = 2 * CENTRE + 1;
    /** Crown layers counted from the trunk top: {offset from top, radius x10, density x100}. */
    private static final int[][] CROWN = {
            {-5, 50, 14}, {-4, 60, 20}, {-3, 60, 24}, {-2, 60, 34}, {-1, 60, 50},
            {0, 60, 52}, {1, 60, 54}, {2, 55, 50}, {3, 40, 44}
    };
    private static final int FIRST_LAYER = CROWN[0][0];

    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int top = 9 + random.nextInt(3);
        if (!this.canStandAt(world, origin, top + 1, 7, top + 4)) {
            return false;
        }

        IBlockState logBase = ModBlocks.rowan_log.getDefaultState();
        IBlockState vertical = log(logBase, EnumFacing.Axis.Y);
        IBlockState plain = ModBlocks.rowan_leaves.getDefaultState();
        IBlockState berries = ModBlocks.rowan_berry_leaves.getDefaultState();

        int lowestCrownY = top + FIRST_LAYER;
        boolean[][][] leaves = new boolean[SIZE][CROWN.length][SIZE];
        boolean[][][] anchors = new boolean[SIZE][CROWN.length][SIZE];
        for (int i = 0; i < CROWN.length; i++) {
            fillLayer(leaves, CENTRE, i, CROWN[i][1] / 10.0, CROWN[i][2] / 100.0, random);
        }
        for (int y = lowestCrownY; y <= top; y++) {
            markAnchor(anchors, 0, y - lowestCrownY, 0);
        }

        // Limbs leave the trunk in different directions, run level and curl upwards.
        int limbCount = 3 + random.nextInt(3);
        int[][] limbs = new int[limbCount][];
        EnumFacing start = randomSide(random);
        for (int i = 0; i < limbCount; i++) {
            EnumFacing side = EnumFacing.byHorizontalIndex((start.getHorizontalIndex() + i) % 4);
            int y = top - 2 + random.nextInt(3);
            int length = 2 + random.nextInt(2);
            limbs[i] = new int[]{side.getHorizontalIndex(), y, length};
            int tipX = side.getXOffset() * length;
            int tipZ = side.getZOffset() * length;
            for (int step = 1; step <= length; step++) {
                markAnchor(anchors, side.getXOffset() * step, y - lowestCrownY, side.getZOffset() * step);
            }
            markAnchor(anchors, tipX, y + 1 - lowestCrownY, tipZ);
            // A tuft of leaves around each limb tip keeps the sparse crown attached to the wood.
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        int layer = y + dy - lowestCrownY;
                        if (random.nextInt(3) != 0 && layer >= 0 && layer < CROWN.length) {
                            leaves[CENTRE + tipX + dx][layer][CENTRE + tipZ + dz] = true;
                        }
                    }
                }
            }
        }
        pruneIsolated(leaves, anchors);

        BlockPos crownOrigin = origin.up(lowestCrownY);
        for (int x = 0; x < SIZE; x++) {
            for (int layer = 0; layer < CROWN.length; layer++) {
                for (int z = 0; z < SIZE; z++) {
                    if (leaves[x][layer][z]) {
                        this.placeLeaf(world, crownOrigin.add(x - CENTRE, layer, z - CENTRE),
                                random.nextInt(70) == 0 ? berries : plain);
                    }
                }
            }
        }

        for (int y = 0; y <= top; y++) {
            this.placeLog(world, origin.up(y), vertical);
        }
        // Root flare: a cross two blocks long at the base, one block long a little higher.
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            BlockPos inner = origin.offset(side);
            if (this.placeAnchoredLog(world, inner, vertical, 3)) {
                this.placeAnchoredLog(world, inner.offset(side), vertical, 2);
            }
        }
        // Small knot of wood a few blocks up.
        IBlockState eastWest = log(logBase, EnumFacing.Axis.X);
        this.placeLog(world, origin.add(1, 3, 0), eastWest);
        this.placeLog(world, origin.add(-1, 3, 0), eastWest);
        for (int[] limb : limbs) {
            EnumFacing side = EnumFacing.byHorizontalIndex(limb[0]);
            IBlockState horizontal = log(logBase, side.getAxis());
            for (int step = 1; step <= limb[2]; step++) {
                this.placeLog(world, origin.add(side.getXOffset() * step, limb[1], side.getZOffset() * step), horizontal);
            }
            this.placeLog(world, origin.add(side.getXOffset() * limb[2], limb[1] + 1, side.getZOffset() * limb[2]), vertical);
        }
        return true;
    }

    private static void markAnchor(boolean[][][] anchors, int dx, int layer, int dz) {
        int x = CENTRE + dx;
        int z = CENTRE + dz;
        if (layer >= 0 && layer < anchors[0].length && x >= 0 && x < SIZE && z >= 0 && z < SIZE) {
            anchors[x][layer][z] = true;
        }
    }
}
