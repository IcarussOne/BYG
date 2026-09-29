package windanesz.byg.worldgen;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Random;
import windanesz.byg.Config;

final class GreatLakesIslandWorldgen {
    private static final String BIOME_ID = "byg:byg_great_lakes";
    private static final int CHUNK_CHANCE = 16;
    /** Open water required beyond the island's edge. */
    private static final int WATER_MARGIN = 2;
    private static final int MIN_DEPTH = 3;
    private static final int MIN_RADIUS = 4;
    private static final int MAX_RADIUS = 6;
    private static final int CHEST_CHANCE = 5;
    private static final EnumFacing[] FACINGS = {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};
    private static final BlockFlower.EnumFlowerType[] RED_FLOWERS = {
            BlockFlower.EnumFlowerType.POPPY, BlockFlower.EnumFlowerType.BLUE_ORCHID, BlockFlower.EnumFlowerType.ALLIUM,
            BlockFlower.EnumFlowerType.HOUSTONIA, BlockFlower.EnumFlowerType.RED_TULIP, BlockFlower.EnumFlowerType.ORANGE_TULIP,
            BlockFlower.EnumFlowerType.WHITE_TULIP, BlockFlower.EnumFlowerType.PINK_TULIP, BlockFlower.EnumFlowerType.OXEYE_DAISY};
    private static final BlockDoublePlant.EnumPlantType[] TALL_FLOWERS = {
            BlockDoublePlant.EnumPlantType.SYRINGA, BlockDoublePlant.EnumPlantType.ROSE, BlockDoublePlant.EnumPlantType.PAEONIA};

    private GreatLakesIslandWorldgen() {
    }

    static void generate(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (!Config.isFeatureDimension(dimID) || random.nextInt(CHUNK_CHANCE) != 0) {
            return;
        }
        // Each axis gets its own radius so islands come out slightly oval.
        int radiusX = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
        int radiusZ = MIN_RADIUS + random.nextInt(MAX_RADIUS - MIN_RADIUS + 1);
        // Keep the whole island inside the 16x16 area Forge guarantees to be loaded around this chunk.
        int centerX = chunkX + radiusX + random.nextInt(16 - 2 * radiusX);
        int centerZ = chunkZ + radiusZ + random.nextInt(16 - 2 * radiusZ);
        if (!BygWorldGenerator.matchesBiome(world, centerX, centerZ, BIOME_ID)) {
            return;
        }
        int waterTop = world.getSeaLevel() - 1;
        BlockPos center = new BlockPos(centerX, waterTop, centerZ);
        if (!isLargeLake(world, center, Math.max(radiusX, radiusZ) + WATER_MARGIN)) {
            return;
        }
        int floorY = findFloor(world, center);
        if (waterTop - floorY < MIN_DEPTH) {
            return;
        }

        boolean hasChest = random.nextInt(CHEST_CHANCE) < 3;
        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                // 0 at the centre, 1 at the nominal edge.
                double distance = (double) (dx * dx) / (radiusX * radiusX) + (double) (dz * dz) / (radiusZ * radiusZ);
                if (distance > 1.05 || (distance >= 0.8 && random.nextBoolean())) {
                    continue;
                }
                int x = centerX + dx;
                int z = centerZ + dz;
                int columnFloor = findFloor(world, new BlockPos(x, waterTop, z));
                int topY = waterTop + (distance <= 0.12 ? 2 : distance <= 0.55 ? 1 : 0);
                for (int y = columnFloor + 1; y < topY; y++) {
                    world.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getDefaultState(), 2);
                }
                boolean shore = topY == waterTop;
                // A bare dirt block caps the buried chest, so it stands out as a hint that something is under it.
                boolean cap = hasChest && dx == 0 && dz == 0;
                world.setBlockState(new BlockPos(x, topY, z),
                        (shore ? Blocks.GRAVEL : cap ? Blocks.DIRT : Blocks.GRASS).getDefaultState(), 2);
                if (!shore && !cap) {
                    decorate(random, world, new BlockPos(x, topY + 1, z));
                }
            }
        }

        if (hasChest) {
            // Dug into the middle of the island, directly under the dirt cap (the centre column tops out at waterTop + 2).
            placeChest(random, world, center.up(1));
        }
    }

    private static boolean isLargeLake(World world, BlockPos center, int clearRadius) {
        if (!world.isAreaLoaded(center.add(-clearRadius, 0, -clearRadius), center.add(clearRadius, 0, clearRadius))) {
            return false;
        }
        int radiusSq = clearRadius * clearRadius;
        for (int dx = -clearRadius; dx <= clearRadius; dx++) {
            for (int dz = -clearRadius; dz <= clearRadius; dz++) {
                if (dx * dx + dz * dz > radiusSq) {
                    continue;
                }
                BlockPos pos = center.add(dx, 0, dz);
                if (world.getBlockState(pos).getMaterial() != Material.WATER
                        || !world.isAirBlock(pos.up())
                        || world.getBlockState(pos.down()).getMaterial() != Material.WATER) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int findFloor(World world, BlockPos waterTop) {
        BlockPos cursor = waterTop;
        while (cursor.getY() > 1 && world.getBlockState(cursor).getMaterial() == Material.WATER) {
            cursor = cursor.down();
        }
        return cursor.getY();
    }

    private static void decorate(Random random, World world, BlockPos pos) {
        int roll = random.nextInt(10);
        if (roll < 3) {
            return;
        }
        if (roll == 3 && world.isAirBlock(pos.up())) {
            BlockDoublePlant.EnumPlantType type = TALL_FLOWERS[random.nextInt(TALL_FLOWERS.length)];
            world.setBlockState(pos, Blocks.DOUBLE_PLANT.getDefaultState()
                    .withProperty(BlockDoublePlant.VARIANT, type)
                    .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 2);
            world.setBlockState(pos.up(), Blocks.DOUBLE_PLANT.getDefaultState()
                    .withProperty(BlockDoublePlant.VARIANT, type)
                    .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
            return;
        }
        IBlockState flower = random.nextInt(6) == 0
                ? Blocks.YELLOW_FLOWER.getDefaultState()
                : Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
                RED_FLOWERS[random.nextInt(RED_FLOWERS.length)]);
        world.setBlockState(pos, flower, 2);
    }

    private static void placeChest(Random random, World world, BlockPos pos) {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState()
                .withProperty(BlockChest.FACING, FACINGS[random.nextInt(FACINGS.length)]), 2);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityChest) {
            ((TileEntityChest) tile).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, random.nextLong());
        }
    }
}
