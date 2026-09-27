package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class HawthornTreeGenerator extends WoodlandTreeGenerator {
    private static final int CENTRE = 7;
    private static final int SIZE = 2 * CENTRE + 1;
    /** Crown layers counted down from the trunk top: {offset from top, radius x10, density x100}. */
    private static final int[][] CROWN = {
            {-4, 60, 22}, {-3, 65, 62}, {-2, 65, 85}, {-1, 65, 58}, {0, 60, 58}, {1, 45, 78}
    };
    private static final int FIRST_LAYER = CROWN[0][0];

    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int top = 9 + random.nextInt(4);
        if (!this.canStandAt(world, origin, top + 1, 7, top + 2)) {
            return false;
        }

        IBlockState logBase = ModBlocks.hawthorn_log.getDefaultState();
        IBlockState vertical = log(logBase, EnumFacing.Axis.Y);
        IBlockState plain = ModBlocks.hawthorn_leaves.getDefaultState();
        IBlockState flowering = ModBlocks.hawthorn_leaves_flowering.getDefaultState();
        IBlockState berries = ModBlocks.hawthorn_berry_leaves.getDefaultState();
        // 0 = plain, 1 = flowering, 2 = flowering with berries.
        int variant = random.nextInt(10) < 4 ? 0 : 1 + random.nextInt(2);

        boolean[][][] leaves = new boolean[SIZE][CROWN.length + 1][SIZE];
        boolean[][][] anchors = new boolean[SIZE][CROWN.length + 1][SIZE];
        for (int i = 0; i < CROWN.length; i++) {
            fillLayer(leaves, CENTRE, i, CROWN[i][1] / 10.0, CROWN[i][2] / 100.0, random);
        }

        // Trunk: single column with a plus-shaped body, a 3x3 collar under the crown and a 3x3 base.
        int lowestCrownY = top + FIRST_LAYER;
        for (int y = 0; y <= top; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    boolean centre = dx == 0 && dz == 0;
                    boolean plus = dx == 0 || dz == 0;
                    boolean wide = y == 2 || y == top - 3;
                    if (y == top ? centre : (wide || plus)) {
                        if (y >= lowestCrownY) {
                            markAnchor(anchors, dx, y - lowestCrownY, dz);
                        }
                    }
                }
            }
        }

        // Limbs reach out below the crown's heaviest layer and curl up at the tip.
        int limbCount = 3 + random.nextInt(2);
        int[][] limbs = new int[limbCount][];
        EnumFacing start = randomSide(random);
        for (int i = 0; i < limbCount; i++) {
            EnumFacing side = EnumFacing.byHorizontalIndex((start.getHorizontalIndex() + i) % 4);
            int y = top - 3 + random.nextInt(2);
            int length = 2 + random.nextInt(2);
            limbs[i] = new int[]{side.getHorizontalIndex(), y, length};
            for (int step = 1; step <= length + 1; step++) {
                markAnchor(anchors, side.getXOffset() * step, y - lowestCrownY, side.getZOffset() * step);
            }
            markAnchor(anchors, side.getXOffset() * (length + 1), y + 1 - lowestCrownY, side.getZOffset() * (length + 1));
            // Extra leaf mass hugging the limb tip.
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    int lx = side.getXOffset() * (length + 1) + dx;
                    int lz = side.getZOffset() * (length + 1) + dz;
                    if (random.nextInt(4) != 0 && Math.abs(lx) <= CENTRE && Math.abs(lz) <= CENTRE) {
                        leaves[CENTRE + lx][y + 1 - lowestCrownY][CENTRE + lz] = true;
                    }
                }
            }
        }
        pruneIsolated(leaves, anchors);

        BlockPos crownOrigin = origin.up(lowestCrownY);
        for (int x = 0; x < SIZE; x++) {
            for (int layer = 0; layer < leaves[0].length; layer++) {
                for (int z = 0; z < SIZE; z++) {
                    if (leaves[x][layer][z]) {
                        IBlockState state = variant == 0 ? plain : random.nextInt(11) == 0 ? flowering : plain;
                        if (variant == 2 && random.nextInt(80) == 0) {
                            state = berries;
                        }
                        this.placeLeaf(world, crownOrigin.add(x - CENTRE, layer, z - CENTRE), state);
                    }
                }
            }
        }

        // Trunk proper. The centre column is mandatory, everything else is decoration.
        for (int y = 0; y <= top; y++) {
            this.placeLog(world, origin.up(y), vertical);
        }
        for (int y = 1; y <= top; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if ((dx == 0 && dz == 0) || y >= top || y < 2) {
                        continue;
                    }
                    if (y == 2 || y == top - 3 || dx == 0 || dz == 0) {
                        this.placeLog(world, origin.add(dx, y, dz), vertical);
                    }
                }
            }
        }
        // Root flare: a diamond two blocks wide, each arm rooted down to whatever ground is there.
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if ((dx != 0 || dz != 0) && Math.abs(dx) + Math.abs(dz) <= 2) {
                    this.placeAnchoredLog(world, origin.add(dx, 0, dz), vertical, 2);
                }
            }
        }
        for (int[] limb : limbs) {
            EnumFacing side = EnumFacing.byHorizontalIndex(limb[0]);
            IBlockState horizontal = log(logBase, side.getAxis());
            for (int step = 1; step <= limb[2]; step++) {
                this.placeLog(world, origin.add(side.getXOffset() * step, limb[1], side.getZOffset() * step), horizontal);
            }
            this.placeLog(world, origin.add(side.getXOffset() * (limb[2] + 1), limb[1], side.getZOffset() * (limb[2] + 1)), vertical);
            this.placeLog(world, origin.add(side.getXOffset() * (limb[2] + 1), limb[1] + 1, side.getZOffset() * (limb[2] + 1)), vertical);
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
