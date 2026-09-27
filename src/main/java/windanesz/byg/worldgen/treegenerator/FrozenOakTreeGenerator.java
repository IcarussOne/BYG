package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class FrozenOakTreeGenerator extends WoodlandTreeGenerator {
    /** Disc radius x10 for each crown layer from the bottom; even indices are the wide tiers. */
    private static final int[] PROFILE = {
            10, 50, 32, 43, 27, 42, 22, 42, 22, 33, 20, 27, 20, 17, 14, 10, 10, 0, 0
    };
    /** Indices in PROFILE where a wide/narrow pair can be dropped to shorten the tree. */
    private static final int CUT_START = 9;

    @Override
    public boolean generate(World world, Random random, BlockPos origin) {
        int bare = 4 + random.nextInt(3);
        int cut = random.nextInt(3) * 2;
        int layers = PROFILE.length - cut;
        int crownTop = bare + layers - 1;
        int trunkTop = crownTop - 6;
        if (!this.canStandAt(world, origin, trunkTop + 1, 5, crownTop + 1)) {
            return false;
        }
        IBlockState log = log(ModBlocks.frozen_oak_log.getDefaultState(), EnumFacing.Axis.Y);
        IBlockState leaves = ModBlocks.frozen_oak_leaves.getDefaultState();
        for (int layer = 0; layer < layers; layer++) {
            int index = layer < CUT_START ? layer : layer + cut;
            double radius = PROFILE[index] / 10.0;
            int reach = (int) Math.ceil(radius);
            for (int dx = -reach; dx <= reach; dx++) {
                for (int dz = -reach; dz <= reach; dz++) {
                    if (dx * dx + dz * dz <= radius * radius + 0.5) {
                        this.placeLeaf(world, origin.add(dx, bare + layer, dz), leaves);
                    }
                }
            }
        }
        for (int y = 0; y <= trunkTop; y++) {
            this.placeLog(world, origin.up(y), log);
        }
        return true;
    }
}
