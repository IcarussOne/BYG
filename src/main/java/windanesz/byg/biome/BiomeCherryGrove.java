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

public class BiomeCherryGrove
        extends Biome {

    public BiomeCherryGrove() {
        super(new Biome.BiomeProperties("Cherry Grove").setRainfall(0.6f).setBaseHeight(0.55f).setWaterColor(-13281615).setHeightVariation(0.2f).setTemperature(0.75f));
        this.setRegistryName("byg_cherry_grove");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 8;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 20;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 15;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 15;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -8003482;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -8003482;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        final boolean pink = rand.nextBoolean();
        int roll = rand.nextInt(100);
        SaplingTreeGenerator.TreeStyle style;
        int minHeight;
        int extraHeight;
        if (roll < 20) {
            style = SaplingTreeGenerator.TreeStyle.CHERRY_BLOSSOM;
            minHeight = 5;
            extraHeight = 2;
        } else if (roll < 45) {
            style = SaplingTreeGenerator.TreeStyle.CHERRY_UMBRELLA;
            minHeight = 10;
            extraHeight = 1;
        } else if (roll < 75) {
            style = SaplingTreeGenerator.TreeStyle.CHERRY_SPREADING;
            minHeight = 6;
            extraHeight = 1;
        } else if (roll < 90) {
            style = SaplingTreeGenerator.TreeStyle.CHERRY_GRAND;
            minHeight = 18;
            extraHeight = 2;
        } else {
            style = SaplingTreeGenerator.TreeStyle.CHERRY_ANCIENT;
            minHeight = 23;
            extraHeight = 2;
        }
        return new SaplingTreeGenerator(
                () -> ModBlocks.cherry_log.getDefaultState(),
                () -> (pink ? ModBlocks.cherry_leaves_pink : ModBlocks.cherry_leaves_white).getDefaultState(),
                style, minHeight, extraHeight);
    }
}
