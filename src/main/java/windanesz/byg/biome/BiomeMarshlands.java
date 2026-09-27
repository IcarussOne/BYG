package windanesz.byg.biome;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
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

import java.util.Random;

public class BiomeMarshlands
        extends Biome {

    private static final int WET_GRASS_COLOR = 0x24552F;
    private static final int MARSH_GRASS_COLOR = 0x35723A;
    private static final int DRY_GRASS_COLOR = 0x69713A;

    public BiomeMarshlands() {
        super(new Biome.BiomeProperties("Marshlands").setRainfall(0.7f).setBaseHeight(-0.35f).setWaterColor(0x56553A).setHeightVariation(0.0f).setTemperature(0.8f));
        this.setRegistryName("byg_marshlands");
        this.topBlock = Blocks.GRASS.getDefaultState();
        this.fillerBlock = Blocks.DIRT.getStateFromMeta(0);
        this.decorator.generateFalls = true;
        this.decorator.treesPerChunk = 0;
        this.decorator.flowersPerChunk = 0;
        this.decorator.grassPerChunk = 12;
        this.decorator.deadBushPerChunk = 0;
        this.decorator.mushroomsPerChunk = 0;
        this.decorator.bigMushroomsPerChunk = 0;
        this.decorator.reedsPerChunk = 0;
        this.decorator.cactiPerChunk = 0;
        this.decorator.sandPatchesPerChunk = 0;
        this.decorator.gravelPatchesPerChunk = 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        double broadNoise = GRASS_COLOR_NOISE.getValue(pos.getX() * 0.018D, pos.getZ() * 0.018D);
        double detailNoise = GRASS_COLOR_NOISE.getValue(pos.getX() * 0.065D + 91.0D, pos.getZ() * 0.065D - 37.0D);
        double moistureNoise = broadNoise * 0.72D + detailNoise * 0.28D;

        if (moistureNoise > 0.12D) {
            return blendColor(MARSH_GRASS_COLOR, WET_GRASS_COLOR,
                    clamp01((moistureNoise - 0.12D) / 0.58D));
        }
        if (moistureNoise < -0.18D) {
            return blendColor(MARSH_GRASS_COLOR, DRY_GRASS_COLOR,
                    clamp01((-moistureNoise - 0.18D) / 0.52D));
        }
        return MARSH_GRASS_COLOR;
    }

    private static double clamp01(double value) {
        return Math.max(0.0D, Math.min(1.0D, value));
    }

    private static int blendColor(int from, int to, double amount) {
        int red = (int) (((from >> 16) & 255) + (((to >> 16) & 255) - ((from >> 16) & 255)) * amount);
        int green = (int) (((from >> 8) & 255) + (((to >> 8) & 255) - ((from >> 8) & 255)) * amount);
        int blue = (int) ((from & 255) + ((to & 255) - (from & 255)) * amount);
        return red << 16 | green << 8 | blue;
    }

    @SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos) {
        return -13274566;
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature) {
        return -13395457;
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new CustomTree();
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        placeMarshGrass(worldIn, rand, pos);
    }

    private static void placeMarshGrass(World world, Random random, BlockPos chunkPos) {
        for (int attempt = 0; attempt < 64; attempt++) {
            BlockPos plantPos = world.getHeight(chunkPos.add(random.nextInt(16) + 8, 0, random.nextInt(16) + 8));
            if (!world.isAirBlock(plantPos) || !world.isAirBlock(plantPos.up())
                    || world.getBlockState(plantPos.down()).getBlock() != Blocks.GRASS) {
                continue;
            }

            world.setBlockState(plantPos, Blocks.DOUBLE_PLANT.getDefaultState()
                    .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                    .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER), 2);
            world.setBlockState(plantPos.up(), Blocks.DOUBLE_PLANT.getDefaultState()
                    .withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.GRASS)
                    .withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
        }
    }

    static class CustomTree
            extends WorldGenAbstractTree {
        CustomTree() {
            super(false);
        }

        public boolean generate(World world, Random rand, BlockPos position) {
            int height = rand.nextInt(5) + 10;
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
                if (ground != Blocks.GRASS.getDefaultState().getBlock() && ground != Blocks.DIRT.getStateFromMeta(0).getBlock() || ground2 != Blocks.GRASS.getDefaultState().getBlock() && ground2 != Blocks.DIRT.getStateFromMeta(0).getBlock()) {
                    return false;
                }
                world.getBlockState(position.down());
                IBlockState state;
                if (position.getY() < world.getHeight() - height - 1) {
                    int genh;
                    world.setBlockState(position.down(), Blocks.DIRT.getStateFromMeta(0), 2);
                    for (genh = position.getY() - 3 + height; genh <= position.getY() + height; ++genh) {
                        int i4 = genh - (position.getY() + height);
                        int j1 = (int) (1.0 - (double) i4 * 0.5);
                        for (int k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1) {
                            for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2) {
                                BlockPos blockpos;
                                int j2 = i2 - position.getZ();
                                if (Math.abs(k1 - position.getX()) == j1 && Math.abs(j2) == j1 && (rand.nextInt(2) == 0 || i4 == 0) || !(state = world.getBlockState(blockpos = new BlockPos(k1, genh, i2))).getBlock().isAir(state, (IBlockAccess) world, blockpos) && !state.getBlock().isLeaves(state, (IBlockAccess) world, blockpos) && state.getBlock() != Blocks.AIR.getDefaultState().getBlock() && state.getBlock() != Blocks.AIR.getDefaultState().getBlock())
                                    continue;
                                this.setBlockAndNotifyAdequately(world, blockpos, Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                    for (genh = 0; genh < height; ++genh) {
                        BlockPos genhPos = position.up(genh);
                        state = world.getBlockState(genhPos);
                        if (!state.getBlock().isAir(state, (IBlockAccess) world, genhPos) && state.getBlock() != Blocks.AIR.getDefaultState().getBlock() && state.getBlock() != Blocks.AIR.getDefaultState().getBlock())
                            continue;
                        this.setBlockAndNotifyAdequately(world, position.up(genh), Blocks.AIR.getDefaultState());
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
            return blockType.getDefaultState().getMaterial() == Material.AIR || blockType == Blocks.AIR.getDefaultState().getBlock() || blockType == Blocks.AIR.getDefaultState().getBlock() || blockType == Blocks.GRASS.getDefaultState().getBlock() || blockType == Blocks.DIRT.getStateFromMeta(0).getBlock();
        }

        protected void setDirtAt(World world, BlockPos pos) {
            if (world.getBlockState(pos).getBlock() != Blocks.DIRT.getStateFromMeta(0).getBlock()) {
                this.setBlockAndNotifyAdequately(world, pos, Blocks.DIRT.getStateFromMeta(0));
            }
        }

        public boolean isReplaceable(World world, BlockPos pos) {
            IBlockState state = world.getBlockState(pos);
            return state.getBlock().isAir(state, (IBlockAccess) world, pos) || this.canGrowInto(state.getBlock()) || state.getBlock().isReplaceable((IBlockAccess) world, pos);
        }
    }

}


