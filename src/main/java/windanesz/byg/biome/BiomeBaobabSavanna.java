package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.BygTrees;

import java.util.Random;

public class BiomeBaobabSavanna
        extends Biome {

    private static final WorldGenAbstractTree BAOBAB_TREE = BygTrees.BAOBAB;
    private static final WorldGenAbstractTree YOUNG_BAOBAB_TREE = BygTrees.BAOBAB_YOUNG;
    private static final WorldGenAbstractTree ACACIA_TREE = new WorldGenSavannaTree(false);

    public BiomeBaobabSavanna() {
        super(new Biome.BiomeProperties("Baobab Savanna").setRainfall(0.3f).setBaseHeight(0.2f).setWaterColor(-14329397).setHeightVariation(0.1f).setTemperature(1.2f));
        this.setRegistryName("byg_baobab_savanna");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 3;
        this.decorator.grassPerChunk = 10;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 15;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -4474795;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -4474795;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        int choice = rand.nextInt(5);
        return choice == 0 ? ACACIA_TREE : choice <= 2 ? YOUNG_BAOBAB_TREE : BAOBAB_TREE;
    }

}


