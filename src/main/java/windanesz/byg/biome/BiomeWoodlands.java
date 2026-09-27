package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.GiantOakTreeGenerator;

import java.util.Random;

public class BiomeWoodlands
        extends Biome {

    public BiomeWoodlands() {
        super(new Biome.BiomeProperties("Woodlands").setRainfall(0.7f).setBaseHeight(0.3f).setWaterColor(-12618012).setHeightVariation(0.15f).setTemperature(0.7f));
        this.setRegistryName("byg_woodlands");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 3;
        this.decorator.flowersPerChunk = 5;
        this.decorator.grassPerChunk = 15;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -11766212;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -11766212;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return rand.nextInt(5) < 2 ? new GiantOakTreeGenerator() : super.getRandomTreeFeature(rand);
    }

}


