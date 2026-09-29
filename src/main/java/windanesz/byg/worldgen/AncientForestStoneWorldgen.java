package windanesz.byg.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import windanesz.byg.Config;

final class AncientForestStoneWorldgen {
    private static final IBlockState MOSSY = Blocks.MOSSY_COBBLESTONE.getDefaultState();
    private static final IBlockState COBBLE = Blocks.COBBLESTONE.getDefaultState();

    private AncientForestStoneWorldgen() {
    }

    static void generate(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || !BygWorldGenerator.matchesBiome(world, chunkX, chunkZ, "byg:byg_ancient_forest")) return;
        if (random.nextInt(3) == 0) {
            placePile(random, chunkX + 3 + random.nextInt(10), chunkZ + 3 + random.nextInt(10), world);
        }
        if (random.nextInt(16) == 0) {
            for (int attempt = 0; attempt < 3; attempt++) {
                if (placeArch(random, chunkX + 4 + random.nextInt(8),
                        chunkZ + 4 + random.nextInt(8), world)) break;
            }
        }
        if (random.nextInt(6) == 0) {
            for (int attempt = 0; attempt < 4; attempt++) {
                if (placeSlope(random, chunkX + 4 + random.nextInt(8),
                        chunkZ + 4 + random.nextInt(8), world)) break;
            }
        }
    }

    private static IBlockState stone(Random random) {
        return random.nextInt(5) == 0 ? COBBLE : MOSSY;
    }

    private static IBlockState archStone(Random random) {
        int choice = random.nextInt(5);
        return choice < 3 ? MOSSY : choice == 3 ? Blocks.STONE.getDefaultState() : COBBLE;
    }

    private static void placePile(Random random, int x, int z, World world) {
        BlockPos center = ground(world, x, z);
        if (center == null) return;
        Map<BlockPos, IBlockState> stones = new LinkedHashMap<>();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int distance = dx * dx + dz * dz;
                if (distance > 5 || (distance > 2 && random.nextBoolean())) continue;
                BlockPos base = ground(world, x + dx, z + dz);
                if (base == null || Math.abs(base.getY() - center.getY()) > 1) continue;
                int height = distance <= 1 ? 1 + random.nextInt(3) : 1;
                for (int y = 1; y <= height; y++) stones.put(base.up(y), stone(random));
            }
        }
        if (stones.size() >= 6) commit(world, stones);
    }

    private static boolean placeArch(Random random, int x, int z, World world) {
        boolean alongX = random.nextBoolean();
        Map<BlockPos, IBlockState> stones = new LinkedHashMap<>();
        int baseY = -1;
        for (int side : new int[]{-3, 3}) {
            int px = x + (alongX ? side : 0);
            int pz = z + (alongX ? 0 : side);
            BlockPos foot = ground(world, px, pz);
            if (foot == null) return false;
            if (baseY < 0) baseY = foot.getY();
            if (Math.abs(foot.getY() - baseY) > 1) return false;
        }
        int crownY = baseY + 5;
        for (int side : new int[]{-3, 3}) {
            int px = x + (alongX ? side : 0);
            int pz = z + (alongX ? 0 : side);
            BlockPos foot = ground(world, px, pz);
            for (int y = foot.getY() + 1; y <= crownY + 1; y++) {
                stones.put(new BlockPos(px, y, pz), archStone(random));
                if (y <= foot.getY() + 2 || random.nextInt(3) == 0) {
                    int depth = random.nextBoolean() ? -1 : 1;
                    stones.put(new BlockPos(px + (alongX ? 0 : depth), y,
                            pz + (alongX ? depth : 0)), archStone(random));
                }
            }
        }
        for (int span = -2; span <= 2; span++) {
            int px = x + (alongX ? span : 0);
            int pz = z + (alongX ? 0 : span);
            stones.put(new BlockPos(px, crownY + 1, pz), archStone(random));
            if (Math.abs(span) == 2) stones.put(new BlockPos(px, crownY, pz), archStone(random));
            if (Math.abs(span) <= 1 && random.nextInt(4) != 0) {
                stones.put(new BlockPos(px, crownY + 2, pz), archStone(random));
            }
            if (random.nextBoolean()) {
                int depth = random.nextBoolean() ? -1 : 1;
                stones.put(new BlockPos(px + (alongX ? 0 : depth), crownY + 1,
                        pz + (alongX ? depth : 0)), archStone(random));
            }
        }
        return commit(world, stones);
    }

    private static boolean placeSlope(Random random, int x, int z, World world) {
        int direction = random.nextBoolean() ? 1 : -1;
        boolean alongX = random.nextBoolean();
        BlockPos[][] surface = new BlockPos[5][2];
        Map<BlockPos, IBlockState> stones = new LinkedHashMap<>();
        for (int step = 0; step < 5; step++) {
            for (int width = 0; width < 2; width++) {
                int px = x + (alongX ? direction * step : width);
                int pz = z + (alongX ? width : direction * step);
                surface[step][width] = ground(world, px, pz);
                if (surface[step][width] == null) return false;
                if (step > 0 && Math.abs(surface[step][width].getY() - surface[step - 1][width].getY()) > 1) return false;
            }
        }
        for (int step = 0; step < 5; step++) {
            for (int width = 0; width < 2; width++) {
                int height = 1 + step / 2;
                if (step > 0 && step < 4 && random.nextInt(5) == 0) height++;
                for (int y = 1; y <= height; y++) {
                    stones.put(surface[step][width].up(y), stone(random));
                }
            }
        }
        return commit(world, stones);
    }

    private static BlockPos ground(World world, int x, int z) {
        if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) return null;
        return AncientForestFloorWorldgen.findGround(world, x, z);
    }

    private static boolean commit(World world, Map<BlockPos, IBlockState> stones) {
        for (BlockPos pos : stones.keySet()) {
            if (!BygWorldGenerator.matchesBiome(world, pos.getX(), pos.getZ(), "byg:byg_ancient_forest")) return false;
            IBlockState existing = world.getBlockState(pos);
            if (existing.getBlock().isLeaves(existing, world, pos)) return false;
            if (!world.isAirBlock(pos) && !existing.getBlock().isReplaceable(world, pos)) return false;
        }
        for (Map.Entry<BlockPos, IBlockState> block : stones.entrySet()) {
            world.setBlockState(block.getKey(), block.getValue(), 2);
        }
        return true;
    }
}
