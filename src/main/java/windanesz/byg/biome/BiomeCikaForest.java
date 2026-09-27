package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.Random;

public class BiomeCikaForest
        extends Biome {

    public BiomeCikaForest() {
        super(new Biome.BiomeProperties("Cika Forest").setRainfall(0.4f).setBaseHeight(0.8f).setWaterColor(-12618012).setHeightVariation(0.02f).setTemperature(0.25f));
        this.setRegistryName("byg_cika_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 4;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 12;
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
        return -6328279;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -6328279;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    // Height mix mirrors the retired cikatree1-11 templates (13-32 tall): a few saplings, mostly mid and tall trees.
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        int roll = rand.nextInt(10);
        int minHeight = roll < 2 ? 13 : roll < 6 ? 22 : 27;
        int extraHeight = roll < 2 ? 4 : 5;
        return new SaplingTreeGenerator(() -> ModBlocks.cika_log.getDefaultState(), () -> ModBlocks.cika_leaves.getDefaultState(),
                SaplingTreeGenerator.TreeStyle.CIKA, minHeight, extraHeight);
    }

}
