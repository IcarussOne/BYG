package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBlanketWeed
        extends Block {
    private static final String SUPPORT_POS_KEY = "BygBlanketWeedSupportPos";
    private static final String SUPPORT_START_TICK_KEY = "BygBlanketWeedSupportStartTick";
    private static final String SUPPORT_LAST_SEEN_TICK_KEY = "BygBlanketWeedSupportLastSeenTick";
    private static final int MAX_SUPPORT_TICKS = 20;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    public BlockBlanketWeed() {
        super(Material.CORAL);
        this.setRegistryName("blanket_weed");
        this.setTranslationKey("blanket_weed");
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", 0);
        this.setHardness(0.01f);
        this.setResistance(1.0f);
        this.setLightLevel(0.5f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch ((EnumFacing) state.getValue(BlockHorizontal.FACING)) {
            default: {
                return new AxisAlignedBB(1.0, 0.0, 1.0, 0.0, 0.1, 0.0);
            }
            case NORTH: {
                return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1, 1.0);
            }
            case WEST: {
                return new AxisAlignedBB(0.0, 0.0, 1.0, 1.0, 0.1, 0.0);
            }
            case EAST:
        }
        return new AxisAlignedBB(1.0, 0.0, 0.0, 0.0, 0.1, 1.0);
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

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (entityIn == null || shouldSupportEntity(worldIn, pos, entityIn)) {
            AxisAlignedBB collisionBox = this.getBoundingBox(state, worldIn, pos).offset(pos);
            if (entityBox.intersects(collisionBox)) {
                collidingBoxes.add(collisionBox);
            }
        }
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollision(worldIn, pos, state, entityIn);
        trackSupportEntity(worldIn, pos, entityIn);
        if (!shouldSupportEntity(worldIn, pos, entityIn)) {
            if (!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() == this && !hasSolidBlockUnder(worldIn, pos)) {
                worldIn.destroyBlock(pos, true);
            }
            clearSupportTracking(entityIn);
            entityIn.onGround = false;
            entityIn.motionY = Math.min(entityIn.motionY, -0.08D);
            entityIn.velocityChanged = true;
        }
    }

    private static boolean shouldSupportEntity(World world, BlockPos pos, Entity entity) {
        NBTTagCompound entityData = entity.getEntityData();
        long currentTick = world.getTotalWorldTime();
        long posKey = pos.toLong();
        long lastSupportedPos = entityData.getLong(SUPPORT_POS_KEY);
        long lastSeenTick = entityData.getLong(SUPPORT_LAST_SEEN_TICK_KEY);
        if (lastSupportedPos != posKey || currentTick - lastSeenTick > 1L) {
            return true;
        }
        long startTick = entityData.getLong(SUPPORT_START_TICK_KEY);
        return currentTick - startTick < MAX_SUPPORT_TICKS;
    }

    private static void trackSupportEntity(World world, BlockPos pos, Entity entity) {
        NBTTagCompound entityData = entity.getEntityData();
        long currentTick = world.getTotalWorldTime();
        long posKey = pos.toLong();
        long lastSupportedPos = entityData.getLong(SUPPORT_POS_KEY);
        long lastSeenTick = entityData.getLong(SUPPORT_LAST_SEEN_TICK_KEY);

        if (lastSupportedPos != posKey || currentTick - lastSeenTick > 1L) {
            entityData.setLong(SUPPORT_POS_KEY, posKey);
            entityData.setLong(SUPPORT_START_TICK_KEY, currentTick);
        }
        entityData.setLong(SUPPORT_LAST_SEEN_TICK_KEY, currentTick);
    }

    private static void clearSupportTracking(Entity entity) {
        NBTTagCompound entityData = entity.getEntityData();
        entityData.removeTag(SUPPORT_POS_KEY);
        entityData.removeTag(SUPPORT_START_TICK_KEY);
        entityData.removeTag(SUPPORT_LAST_SEEN_TICK_KEY);
    }

    private static boolean hasSolidBlockUnder(World world, BlockPos pos) {
        BlockPos belowPos = pos.down();
        IBlockState belowState = world.getBlockState(belowPos);
        return belowState.isSideSolid(world, belowPos, EnumFacing.UP);
    }

}
