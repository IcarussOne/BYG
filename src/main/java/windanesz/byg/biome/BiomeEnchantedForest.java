package windanesz.byg.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.worldgen.treegenerator.BygWood;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BiomeEnchantedForest extends Biome {
    private static final WorldGenAbstractTree[] TREES = buildTrees();

    // Replaces the old enchantedblue*/enchantedgreen* NBT templates: every leaf
    // colour in three canopy shapes, with either log colour, plus a rare tall
    // blue tree standing in for the 30-block template.
    private static WorldGenAbstractTree[] buildTrees() {
        List<Supplier<IBlockState>> logs = Arrays.<Supplier<IBlockState>>asList(
                BygWood.BLUE_ENCHANTED.log(),
                () -> ModBlocks.green_enchanted_log.getDefaultState());
        List<BygWood> woods = Arrays.asList(BygWood.BLUE_ENCHANTED, BygWood.PINK_ENCHANTED, BygWood.PURPLE_ENCHANTED);
        List<WorldGenAbstractTree> trees = new ArrayList<>();
        for (Supplier<IBlockState> log : logs) {
            for (BygWood wood : woods) {
                trees.add(new SaplingTreeGenerator(log, wood.leaves(), SaplingTreeGenerator.TreeStyle.ENCHANTED_TIERED, 16, 3));
                trees.add(new SaplingTreeGenerator(log, wood.leaves(), SaplingTreeGenerator.TreeStyle.ENCHANTED_TIERED, 12, 2));
                trees.add(new SaplingTreeGenerator(log, wood.leaves(), SaplingTreeGenerator.TreeStyle.ENCHANTED_STEPPED, 9, 2));
            }
            trees.add(new SaplingTreeGenerator(log, BygWood.BLUE_ENCHANTED.leaves(), SaplingTreeGenerator.TreeStyle.ENCHANTED_TIERED, 24, 3));
        }
        return trees.toArray(new WorldGenAbstractTree[0]);
    }

    public BiomeEnchantedForest() {
        super(new Biome.BiomeProperties("Enchanted Forest").setRainfall(0.5f).setBaseHeight(0.6f).setWaterColor(-6668618).setHeightVariation(0.15f).setTemperature(0.6f));
        this.setRegistryName("byg_enchanted_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 4;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 18;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 2;
        this.decorator.bigMushroomsPerChunk = 1;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -15620224;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -15620224;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -5916161;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return TREES[rand.nextInt(TREES.length)];
    }
}
