package windanesz.byg.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.StellataTreeGenerator;

import java.util.Random;

public class BiomeStellataPasture
        extends Biome {

    public BiomeStellataPasture() {
        super(new Biome.BiomeProperties("Stellata Pasture").setRainfall(0.6f).setBaseHeight(0.25f).setWaterColor(-13804098).setHeightVariation(0.15f).setTemperature(0.75f));
        this.setRegistryName("byg_stellata_pasture");
        this.topBlock = windanesz.byg.registry.ModBlocks.pasture_grass.getDefaultState();
        this.fillerBlock = windanesz.byg.registry.ModBlocks.pasture_dirt.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 1;
        this.decorator.flowersPerChunk = 3;
        this.decorator.grassPerChunk = 7;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 15;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 15;
        this.decorator.gravelPatchesPerChunk = 5;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -8205275;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -8205275;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new StellataTreeGenerator(rand.nextBoolean());
    }

}
