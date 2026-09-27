package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.FrozenOakTreeGenerator;

import java.util.Random;

public class BiomeFrostyForest
        extends Biome {

    public BiomeFrostyForest() {
        super(new Biome.BiomeProperties("Frosty Forest").setRainfall(0.5f).setBaseHeight(0.85f).setWaterColor(-3342337).setHeightVariation(0.2f).setTemperature(-0.5f));
        this.setRegistryName("byg_frosty_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 10;
        this.decorator.flowersPerChunk = 1;
        this.decorator.grassPerChunk = 10;
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
        return -3355444;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -3355444;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (!windanesz.byg.Config.isWoodSetEnabled("frozen_oak")) {
            return TREE_FEATURE;
        }
        return new FrozenOakTreeGenerator();
    }

}
