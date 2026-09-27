package windanesz.byg.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.Random;
import java.util.function.Supplier;

public class BiomeSeasonalTaiga
        extends Biome {

    public BiomeSeasonalTaiga() {
        super(new Biome.BiomeProperties("Seasonal Taiga").setRainfall(0.5f).setBaseHeight(0.7f).setWaterColor(-12618012).setHeightVariation(0.15f).setTemperature(0.25f));
        this.setRegistryName("byg_seasonal_taiga");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 5;
        this.decorator.flowersPerChunk = 6;
        this.decorator.grassPerChunk = 13;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 1;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 12;
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
        int leaves = rand.nextInt(3);
        Supplier<IBlockState> leavesState = () -> (leaves == 0 ? ModBlocks.spruce_leaves_orange : leaves == 1 ? ModBlocks.spruce_leaves_red : ModBlocks.spruce_leaves_yellow).getDefaultState();
        int shape = rand.nextInt(6);
        if (shape == 0) {
            return new SaplingTreeGenerator(() -> Blocks.LOG.getStateFromMeta(1), leavesState,
                    SaplingTreeGenerator.TreeStyle.CONIFER, 7, 3);
        }
        if (shape == 1) {
            return new SaplingTreeGenerator(() -> Blocks.LOG.getStateFromMeta(1), leavesState,
                    SaplingTreeGenerator.TreeStyle.TALL_CONIFER, 10, 4);
        }
        return new SaplingTreeGenerator(() -> Blocks.LOG.getStateFromMeta(1), leavesState,
                SaplingTreeGenerator.TreeStyle.BLUE_SPRUCE, 11, 8);
    }

}
