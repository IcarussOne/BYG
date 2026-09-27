package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public class BiomeGrasslandPlateau
        extends Biome {

    public BiomeGrasslandPlateau() {
        super(new Biome.BiomeProperties("Grassland Plateau").setRainfall(0.4f).setBaseHeight(2.0f).setHeightVariation(0.02f).setTemperature(0.8f));
        this.setRegistryName("byg_grassland_plateau");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 0;
        this.decorator.flowersPerChunk = 3;
        this.decorator.grassPerChunk = 34;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 15;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 30;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return super.getRandomTreeFeature(rand);
    }

}


