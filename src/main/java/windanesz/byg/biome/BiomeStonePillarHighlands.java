package windanesz.byg.biome;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.BygTreePlacement;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;
import windanesz.byg.worldgen.WorldGenStonePillarHighlands;

import java.util.Random;

public class BiomeStonePillarHighlands extends Biome {

    private static final WorldGenStonePillarHighlands HIGHLANDS_PILLAR_GENERATOR = new WorldGenStonePillarHighlands();
    private static final WorldGenTallGrass GRASS_GENERATOR = new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    private static final WorldGenTallGrass FERN_GENERATOR = new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
    private static final WorldGenVines VINE_GENERATOR = new WorldGenVines();
    
    private static final WorldGenAbstractTree CLIFF_PINE_TREE = new SaplingTreeGenerator(
            () -> Blocks.LOG.getStateFromMeta(1), // Spruce log
            () -> Blocks.LEAVES.getStateFromMeta(1), // Spruce leaves
            SaplingTreeGenerator.TreeStyle.ROUND,
            4, // Small height
            0  // Minimal trunk height
    );
    
    // Valley floor subtropical trees - large jungle-like trees
    private static final WorldGenShrub VALLEY_SHRUB = new WorldGenShrub(
            Blocks.LOG.getStateFromMeta(3), // Jungle log
            Blocks.LEAVES.getStateFromMeta(3)  // Jungle leaves
    );

    public BiomeStonePillarHighlands() {
        super(new Biome.BiomeProperties("Stone Pillar Highlands")
                .setRainfall(1.2f) // Very humid, rainforest-like
                .setBaseHeight(1.8f) // Very high base elevation
                .setWaterColor(-14654674) // Blue-gray misty water
                .setHeightVariation(1.5f) // Extreme variation for pillars
                .setTemperature(0.9f)); // Warm subtropical rainforest temperature
        this.setRegistryName("byg_mesa_highlands");
        this.topBlock = Blocks.GRASS.getDefaultState(); // Grass top
        this.fillerBlock = Blocks.STONE.getDefaultState(); // Stone filler
        this.decorator.generateFalls = true; // Waterfalls plunging from pillars
        this.decorator.treesPerChunk = 0; // Custom decoration
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 0; // Custom decoration
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 1;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 2;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.waterlilyPerChunk = 0;
        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityParrot.class, 40, 1, 2));
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        // Dark mossy green
        return 0x3A5A3A; // RGB: 58, 90, 58
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        // Dark green canopy
        return 0x2D4D2D; // RGB: 45, 77, 45
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float temperature) {
        // Blue-gray atmospheric fog
        return 0x7A9BB0; // RGB: 122, 155, 176
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return CLIFF_PINE_TREE;
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        // Generate massive stone pillars
        // Highlands pillars are very large (up to 35 block radius) - spawn rarely to minimize cascading
        // Only 1 pillar per chunk instead of 3-5 to reduce cascading worldgen
        int pillarAttempts = rand.nextInt(2); // 0-1 pillars per chunk
        for (int i = 0; i < pillarAttempts; i++) {
            int x = pos.getX() + rand.nextInt(16) + 8;
            int z = pos.getZ() + rand.nextInt(16) + 8;
            HIGHLANDS_PILLAR_GENERATOR.generate(worldIn, rand, new BlockPos(x, 0, z));
        }

        // NOTE: super.decorate() is NOT called to prevent cascading worldgen from vanilla decorators
        // (lakes, dungeons, ores, etc. that use rand.nextInt(16) + 8 pattern)
        // All decoration is handled by custom methods and FlowerWorldgenRegistry

        // Valley floor vegetation - dense subtropical forest
        placeValleyVegetation(worldIn, rand, pos);
        
        // Cliff vegetation - moss, ferns, vines
        placeCliffVegetation(worldIn, rand, pos);
        
        // Plateau top vegetation - sparse flowering shrubs
        placePlateauVegetation(worldIn, rand, pos);
    }

    private void placeValleyVegetation(World worldIn, Random rand, BlockPos chunkPos) {
        // Dense forest in valleys (low Y levels)
        int attempts = 40 + rand.nextInt(20);
        for (int i = 0; i < attempts; i++) {
            BlockPos targetPos = chunkPos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8);
            BlockPos surfacePos = worldIn.getHeight(targetPos);
            
            // Only place in valleys (below Y=80)
            if (surfacePos.getY() < 80) {
                // Dense grass and ferns
                if (rand.nextBoolean()) {
                    GRASS_GENERATOR.generate(worldIn, rand, surfacePos.up());
                } else {
                    FERN_GENERATOR.generate(worldIn, rand, surfacePos.up());
                }
                
                // Occasional shrubs/small trees
                if (rand.nextInt(8) == 0) {
                    VALLEY_SHRUB.generate(worldIn, rand, surfacePos.up());
                }
            }
        }
    }

    private void placeCliffVegetation(World worldIn, Random rand, BlockPos chunkPos) {
        // Hanging vines and moss on cliff faces
        int vineAttempts = 30 + rand.nextInt(20);
        for (int i = 0; i < vineAttempts; i++) {
            int x = chunkPos.getX() + rand.nextInt(16) + 8;
            int y = 60 + rand.nextInt(140); // Mid to high elevations
            int z = chunkPos.getZ() + rand.nextInt(16) + 8;
            VINE_GENERATOR.generate(worldIn, rand, new BlockPos(x, y, z));
        }
        
        // Cliff-edge windswept trees
        int cliffTreeAttempts = 2 + rand.nextInt(2);
        for (int i = 0; i < cliffTreeAttempts; i++) {
            int x = chunkPos.getX() + rand.nextInt(16) + 8;
            int z = chunkPos.getZ() + rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(new BlockPos(x, 0, z));
            
            // Only on elevated positions (cliff edges)
            if (surfacePos.getY() > 90 && rand.nextInt(3) == 0 && BygTreePlacement.allowTrees(worldIn, rand, surfacePos)) {
                CLIFF_PINE_TREE.generate(worldIn, rand, surfacePos.up());
            }
        }
    }

    private void placePlateauVegetation(World worldIn, Random rand, BlockPos chunkPos) {
        // Sparse flowering shrubs on pillar tops
        int shrubAttempts = 5 + rand.nextInt(5);
        for (int i = 0; i < shrubAttempts; i++) {
            int x = chunkPos.getX() + rand.nextInt(16) + 8;
            int z = chunkPos.getZ() + rand.nextInt(16) + 8;
            BlockPos surfacePos = worldIn.getHeight(new BlockPos(x, 0, z));
            
            // Only on very high plateaus (pillar tops)
            if (surfacePos.getY() > 140) {
                // Sparse grass
                if (rand.nextInt(3) == 0) {
                    GRASS_GENERATOR.generate(worldIn, rand, surfacePos.up());
                }
            }
        }
    }
}

