package windanesz.byg.worldgen.treegenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import windanesz.byg.registry.ModBlocks;

public class WorldGenMegaBlueSpruceTree extends WorldGenMegaPineTree {

    public WorldGenMegaBlueSpruceTree() {
        super(false, true);
    }

    @Override
    protected void growLeavesLayerStrict(World world, BlockPos center, int width) {
        int radiusSquared = width * width;

        for (int x = -width; x <= width + 1; ++x) {
            for (int z = -width; z <= width + 1; ++z) {
                int previousX = x - 1;
                int previousZ = z - 1;

                if (x * x + z * z <= radiusSquared
                        || previousX * previousX + previousZ * previousZ <= radiusSquared
                        || x * x + previousZ * previousZ <= radiusSquared
                        || previousX * previousX + z * z <= radiusSquared) {
                    BlockPos pos = center.add(x, 0, z);
                    IBlockState state = world.getBlockState(pos);

                    if (state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos)) {
                        this.setBlockAndNotifyAdequately(world, pos, ModBlocks.spruce_leaves_blue.getDefaultState());
                    }
                }
            }
        }
    }
}
