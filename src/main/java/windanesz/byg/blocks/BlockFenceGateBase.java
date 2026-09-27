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
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockFenceGateBase extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private final boolean open;
    private final Supplier<Block> toggledBlock;
    private final Supplier<Block> dropBlock;

    public BlockFenceGateBase(String name, boolean open, int harvestLevel, float hardness, float resistance, @Nullable Supplier<Block> toggledBlock, @Nullable Supplier<Block> dropBlock) {
        super(Material.WOOD);
        this.open = open;
        this.toggledBlock = toggledBlock;
        this.dropBlock = dropBlock;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(open ? null : BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    private void toggle(World world, BlockPos pos) {
        if (this.toggledBlock == null) {
            return;
        }
        Block replacement = this.toggledBlock.get();
        if (replacement == null) {
            return;
        }
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                SoundEvent.REGISTRY.getObject(new ResourceLocation(this.open ? "block.fence_gate.close" : "block.fence_gate.open")),
                SoundCategory.NEUTRAL, 1.0f, 1.0f);
        world.setBlockState(pos, replacement.getDefaultState().withProperty(FACING, world.getBlockState(pos).getValue(FACING)), 3);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return this.open ? NULL_AABB : super.getCollisionBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return this.open;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            default:
                return new AxisAlignedBB(1.0, 0.0, 0.6, 0.0, 1.0, 0.4);
            case NORTH:
                return new AxisAlignedBB(0.0, 0.0, 0.4, 1.0, 1.0, 0.6);
            case WEST:
                return new AxisAlignedBB(0.4, 0.0, 1.0, 0.6, 1.0, 0.0);
            case EAST:
                return new AxisAlignedBB(0.6, 0.0, 0.0, 0.4, 1.0, 1.0);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this.dropBlock.get(), 1);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return MapColor.WOOD;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this.dropBlock.get(), 1));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        this.toggle(world, pos);
        return true;
    }
}

