package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import windanesz.byg.worldgen.treegenerator.BygTrees;

import java.util.Random;

public class BiomeAmaranthFields
        extends Biome {

    public BiomeAmaranthFields() {
        super(new Biome.BiomeProperties("Amaranth Fields").setRainfall(0.6f).setBaseHeight(0.25f).setHeightVariation(0.15f).setTemperature(0.8f));
        this.setRegistryName("byg_amaranth_fields");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = true;
        this.decorator.treesPerChunk = 1;
        this.decorator.flowersPerChunk = 0;
        this.decorator.grassPerChunk = 10;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 15;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 15;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (!windanesz.byg.Config.isWoodSetEnabled("jacaranda")) {
            return TREE_FEATURE;
        }
        return rand.nextInt(3) != 0 ? BygTrees.JACARANDA_LARGE : BygTrees.JACARANDA;
    }

}

