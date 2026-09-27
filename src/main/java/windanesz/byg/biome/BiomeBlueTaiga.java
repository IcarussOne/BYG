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

public class BiomeBlueTaiga
        extends Biome {

    public BiomeBlueTaiga() {
        super(new Biome.BiomeProperties("Blue Taiga").setRainfall(0.5f).setBaseHeight(0.8f).setWaterColor(-12618012).setHeightVariation(0.15f).setTemperature(0.25f));
        this.setRegistryName("byg_blue_taiga");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 4;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 7;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 35;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -7817344;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -7817344;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        // One third regular green spruces, the rest blue.
        boolean green = rand.nextInt(3) == 0;
        return new SaplingTreeGenerator(() -> Blocks.LOG.getStateFromMeta(1),
                () -> green ? Blocks.LEAVES.getStateFromMeta(1) : ModBlocks.spruce_leaves_blue.getDefaultState(),
                SaplingTreeGenerator.TreeStyle.BLUE_SPRUCE, 11, 8);
    }

}
