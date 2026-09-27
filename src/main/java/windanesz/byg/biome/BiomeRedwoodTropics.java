package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.RedwoodTreeGenerator;

import java.util.Random;

public class BiomeRedwoodTropics
        extends Biome {
    private static final WorldGenAbstractTree REDWOOD_TREE = new RedwoodTreeGenerator();

    public BiomeRedwoodTropics() {
        super(new Biome.BiomeProperties("Redwood Tropics").setRainfall(0.8f).setBaseHeight(0.5f).setWaterColor(-12618012).setHeightVariation(0.1f).setTemperature(0.95f));
        this.setRegistryName("byg_redwood_tropics");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 8;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 20;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 30;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 35;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -11167488;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -11167488;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return REDWOOD_TREE;
    }
}

