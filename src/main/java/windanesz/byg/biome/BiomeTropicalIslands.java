package windanesz.byg.biome;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeTropicalIslands
        extends Biome {

    public BiomeTropicalIslands() {
        super(new Biome.BiomeProperties("Tropical Islands").setRainfall(0.7f).setBaseHeight(-1.0f).setWaterColor(-12331538).setHeightVariation(0.35f).setTemperature(0.5f));
        this.setRegistryName("byg_tropical_islands");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = true;
        this.decorator.treesPerChunk = 5;
        this.decorator.flowersPerChunk = 2;
        this.decorator.grassPerChunk = 15;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 50;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -10702270;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -10702270;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new CustomTree();
    }

    static class CustomTree
            extends WorldGenAbstractTree {
        CustomTree() {
            super(false);
        }

        public boolean generate(World world, Random par2Random, BlockPos pos) {
            if (world.isRemote || !windanesz.byg.Config.isPalmContentEnabled()) {
                return false;
            }
            // Decoration hands over the first free block above the surface, but tolerate the soil itself.
            Block here = world.getBlockState(pos).getBlock();
            BlockPos base = here == Blocks.GRASS || here == Blocks.DIRT ? pos.up() : pos;
            return windanesz.byg.worldgen.PalmWorldgen.newGenerator().generate(world, par2Random, base);
        }

        protected boolean canGrowInto(Block blockType) {
            Material material = blockType.getDefaultState().getMaterial();
            return material == Material.AIR || blockType == Blocks.GRASS.getDefaultState().getBlock() || blockType == Blocks.DIRT.getStateFromMeta(0).getBlock();
        }

        protected void setDirtAt(World world, BlockPos pos) {
        }

        public boolean isReplaceable(World world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return state.getBlock().isAir(state, (IBlockAccess) world, pos) || this.canGrowInto(state.getBlock()) || state.getBlock().isReplaceable((IBlockAccess) world, pos);
        }
    }

}

