package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.HawthornTreeGenerator;
import windanesz.byg.worldgen.treegenerator.RowanTreeGenerator;

import java.util.Random;

public class BiomeWhisperingWoods
        extends Biome {

    public BiomeWhisperingWoods() {
        super(new Biome.BiomeProperties("Whispering Woods").setRainfall(0.5f).setBaseHeight(0.3f).setWaterColor(-14329397).setHeightVariation(0.1f).setTemperature(0.7f));
        this.setRegistryName("byg_whispering_woods");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 4;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 10;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 15;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -14123726;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -14123726;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        int roll = rand.nextInt(10);
        if (roll < 5) {
            return new HawthornTreeGenerator();
        }
        return roll < 9 ? new RowanTreeGenerator() : BIG_TREE_FEATURE;
    }

}


