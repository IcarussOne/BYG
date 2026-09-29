package windanesz.byg.biome;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.BygTreePlacement;

import java.util.Random;

public class BiomeSeasonalBirchForest
        extends Biome {
    private static final WorldGenTallGrass GRASS_GENERATOR = new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);

    public BiomeSeasonalBirchForest() {
        super(new Biome.BiomeProperties("Seasonal Birch Forest").setRainfall(0.4f).setBaseHeight(0.75f).setWaterColor(-14329397).setHeightVariation(0.1f).setTemperature(0.25f));
        this.setRegistryName("byg_seasonal_birch_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 6;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 12;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 25;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -3039697;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -3039697;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new WorldGenBirchTree(true, true);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        placeBirchTrees(worldIn, rand, pos);
        placeGroundGrass(worldIn, rand, pos);
    }

    private void placeBirchTrees(World worldIn, Random rand, BlockPos chunkPos) {
        int attempts = 6 + (rand.nextInt(10) == 0 ? 1 : 0);
        for (int i = 0; i < attempts; i++) {
            BlockPos treePos = worldIn.getHeight(chunkPos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8));
            WorldGenAbstractTree tree = this.getRandomTreeFeature(rand);
            tree.setDecorationDefaults();
            if (BygTreePlacement.allowTrees(worldIn, rand, treePos) && tree.generate(worldIn, rand, treePos)) {
                tree.generateSaplings(worldIn, rand, treePos);
            }
        }
    }

    private static void placeGroundGrass(World worldIn, Random rand, BlockPos chunkPos) {
        int attempts = 12;
        for (int i = 0; i < attempts; i++) {
            BlockPos grassPos = worldIn.getHeight(chunkPos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8));
            GRASS_GENERATOR.generate(worldIn, rand, grassPos);
        }
    }

}

