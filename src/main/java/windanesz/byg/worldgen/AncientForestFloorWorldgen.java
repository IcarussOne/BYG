package windanesz.byg.worldgen;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;

import java.util.Random;

public final class AncientForestFloorWorldgen {
    private static final EnumFacing[] FACING = {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST};

    private AncientForestFloorWorldgen() {
    }

    static void generate(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (dimID != 0 || !BygWorldGenerator.matchesBiome(world, chunkX, chunkZ, "byg:byg_ancient_forest")) {
            return;
        }

        int patches = Config.scaleClusterPlantAttempts(4);
        for (int i = 0; i < patches; i++) {
            int centerX = chunkX + random.nextInt(16);
            int centerZ = chunkZ + random.nextInt(16);
            int kind = random.nextInt(3); // Ferns and leaf litter.
            int size = kind == 0 ? 8 + random.nextInt(7) : 2 + random.nextInt(4);
            for (int j = 0; j < size; j++) {
                int spread = kind == 0 ? 7 : 5;
                int x = centerX + random.nextInt(spread) - spread / 2;
                int z = centerZ + random.nextInt(spread) - spread / 2;
                if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) continue;
                BlockPos ground = findGround(world, x, z);
                if (ground == null) continue;
                BlockPos above = ground.up();
                if (kind == 0) {
                    if (world.isAirBlock(above)) {
                        IBlockState fern = Blocks.TALLGRASS.getDefaultState()
                                .withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN);
                        if (fern.getBlock().canPlaceBlockAt(world, above)) world.setBlockState(above, fern, 2);
                    }
                } else {
                    if (world.isAirBlock(above)) {
                        Block leaves = kind == 1 ? ModBlocks.leafpile : ModBlocks.leaf_pile_dead;
                        if (leaves == null) continue;
                        IBlockState pile = leaves.getDefaultState()
                                .withProperty(BlockHorizontal.FACING, FACING[random.nextInt(FACING.length)]);
                        if (leaves.canPlaceBlockAt(world, above)) world.setBlockState(above, pile, 2);
                    }
                }
            }
        }

        int shrubPatches = Config.scaleClusterPlantAttempts(2);
        for (int i = 0; i < shrubPatches; i++) {
            int centerX = chunkX + random.nextInt(16);
            int centerZ = chunkZ + random.nextInt(16);
            int attempts = 5 + random.nextInt(5);
            for (int j = 0; j < attempts; j++) {
                int x = centerX + random.nextInt(7) - 3;
                int z = centerZ + random.nextInt(7) - 3;
                placeGroundPlant(world, x, z, ModBlocks.salal_bush);
            }
        }
        if (random.nextInt(5) == 0) {
            int centerX = chunkX + random.nextInt(16);
            int centerZ = chunkZ + random.nextInt(16);
            for (int i = 0; i < 3; i++) {
                placeGroundPlant(world, centerX + random.nextInt(5) - 2,
                        centerZ + random.nextInt(5) - 2, ModBlocks.wild_strawberry);
            }
        }

        // Fallen timber is occasional and short enough to leave walking gaps.
        if (random.nextInt(5) == 0) placeFallenLog(random, chunkX, chunkZ, world);
        if (random.nextInt(3) == 0) placeFallenBranch(random, chunkX, chunkZ, world);
        if (random.nextInt(3) == 0) placeMushroom(random, chunkX, chunkZ, world);
    }

    private static void placeGroundPlant(World world, int x, int z, Block plant) {
        if (plant == null) return;
        if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) return;
        BlockPos ground = findGround(world, x, z);
        if (ground == null || !world.isAirBlock(ground.up())) return;
        if (plant.canPlaceBlockAt(world, ground.up())) {
            world.setBlockState(ground.up(), plant.getDefaultState(), 2);
        }
    }

    static void generateGroundPatches(Random random, int chunkX, int chunkZ, World world, int dimID) {
        if (dimID != 0 || !BygWorldGenerator.matchesBiome(world, chunkX, chunkZ, "byg:byg_ancient_forest")) return;
        int patches = Config.scaleClusterPlantAttempts(2);
        for (int i = 0; i < patches; i++) {
            int centerX = chunkX + random.nextInt(16);
            int centerZ = chunkZ + random.nextInt(16);
            int radiusX = 4 + random.nextInt(3);
            int radiusZ = 4 + random.nextInt(3);
            IBlockState groundCover = !Config.isWorldgenFeatureEnabled("peat_grass") || random.nextInt(3) == 0
                    ? Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL)
                    : ModBlocks.peat_grass.getDefaultState();
            for (int dx = -radiusX; dx <= radiusX; dx++) {
                for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                    double distance = (double) dx * dx / (radiusX * radiusX)
                            + (double) dz * dz / (radiusZ * radiusZ);
                    if (distance > 0.8 + random.nextDouble() * 0.4) continue;
                    int x = centerX + dx;
                    int z = centerZ + dz;
                    if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) continue;
                    BlockPos ground = findGround(world, x, z);
                    if (ground != null && world.getBlockState(ground).getBlock() == Blocks.GRASS) {
                        world.setBlockState(ground, groundCover, 2);
                    }
                }
            }
        }
    }

    public static BlockPos findGround(World world, int x, int z) {
        BlockPos pos = world.getHeight(new BlockPos(x, 0, z)).down();
        for (int depth = 0; depth < 96 && pos.getY() > 1; depth++, pos = pos.down()) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == Blocks.GRASS || state.getBlock() == ModBlocks.peat_grass
                    || state.getBlock() == Blocks.DIRT && state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) return pos;
            if (state.getMaterial() != Material.AIR && !state.getBlock().isLeaves(state, world, pos)
                    && !state.getBlock().isReplaceable(world, pos)) return null;
        }
        return null;
    }

    private static void placeFallenLog(Random random, int chunkX, int chunkZ, World world) {
        boolean alongX = random.nextBoolean();
        int length = 3 + random.nextInt(3);
        int startX = chunkX + random.nextInt(alongX ? 16 - length : 16);
        int startZ = chunkZ + random.nextInt(alongX ? 16 : 16 - length);
        BlockPos[] positions = new BlockPos[length];
        int firstY = -1;
        for (int i = 0; i < length; i++) {
            int x = startX + (alongX ? i : 0);
            int z = startZ + (alongX ? 0 : i);
            if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) return;
            BlockPos ground = findGround(world, x, z);
            if (ground == null || !world.isAirBlock(ground.up())) return;
            if (firstY < 0) firstY = ground.getY();
            if (Math.abs(ground.getY() - firstY) > 1) return;
            positions[i] = ground.up();
        }
        IBlockState wood = Blocks.LOG2.getDefaultState()
                .withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK)
                .withProperty(BlockLog.LOG_AXIS, alongX ? BlockLog.EnumAxis.X : BlockLog.EnumAxis.Z);
        for (BlockPos pos : positions) world.setBlockState(pos, wood, 2);
    }

    private static void placeFallenBranch(Random random, int chunkX, int chunkZ, World world) {
        boolean alongX = random.nextBoolean();
        int length = 1 + random.nextInt(2);
        int x = chunkX + random.nextInt(alongX ? 16 - length : 16);
        int z = chunkZ + random.nextInt(alongX ? 16 : 16 - length);
        BlockPos[] positions = new BlockPos[length];
        int firstGroundY = -1;
        for (int i = 0; i < length; i++) {
            int px = x + (alongX ? i : 0);
            int pz = z + (alongX ? 0 : i);
            if (!BygWorldGenerator.matchesBiome(world, px, pz, "byg:byg_ancient_forest")) return;
            BlockPos ground = findGround(world, px, pz);
            if (ground == null || !world.isAirBlock(ground.up())) return;
            if (firstGroundY < 0) firstGroundY = ground.getY();
            if (Math.abs(ground.getY() - firstGroundY) > 1) return;
            positions[i] = ground.up();
        }
        IBlockState branch = Blocks.LOG2.getDefaultState()
                .withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK)
                .withProperty(BlockLog.LOG_AXIS, alongX ? BlockLog.EnumAxis.X : BlockLog.EnumAxis.Z);
        for (BlockPos pos : positions) world.setBlockState(pos, branch, 2);
    }

    private static void placeMushroom(Random random, int chunkX, int chunkZ, World world) {
        int x = chunkX + random.nextInt(16);
        int z = chunkZ + random.nextInt(16);
        if (!BygWorldGenerator.matchesBiome(world, x, z, "byg:byg_ancient_forest")) return;
        BlockPos ground = findGround(world, x, z);
        if (ground == null || !world.isAirBlock(ground.up())) return;
        Block mushroom = random.nextInt(4) == 0 ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM;
        if (mushroom.canPlaceBlockAt(world, ground.up())) {
            world.setBlockState(ground.up(), mushroom.getDefaultState(), 2);
        }
    }
}
