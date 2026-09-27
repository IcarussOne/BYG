package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Supplier;

public class BlockDoorPartBase extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private final boolean open;
    private final boolean topHalf;
    private final Supplier<Item> doorItem;
    private final Supplier<Block> toggledBlock;
    private final Supplier<Block> mismatchedOtherHalf;

    public BlockDoorPartBase(String registryName, boolean open, boolean topHalf, Supplier<Item> doorItem, Supplier<Block> toggledBlock, Supplier<Block> mismatchedOtherHalf) {
        super(Material.WOOD);
        this.open = open;
        this.topHalf = topHalf;
        this.doorItem = doorItem;
        this.toggledBlock = toggledBlock;
        this.mismatchedOtherHalf = mismatchedOtherHalf;
        this.setRegistryName(registryName);
        this.setTranslationKey(registryName);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 1);
        this.setHardness(1.0f);
        this.setResistance(10.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    private void toggle(World world, BlockPos pos) {
        Block block = this.toggledBlock.get();
        if (block == null) {
            return;
        }
        String sound = this.open ? "block.wooden_door.close" : "block.wooden_door.open";
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation(sound)), SoundCategory.NEUTRAL, 1.0f, 1.0f);
        world.setBlockState(pos, block.getDefaultState().withProperty(FACING, this.getDefaultState().withProperty(FACING, world.getBlockState(pos).getValue(FACING)).getValue(FACING)), 3);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
        EnumFacing facing = state.getValue(FACING);
        if (!this.open) {
            switch (facing) {
                case NORTH:
                    return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.2);
                case WEST:
                    return new AxisAlignedBB(0.0, 0.0, 1.0, 0.2, 1.0, 0.0);
                case EAST:
                    return new AxisAlignedBB(1.0, 0.0, 0.0, 0.8, 1.0, 1.0);
                default:
                    return new AxisAlignedBB(1.0, 0.0, 1.0, 0.0, 1.0, 0.8);
            }
        }
        switch (facing) {
            case NORTH:
                return new AxisAlignedBB(0.0, 0.0, 0.0, 0.2, 1.0, 1.0);
            case WEST:
                return new AxisAlignedBB(0.0, 0.0, 1.0, 1.0, 1.0, 0.8);
            case EAST:
                return new AxisAlignedBB(1.0, 0.0, 0.0, 0.0, 1.0, 0.2);
            default:
                return new AxisAlignedBB(1.0, 0.0, 1.0, 0.8, 1.0, 0.0);
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this.doorItem.get(), 1);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this.doorItem.get(), 1));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, neighborBlock, fromPos);
        BlockPos otherHalfPos = this.topHalf ? pos.down() : pos.up();
        Block otherHalf = this.mismatchedOtherHalf.get();
        if (otherHalf != null && world.getBlockState(otherHalfPos).getBlock() == otherHalf) {
            this.toggle(world, pos);
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        boolean removed = super.removedByPlayer(state, world, pos, player, willHarvest);
        world.setBlockToAir(this.topHalf ? pos.down() : pos.up());
        return removed;
    }

    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion) {
        super.onExplosionDestroy(world, pos, explosion);
        world.setBlockToAir(this.topHalf ? pos.down() : pos.up());
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        this.toggle(world, pos);
        return true;
    }
}
