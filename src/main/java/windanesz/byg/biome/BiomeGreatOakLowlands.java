package windanesz.byg.biome;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.Random;

public class BiomeGreatOakLowlands
        extends Biome {
    private static final WorldGenAbstractTree GREAT_OAK_TREE = new SaplingTreeGenerator(
            () -> ModBlocks.great_oak_log.getDefaultState(),
            () -> ModBlocks.great_oak_leaves.getDefaultState(),
            SaplingTreeGenerator.TreeStyle.GREAT_OAK, 0, 0);
    private static final WorldGenAbstractTree VANILLA_OAK_TREE = new WorldGenTrees(false);

    public BiomeGreatOakLowlands() {
        super(new Biome.BiomeProperties("Great Oak Lowlands").setRainfall(0.5f).setBaseHeight(0.03f).setWaterColor(-12618012).setHeightVariation(0.01f).setTemperature(0.7f));
        this.setRegistryName("byg_great_oak_lowlands");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 2;
        this.decorator.flowersPerChunk = 3;
        this.decorator.grassPerChunk = 25;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 3;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -11770848;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -11770848;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        int tree = rand.nextInt(4);
        if (tree < 2) {
            return GREAT_OAK_TREE;
        }
        if (tree == 2) {
            return VANILLA_OAK_TREE;
        }
        return new WorldGenTrees(false, 5, ModBlocks.great_oak_log.getDefaultState(),
                ModBlocks.great_oak_leaves.getDefaultState(), false);
    }
}
