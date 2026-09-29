package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.Config;

import java.util.Random;

final class AncientForestCanopyWebWorldgen {
    private AncientForestCanopyWebWorldgen() {}

    static void generate(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || world.isRemote || !BygWorldGenerator.matchesBiome(world, chunkX, chunkZ, "byg:byg_ancient_forest")) return;
        int limit = Config.scaleClusterPlantAttempts(5);
        int placed = 0;
        int start = random.nextInt(256);
        for (int index = 0; index < 256 && placed < limit; index++) {
            int cell = (start + index * 73) & 255;
            int x = chunkX + (cell & 15);
            int z = chunkZ + (cell >> 4);
            if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) continue;
            int top = world.getHeight(new BlockPos(x, 0, z)).getY() - 1;
            int bottom = Math.max(5, top - 135);
            for (int y = top; y >= bottom && placed < limit; y--) {
                BlockPos leafPos = new BlockPos(x, y, z);
                IBlockState state = world.getBlockState(leafPos);
                if (!state.getBlock().isLeaves(state, world, leafPos) || !world.isAirBlock(leafPos.down())) continue;
                if (random.nextInt(110) != 0) continue;
                BlockPos webPos = leafPos.down();
                world.setBlockState(webPos, Blocks.WEB.getDefaultState(), 2);
                placed++;
                if (placed < limit && random.nextInt(4) == 0 && world.isAirBlock(webPos.down())) {
                    world.setBlockState(webPos.down(), Blocks.WEB.getDefaultState(), 2);
                    placed++;
                }
            }
        }
    }
}
