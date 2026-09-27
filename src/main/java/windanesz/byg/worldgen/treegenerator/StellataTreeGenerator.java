package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class StellataTreeGenerator extends WoodlandTreeGenerator {
    private static final int CENTRE = 6;
    private static final int SIZE = 2 * CENTRE + 1;
    private static final int LIMB_LENGTH = 3;

    private static final int[][] PINK_CROWN = {
            {-5, 45, 45}, {-4, 55, 55}, {-3, 60, 60}, {-2, 60, 72}, {-1, 55, 62},
            {0, 50, 60}, {1, 35, 55}, {2, 25, 55}, {3, 15, 60}
    };
    private static final int[][] WHITE_CROWN = {
            {-6, 35, 55}, {-5, 45, 55}, {-4, 55, 60}, {-3, 60, 80}, {-2, 60, 70},
            {-1, 55, 60}, {0, 45, 60}, {1, 35, 50}, {2, 25, 55}, {3, 15, 60}
    };

    private final boolean white;

    public StellataTreeGenerator(boolean white) {
        this.white = white;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int top = 9 + random.nextInt(3);
        int[][] crown = this.white ? WHITE_CROWN : PINK_CROWN;
        int firstLayer = crown[0][0];
        int lastLayer = crown[crown.length - 1][0];
        if (!this.canStandAt(world, origin, top, CENTRE, top + lastLayer + 1)) {
            return false;
        }

        IBlockState leaf = (this.white ? ModBlocks.stellata_leaves_white : ModBlocks.stellata_leaves_pink).getDefaultState();
        IBlockState vertical = logState(EnumFacing.Axis.Y);
        int limbY = top - (this.white ? 3 : 2);
        int lowestCrownY = top + firstLayer;

        boolean[][][] leaves = new boolean[SIZE][crown.length][SIZE];
        boolean[][][] anchors = new boolean[SIZE][crown.length][SIZE];
        for (int i = 0; i < crown.length; i++) {
            fillLayer(leaves, CENTRE, i, crown[i][1] / 10.0, crown[i][2] / 100.0, random);
        }
        // The trunk and limbs are inside the crown; mark them so leaves hugging them survive pruning.
        for (int y = lowestCrownY; y <= top; y++) {
            anchors[CENTRE][y - lowestCrownY][CENTRE] = true;
        }
        for (int step = 1; step <= LIMB_LENGTH; step++) {
            int layer = limbY - lowestCrownY;
            anchors[CENTRE + step][layer][CENTRE] = true;
            anchors[CENTRE - step][layer][CENTRE] = true;
            anchors[CENTRE][layer][CENTRE + step] = true;
            anchors[CENTRE][layer][CENTRE - step] = true;
        }
        pruneIsolated(leaves, anchors);

        BlockPos crownOrigin = origin.up(lowestCrownY);
        for (int x = 0; x < SIZE; x++) {
            for (int layer = 0; layer < crown.length; layer++) {
                for (int z = 0; z < SIZE; z++) {
                    if (leaves[x][layer][z]) {
                        this.placeLeaf(world, crownOrigin.add(x - CENTRE, layer, z - CENTRE), leaf);
                    }
                }
            }
        }

        for (int y = 0; y <= top; y++) {
            this.placeLog(world, origin.up(y), vertical);
        }
        IBlockState alongX = logState(EnumFacing.Axis.X);
        IBlockState alongZ = logState(EnumFacing.Axis.Z);
        for (int step = 1; step <= LIMB_LENGTH; step++) {
            this.placeLog(world, origin.add(step, limbY, 0), alongX);
            this.placeLog(world, origin.add(-step, limbY, 0), alongX);
            this.placeLog(world, origin.add(0, limbY, step), alongZ);
            this.placeLog(world, origin.add(0, limbY, -step), alongZ);
        }
        return true;
    }

    /** Pink stellata grows spruce logs, white grows dark oak, as the old templates did. */
    private IBlockState logState(EnumFacing.Axis axis) {
        BlockLog.EnumAxis logAxis = BlockLog.EnumAxis.fromFacingAxis(axis);
        if (this.white) {
            return Blocks.LOG2.getDefaultState()
                    .withProperty(BlockNewLog.VARIANT, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK)
                    .withProperty(BlockLog.LOG_AXIS, logAxis);
        }
        return Blocks.LOG.getDefaultState()
                .withProperty(BlockOldLog.VARIANT, net.minecraft.block.BlockPlanks.EnumType.SPRUCE)
                .withProperty(BlockLog.LOG_AXIS, logAxis);
    }
}
