package windanesz.byg.blocks;

import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.registry.ModItems;

import java.util.Random;

/**
 * A spile and bucket hung on the sap side of a sappy maple log. It slowly fills with sap, and a glass
 * bottle empties it into a bottle of maple sap. Each emptying may dry the log out.
 */
public class BlockMapleTap extends Block {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger FILL = PropertyInteger.create("fill", 0, 3);

    public BlockMapleTap() {
        super(Material.WOOD);
        setTranslationKey("maple_tap");
        setRegistryName("maple_tap");
        setCreativeTab(BYGTab.tab);
        setHardness(0.5f);
        setSoundType(SoundType.WOOD);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(FILL, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, FILL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(FILL, (meta >> 2) & 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | (state.getValue(FILL) << 2);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        // Facing is the side the tap points to, so the log sits on the opposite edge of the block.
        switch (state.getValue(FACING)) {
            case NORTH:
                return new AxisAlignedBB(5 / 16.0, 2 / 16.0, 6 / 16.0, 11 / 16.0, 12 / 16.0, 1.0);
            case EAST:
                return new AxisAlignedBB(0.0, 2 / 16.0, 5 / 16.0, 10 / 16.0, 12 / 16.0, 11 / 16.0);
            case WEST:
                return new AxisAlignedBB(6 / 16.0, 2 / 16.0, 5 / 16.0, 1.0, 12 / 16.0, 11 / 16.0);
            default:
                return new AxisAlignedBB(5 / 16.0, 2 / 16.0, 0.0, 11 / 16.0, 12 / 16.0, 10 / 16.0);
        }
    }

    /** A tap hangs on the sap side of a sappy maple log; facing is the side the tap points to. */
    private static boolean isMapleLog(World world, BlockPos pos, EnumFacing facing) {
        IBlockState log = world.getBlockState(pos.offset(facing.getOpposite()));
        return log.getBlock() == ModBlocks.sappy_maple_log && log.getValue(BlockSappyMapleLog.SAP_SIDE) == facing;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
        return side.getAxis().isHorizontal() && isMapleLog(world, pos, side) && super.canPlaceBlockAt(world, pos);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
            if (isMapleLog(world, pos, facing)) {
                return super.canPlaceBlockAt(world, pos);
            }
        }
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                            int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, facing.getAxis().isHorizontal() ? facing : placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!isMapleLog(world, pos, state.getValue(FACING))) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        scheduleFill(world, pos, state);
    }

    private void scheduleFill(World world, BlockPos pos, IBlockState state) {
        if (state.getValue(FILL) < 3 && !world.isUpdateScheduled(pos, this)) {
            world.scheduleUpdate(pos, this, Config.getMapleTapStageTickRate());
        }
    }

    // Scheduled ticks advance the fill one stage each, so a full tap takes three times the configured stage tick rate.
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) {
            return;
        }
        int fill = state.getValue(FILL);
        if (fill >= 3) {
            return;
        }
        IBlockState next = state.withProperty(FILL, fill + 1);
        world.setBlockState(pos, next, 2);
        scheduleFill(world, pos, next);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack held = player.getHeldItem(hand);
        if (state.getValue(FILL) < 3 || held.getItem() != Items.GLASS_BOTTLE) {
            return false;
        }
        if (!world.isRemote) {
            if (!player.capabilities.isCreativeMode) {
                held.shrink(1);
            }
            ItemStack sap = new ItemStack(ModItems.maple_sap);
            if (!player.inventory.addItemStackToInventory(sap)) {
                player.dropItem(sap, false);
            }
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
                Advancement advancement = serverPlayer.getServer().getAdvancementManager()
                        .getAdvancement(new ResourceLocation(BiomesYouGo.MODID, "maple_sap"));
                if (advancement != null) {
                    serverPlayer.getAdvancements().grantCriterion(advancement, "collect_sap");
                }
            }
            IBlockState emptied = state.withProperty(FILL, 0);
            world.setBlockState(pos, emptied, 2);
            scheduleFill(world, pos, emptied);
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (world.rand.nextDouble() < Config.getMapleTapLogDepletionChance()) {
                // The tap loses its sappy log and pops off through neighborChanged.
                world.setBlockState(pos.offset(state.getValue(FACING).getOpposite()), ModBlocks.maple_log.getDefaultState(), 3);
            }
        }
        return true;
    }
}
