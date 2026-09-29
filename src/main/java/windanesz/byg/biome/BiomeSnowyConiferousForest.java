package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.BygTrees;

import java.util.Random;

public class BiomeSnowyConiferousForest
        extends Biome {

    public BiomeSnowyConiferousForest() {
        super(new Biome.BiomeProperties("Snowy Coniferous Forest").setRainfall(0.6f).setBaseHeight(0.85f).setWaterColor(-14329397).setHeightVariation(0.25f).setTemperature(-0.5f));
        this.setRegistryName("byg_snowy_coniferous_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 14;
        this.decorator.flowersPerChunk = 1;
        this.decorator.grassPerChunk = 5;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 1;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 45;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -14320839;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -14320839;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return rand.nextBoolean() ? BygTrees.FIR : BygTrees.FIR_TALL;
    }
}
