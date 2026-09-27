package windanesz.byg.biome;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BiomeSkyrisHighlands
        extends Biome {
    private static final int MAX_FLOATING_PATCH_SIZE = 128;

    public BiomeSkyrisHighlands() {
        super(new Biome.BiomeProperties("Skyris Highlands").setRainfall(0.6f).setBaseHeight(4.0f).setWaterColor(-4559438).setHeightVariation(1.0f).setTemperature(0.25f));
        this.setRegistryName("byg_skyris_highlands");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 5;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 8;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 35;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -12676561;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -12676561;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13057;
    }

    @Override
    public void decorate(World world, Random random, BlockPos chunkPos) {
        removeFloatingStone(world, chunkPos);
        if (random.nextInt(12) == 0) {
            generateSpringwaterPool(world, random, chunkPos);
        }
        super.decorate(world, random, chunkPos);
    }

    private void generateSpringwaterPool(World world, Random random, BlockPos chunkPos) {
        for (int attempt = 0; attempt < 8; attempt++) {
            BlockPos surface = world.getHeight(chunkPos.add(9 + random.nextInt(8), 0,
                    9 + random.nextInt(8))).down();
            int width = 2 + random.nextInt(6);
            int depth = Math.max(2, Math.min(7, width + random.nextInt(3) - 1));
            if (!canPlaceSpringwaterPool(world, surface, width, depth)) {
                continue;
            }
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    if (isPoolCell(x, z, width, depth)) {
                        world.setBlockState(surface.add(x, 0, z), ModBlocks.springwater.getDefaultState(), 2);
                    }
                }
            }
            return;
        }
    }

    private boolean canPlaceSpringwaterPool(World world, BlockPos start, int width, int depth) {
        BlockPos end = start.add(width - 1, 0, depth - 1);
        if (!world.isAreaLoaded(start.add(-1, -1, -1), end.add(1, 1, 1))) {
            return false;
        }
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                if (!isPoolCell(x, z, width, depth)) {
                    continue;
                }
                BlockPos pool = start.add(x, 0, z);
                Block block = world.getBlockState(pool).getBlock();
                if (world.getBiome(pool) != this || (block != Blocks.GRASS && block != Blocks.DIRT
                        && block != Blocks.STONE) || !world.isAirBlock(pool.up())
                        || !world.getBlockState(pool.down()).isFullCube()) {
                    return false;
                }
                for (EnumFacing side : EnumFacing.HORIZONTALS) {
                    int neighborX = x + side.getXOffset();
                    int neighborZ = z + side.getZOffset();
                    if (!isPoolCell(neighborX, neighborZ, width, depth)
                            && !world.getBlockState(pool.offset(side)).isFullCube()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean isPoolCell(int x, int z, int width, int depth) {
        if (x < 0 || x >= width || z < 0 || z >= depth) {
            return false;
        }
        int centeredX = 2 * x - width + 1;
        int centeredZ = 2 * z - depth + 1;
        return centeredX * centeredX * depth * depth + centeredZ * centeredZ * width * width
                <= width * width * depth * depth;
    }

    private static void removeFloatingStone(World world, BlockPos chunkPos) {
        int minX = chunkPos.getX() + 8;
        int minZ = chunkPos.getZ() + 8;
        int maxX = minX + 15;
        int maxZ = minZ + 15;
        int minY = world.getSeaLevel() + 1;
        if (!world.isAreaLoaded(new BlockPos(minX, minY, minZ),
                new BlockPos(maxX, world.getActualHeight() - 1, maxZ))) {
            return;
        }

        Set<BlockPos> checked = new HashSet<>();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = world.getHeight(new BlockPos(x, 0, z)).getY() - 1; y >= minY; y--) {
                    BlockPos start = new BlockPos(x, y, z);
                    if (checked.contains(start) || world.getBlockState(start).getBlock() != Blocks.STONE
                            || !world.isAirBlock(start.down())) {
                        continue;
                    }
                    removeFloatingPatch(world, start, minX, maxX, minZ, maxZ, minY, checked);
                }
            }
        }
    }

    private static void removeFloatingPatch(World world, BlockPos start, int minX, int maxX,
            int minZ, int maxZ, int minY, Set<BlockPos> checked) {
        ArrayDeque<BlockPos> pending = new ArrayDeque<>();
        Set<BlockPos> found = new HashSet<>();
        pending.add(start);
        found.add(start);
        boolean supported = false;
        while (!pending.isEmpty() && found.size() <= MAX_FLOATING_PATCH_SIZE) {
            BlockPos pos = pending.removeFirst();
            if (pos.getX() <= minX || pos.getX() >= maxX || pos.getZ() <= minZ || pos.getZ() >= maxZ
                    || pos.getY() <= minY || pos.getY() >= world.getActualHeight() - 1) {
                supported = true;
                break;
            }
            for (EnumFacing side : EnumFacing.values()) {
                BlockPos neighbor = pos.offset(side);
                IBlockState state = world.getBlockState(neighbor);
                if (isTerrainBlock(state.getBlock())) {
                    if (found.add(neighbor)) {
                        pending.add(neighbor);
                    }
                } else if (state.getMaterial().isSolid()) {
                    supported = true;
                }
            }
            if (supported) {
                break;
            }
        }
        checked.addAll(found);
        if (supported || !pending.isEmpty() || found.size() > MAX_FLOATING_PATCH_SIZE) {
            return;
        }
        for (BlockPos pos : found) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        }
    }

    private static boolean isTerrainBlock(Block block) {
        return block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS
                || block == Blocks.GRAVEL;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new SaplingTreeGenerator(
                () -> ModBlocks.skyris_log.getDefaultState(),
                () -> ModBlocks.skyris_leaves.getDefaultState(),
                SaplingTreeGenerator.TreeStyle.SKYRIS, rand.nextInt(3) == 0 ? 11 : 8, 2);
    }

}
