package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import javax.annotation.Nullable;

public class BlockAspenstickpile
        extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    public BlockAspenstickpile() {
        super(Material.WOOD);
        this.setRegistryName("aspen_stick_pile");
        this.setTranslationKey("aspen_stick_pile");
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 1);
        this.setHardness(0.15f);
        this.setResistance(5.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch ((EnumFacing) state.getValue(BlockHorizontal.FACING)) {
            default: {
                return new AxisAlignedBB(1.0, 0.0, 1.0, 0.0, 0.3, 0.0);
            }
            case NORTH: {
                return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3, 1.0);
            }
            case WEST: {
                return new AxisAlignedBB(0.0, 0.0, 1.0, 1.0, 0.3, 0.0);
            }
            case EAST:
        }
        return new AxisAlignedBB(1.0, 0.0, 0.0, 0.0, 0.3, 1.0);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer((Block) this, new IProperty[]{FACING});
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex((int) meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Items.STICK, 1);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return MapColor.WOOD;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(Items.STICK, 1));
    }

}

