package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public class WorldGenDeadSeaSpikes extends WorldGenerator {
    private static final IBlockState ROCK = ModBlocks.rocky_stone.getDefaultState();
    private static final IBlockState STONE = Blocks.STONE.getDefaultState();
    private static final IBlockState MOSSY = Blocks.MOSSY_COBBLESTONE.getDefaultState();

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        if (!world.isAreaLoaded(position.add(-8, 1, -8), position.add(8, world.getSeaLevel(), 8))) return false;
        int floor = WorldGenDeadSeaRocks.findFloor(world, position);
        if (floor < 0) return false;

        int spikes = 3 + rand.nextInt(5);
        int built = 0;
        for (int i = 0; i < spikes; i++) {
            BlockPos spot = position.add(rand.nextInt(11) - 5, 0, rand.nextInt(11) - 5);
            int spotFloor = WorldGenDeadSeaRocks.findFloor(world, spot);
            if (spotFloor < 0) continue;
            // A few tall needles among mostly short teeth; never break the surface.
            int max = world.getSeaLevel() - spotFloor - 2;
            int height = rand.nextInt(4) == 0 ? 7 + rand.nextInt(5) : 2 + rand.nextInt(5);
            height = Math.min(height, max);
            if (height < 2) continue;
            buildSpike(world, rand, spot, spotFloor, height);
            built++;
        }
        for (int i = 0; i < 6; i++) {
            BlockPos spot = position.add(rand.nextInt(15) - 7, 0, rand.nextInt(15) - 7);
            int spotFloor = WorldGenDeadSeaRocks.findFloor(world, spot);
            if (spotFloor < 0) continue;
            for (int y = 0; y <= rand.nextInt(2); y++) place(world, spot.up(spotFloor + y - spot.getY()), rand);
        }
        return built > 0;
    }

    private void buildSpike(World world, Random rand, BlockPos base, int floor, int height) {
        int x = base.getX();
        int z = base.getZ();
        for (int y = -1; y < height; y++) {
            // Broad at the foot, a single block at the tip; the shaft wanders slightly as it rises.
            if (y > 0 && y % 3 == 0 && rand.nextInt(3) == 0) {
                if (rand.nextBoolean()) x += rand.nextBoolean() ? 1 : -1;
                else z += rand.nextBoolean() ? 1 : -1;
            }
            int radius = y < height / 3 ? 1 : 0;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (Math.abs(dx) + Math.abs(dz) > radius) continue;
                    place(world, new BlockPos(x + dx, floor + y, z + dz), rand);
                }
            }
            if (y > 0) {
                // Keep the wandering shaft connected below.
                place(world, new BlockPos(x, floor + y - 1, z), rand);
            }
        }
    }

    private void place(World world, BlockPos pos, Random rand) {
        if (world.getBlockState(pos).getBlock() != Blocks.WATER && !world.isAirBlock(pos)) return;
        int roll = rand.nextInt(10);
        world.setBlockState(pos, roll < 6 ? ROCK : roll < 9 ? STONE : MOSSY, 2);
    }
}
