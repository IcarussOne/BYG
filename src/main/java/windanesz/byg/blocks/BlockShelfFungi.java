package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import javax.annotation.Nullable;

public class BlockShelfFungi extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 2);

    public BlockShelfFungi() {
        super(Material.PLANTS);
        this.setRegistryName("shelf_fungi");
        this.setTranslationKey("shelf_fungi");
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.2f);
        this.setResistance(1.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(VARIANT, 0));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int variant = state.getValue(VARIANT);
        double minY = 0.125D + variant * 0.1875D;
        double maxY = minY + 0.25D;
        double depth = 0.3125D;
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case SOUTH:
                return new AxisAlignedBB(0.125D, minY, 1.0D - depth, 0.875D, maxY, 1.0D);
            case WEST:
                return new AxisAlignedBB(0.0D, minY, 0.125D, depth, maxY, 0.875D);
            case EAST:
                return new AxisAlignedBB(1.0D - depth, minY, 0.125D, 1.0D, maxY, 0.875D);
            case NORTH:
            default:
                return new AxisAlignedBB(0.125D, minY, 0.0D, 0.875D, maxY, depth);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (this.canStay(worldIn, pos, facing)) {
                return true;
            }
        }
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side.getAxis().isHorizontal() && this.canStay(worldIn, pos, side.getOpposite());
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing horizontalFacing = facing.getAxis().isHorizontal() ? facing.getOpposite() : placer.getHorizontalFacing();
        if (!this.canStay(worldIn, pos, horizontalFacing)) {
            for (EnumFacing testFacing : EnumFacing.HORIZONTALS) {
                if (this.canStay(worldIn, pos, testFacing)) {
                    horizontalFacing = testFacing;
                    break;
                }
            }
        }
        return this.getDefaultState()
                .withProperty(FACING, horizontalFacing)
                .withProperty(VARIANT, this.computeVariant(worldIn, pos));
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.refreshVariant(worldIn, pos, state);
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canStay(worldIn, pos, state.getValue(FACING))) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return;
        }
        this.refreshVariant(worldIn, pos, state);
    }

    private void refreshVariant(World worldIn, BlockPos pos, IBlockState currentState) {
        if (worldIn.isRemote) {
            return;
        }
        int expectedVariant = this.computeVariant(worldIn, pos);
        if (currentState.getValue(VARIANT) != expectedVariant) {
            worldIn.setBlockState(pos, currentState.withProperty(VARIANT, expectedVariant), 2);
        }
    }

    private int computeVariant(IBlockAccess world, BlockPos pos) {
        int base = Math.floorMod(pos.getX() * 31 + pos.getY() * 17 + pos.getZ() * 13, 3);
        if (world.getBlockState(pos.down()).getBlock() == this) {
            base = (base + 1) % 3;
        }
        if (world.getBlockState(pos.up()).getBlock() == this) {
            base = (base + 2) % 3;
        }
        return base;
    }

    private boolean canStay(World world, BlockPos pos, EnumFacing facing) {
        BlockPos supportPos = pos.offset(facing);
        IBlockState supportState = world.getBlockState(supportPos);
        return supportState.isSideSolid(world, supportPos, facing.getOpposite());
    }

    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, VARIANT});
    }

    public IBlockState getStateFromMeta(int meta) {
        int horizontal = meta & 3;
        int variant = (meta >> 2) & 3;
        if (variant > 2) {
            variant = 2;
        }
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.byHorizontalIndex(horizontal))
                .withProperty(VARIANT, variant);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | (state.getValue(VARIANT) << 2);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return MapColor.BROWN;
    }
}
