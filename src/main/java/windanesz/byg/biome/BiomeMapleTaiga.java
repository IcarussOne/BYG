package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.BygTrees;

import java.util.Random;

public class BiomeMapleTaiga
        extends Biome {

    public BiomeMapleTaiga() {
        super(new Biome.BiomeProperties("Maple Taiga").setRainfall(0.5f).setBaseHeight(0.65f).setWaterColor(-12618012).setHeightVariation(0.1f).setTemperature(0.25f));
        this.setRegistryName("byg_maple_taiga");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 14;
        this.decorator.flowersPerChunk = 3;
        this.decorator.grassPerChunk = 10;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 1;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 10;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 20;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -6719727;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -6719727;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        boolean silver = rand.nextInt(4) == 0;
        return silver ? BygTrees.SILVER_MAPLE_TAIGA : BygTrees.RED_MAPLE_TAIGA;
    }

}
