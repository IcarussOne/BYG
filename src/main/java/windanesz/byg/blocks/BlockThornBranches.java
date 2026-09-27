package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

public class BlockThornBranches
        extends Block {
    // One state per axis: south is vertical, up runs east-west, east runs north-south.
    public static final PropertyDirection FACING = PropertyDirection.create("facing",
            facing -> facing == EnumFacing.SOUTH || facing == EnumFacing.UP || facing == EnumFacing.EAST);


    public BlockThornBranches() {
        super(Material.WOOD);
        this.setRegistryName("thorn_branches");
        this.setTranslationKey("thorn_branches");
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 1);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case SOUTH:
                return new AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8);
            case EAST:
                return new AxisAlignedBB(0.2, 0.2, 0.0, 0.8, 0.8, 1.0);
            case UP:
                return new AxisAlignedBB(0.0, 0.2, 0.2, 1.0, 0.8, 0.8);
            default:
                return FULL_BLOCK_AABB;
        }
    }

    public int tickRate(World world) {
        return 2600;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public IBlockState getStateFromMeta(int meta) {
        // Old worlds used all six facings. Fold opposite directions onto the
        // matching axis without changing their appearance.
        EnumFacing facing = EnumFacing.byIndex(meta);
        EnumFacing axis = facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? EnumFacing.SOUTH
                : facing == EnumFacing.WEST || facing == EnumFacing.EAST ? EnumFacing.EAST : EnumFacing.UP;
        return this.getDefaultState().withProperty(FACING, axis);
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, axisFacing(facing));
    }

    private static EnumFacing axisFacing(EnumFacing facing) {
        if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) return EnumFacing.UP;
        if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) return EnumFacing.EAST;
        return EnumFacing.SOUTH;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return MapColor.WOOD;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.MIDDLE_POLE;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        super.onEntityCollision(world, pos, state, entity);
        entity.attackEntityFrom(DamageSource.GENERIC, Config.getThornBranchesDamage());
    }

    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        super.onEntityWalk(world, pos, entity);
        entity.attackEntityFrom(DamageSource.GENERIC, Config.getThornBranchesDamage());
    }

}
