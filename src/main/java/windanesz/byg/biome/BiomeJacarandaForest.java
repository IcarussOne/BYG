package windanesz.byg.biome;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.worldgen.treegenerator.SaplingTreeGenerator;

import java.util.Random;

public class BiomeJacarandaForest
        extends Biome {

    public BiomeJacarandaForest() {
        super(new Biome.BiomeProperties("Jacaranda Forest").setRainfall(0.5f).setBaseHeight(0.6f).setWaterColor(-12618012).setHeightVariation(0.03f).setTemperature(0.7f));
        this.setRegistryName("byg_jacaranda_forest");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getDefaultState();
        this.decorator.generateFalls = false;
        this.decorator.treesPerChunk = 14;
        this.decorator.flowersPerChunk = 4;
        this.decorator.grassPerChunk = 9;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos) {
        return -8732583;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -8732583;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new SaplingTreeGenerator(
                () -> windanesz.byg.registry.ModBlocks.jacaranda_log.getDefaultState(),
                () -> windanesz.byg.registry.ModBlocks.jacaranda_leaves.getDefaultState(),
                rand.nextInt(3) == 0
                        ? SaplingTreeGenerator.TreeStyle.JACARANDA_TALL
                        : SaplingTreeGenerator.TreeStyle.JACARANDA,
                rand.nextInt(3) != 0 ? 8 : 6, 2);
    }

    static class CustomTree
            extends WorldGenAbstractTree {
        CustomTree() {
            super(false);
        }

        public boolean generate(World world, Random rand, BlockPos position) {
            int height = rand.nextInt(5) + 5;
            boolean spawnTree = true;
            if (position.getY() >= 1 && position.getY() + height + 1 <= world.getHeight()) {
                for (int j = position.getY(); j <= position.getY() + 1 + height; ++j) {
                    int k = 1;
                    if (j == position.getY()) {
                        k = 0;
                    }
                    if (j >= position.getY() + height - 1) {
                        k = 2;
                    }
                    for (int px = position.getX() - k; px <= position.getX() + k && spawnTree; ++px) {
                        for (int pz = position.getZ() - k; pz <= position.getZ() + k && spawnTree; ++pz) {
                            if (j >= 0 && j < world.getHeight()) {
                                if (this.isReplaceable(world, new BlockPos(px, j, pz))) continue;
                                spawnTree = false;
                                continue;
                            }
                            spawnTree = false;
                        }
                    }
                }
                if (!spawnTree) {
                    return false;
                }
                Block ground = world.getBlockState(position.add(0, -1, 0)).getBlock();
                Block ground2 = world.getBlockState(position.add(0, -2, 0)).getBlock();
                if (ground != Blocks.GRASS.getDefaultState().getBlock() && ground != Blocks.DIRT.getDefaultState().getBlock() || ground2 != Blocks.GRASS.getDefaultState().getBlock() && ground2 != Blocks.DIRT.getDefaultState().getBlock()) {
                    return false;
                }
                world.getBlockState(position.down());
                IBlockState state;
                if (position.getY() < world.getHeight() - height - 1) {
                    int genh;
                    world.setBlockState(position.down(), Blocks.DIRT.getDefaultState(), 2);
                    for (genh = position.getY() - 3 + height; genh <= position.getY() + height; ++genh) {
                        int i4 = genh - (position.getY() + height);
                        int j1 = (int) (1.0 - (double) i4 * 0.5);
                        for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1) {
                            for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2) {
                                BlockPos blockpos;
                                int j2 = i2 - position.getZ();
                                if (Math.abs(k1 - position.getX()) == j1 && Math.abs(j2) == j1 && (rand.nextInt(2) == 0 || i4 == 0) || !(state = world.getBlockState(blockpos = new BlockPos(k1, genh, i2))).getBlock().isAir(state, (IBlockAccess) world, blockpos) && !state.getBlock().isLeaves(state, (IBlockAccess) world, blockpos) && state.getBlock() != Blocks.AIR.getDefaultState().getBlock() && state.getBlock() != windanesz.byg.registry.ModBlocks.jacaranda_leaves.getDefaultState().getBlock())
                                    continue;
                                this.setBlockAndNotifyAdequately(world, blockpos, windanesz.byg.registry.ModBlocks.jacaranda_leaves.getDefaultState());
                            }
                        }
                    }
                    for (genh = 0; genh < height; ++genh) {
                        BlockPos genhPos = position.up(genh);
                        state = world.getBlockState(genhPos);
                        if (!state.getBlock().isAir(state, (IBlockAccess) world, genhPos) && state.getBlock() != Blocks.AIR.getDefaultState().getBlock() && state.getBlock() != windanesz.byg.registry.ModBlocks.jacaranda_leaves.getDefaultState().getBlock())
                            continue;
                        this.setBlockAndNotifyAdequately(world, position.up(genh), windanesz.byg.registry.ModBlocks.jacaranda_log.getDefaultState());
                    }
                    if (rand.nextInt(4) == 0 && height > 5) {
                        for (int hlevel = 0; hlevel < 2; ++hlevel) {
                            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                                if (rand.nextInt(4 - hlevel) != 0) continue;
                                EnumFacing enumfacing1 = enumfacing.getOpposite();
                                this.setBlockAndNotifyAdequately(world, position.add(enumfacing1.getXOffset(), height - 5 + hlevel, enumfacing1.getZOffset()), Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
            return false;
        }

        private void addVines(World world, BlockPos pos) {
            this.setBlockAndNotifyAdequately(world, pos, Blocks.AIR.getDefaultState());
            BlockPos blockpos = pos.down();
            for (int i = 5; world.isAirBlock(blockpos) && i > 0; --i) {
                this.setBlockAndNotifyAdequately(world, blockpos, Blocks.AIR.getDefaultState());
                blockpos = blockpos.down();
            }
        }

        protected boolean canGrowInto(Block blockType) {
            return blockType.getDefaultState().getMaterial() == Material.AIR || blockType == windanesz.byg.registry.ModBlocks.jacaranda_log.getDefaultState().getBlock() || blockType == windanesz.byg.registry.ModBlocks.jacaranda_leaves.getDefaultState().getBlock() || blockType == Blocks.GRASS.getDefaultState().getBlock() || blockType == Blocks.DIRT.getDefaultState().getBlock();
        }

        protected void setDirtAt(World world, BlockPos pos) {
            if (world.getBlockState(pos).getBlock() != Blocks.DIRT.getDefaultState().getBlock()) {
                this.setBlockAndNotifyAdequately(world, pos, Blocks.DIRT.getDefaultState());
            }
        }

        public boolean isReplaceable(World world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return state.getBlock().isAir(state, (IBlockAccess) world, pos) || this.canGrowInto(state.getBlock()) || state.getBlock().isReplaceable((IBlockAccess) world, pos);
        }
    }

}



