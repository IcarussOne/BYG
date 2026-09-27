package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;
import windanesz.byg.registry.ModBiomes;

import javax.annotation.Nullable;

public class BlockAlgae extends Block {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.015625D, 1.0D);

    public BlockAlgae() {
        super(Material.PLANTS);
        this.setRegistryName("algae");
        this.setTranslationKey("algae");
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", 0);
        this.setHardness(0.0f);
        this.setResistance(0.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(NORTH, false)
                .withProperty(EAST, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int tintIndex) {
        if (worldIn.getBiome(pos) == ModBiomes.byg_quagmire || worldIn.getBiome(pos) == ModBiomes.byg_mangrove_marshes) {
            // These murky wetlands use the same restrained algae tint rather than their grass color.
            return 0x456F43;
        }

        int grassColor = worldIn.getBiome(pos).getGrassColorAtPos(pos);
        int red = (grassColor >> 16) & 255;
        int green = (grassColor >> 8) & 255;
        int blue = grassColor & 255;

        // Keep the local biome character while biasing aquatic growth toward green.
        red = red * 3 / 4;
        green = Math.min(255, green * 5 / 4 + 20);
        blue = blue * 3 / 4 + 15;
        return (red << 16) | (green << 8) | blue;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos) && canBlockStay(world, pos);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        validatePosition(world, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        validatePosition(world, pos, state);
        if (world.isRemote) {
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state
                .withProperty(NORTH, isAlgae(worldIn, pos.offset(EnumFacing.NORTH)))
                .withProperty(EAST, isAlgae(worldIn, pos.offset(EnumFacing.EAST)))
                .withProperty(SOUTH, isAlgae(worldIn, pos.offset(EnumFacing.SOUTH)))
                .withProperty(WEST, isAlgae(worldIn, pos.offset(EnumFacing.WEST)));
    }

    private boolean isAlgae(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    private void validatePosition(World world, BlockPos pos, IBlockState state) {
        if (!canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    private static boolean canBlockStay(World world, BlockPos pos) {
        BlockPos belowPos = pos.down();
        IBlockState belowState = world.getBlockState(belowPos);
        if (belowState.getMaterial() != Material.WATER) {
            return false;
        }
        if (belowState.getPropertyKeys().contains(BlockLiquid.LEVEL) && belowState.getValue(BlockLiquid.LEVEL) != 0) {
            return false;
        }
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{NORTH, EAST, SOUTH, WEST});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        // The default Silk Touch path drops this block; ordinary harvesting yields nothing.
    }
}
