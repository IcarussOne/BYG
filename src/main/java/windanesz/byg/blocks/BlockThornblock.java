package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

public class BlockThornblock
        extends Block {
    public static final PropertyDirection FACING = BlockDirectional.FACING;


    public BlockThornblock() {
        super(Material.WOOD);
        this.setRegistryName("thorn_block");
        this.setTranslationKey("thorn_block");
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 1);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.SOUTH));
    }

    public int tickRate(World world) {
        return 2600;
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
        facing = facing == EnumFacing.WEST || facing == EnumFacing.EAST ? EnumFacing.UP : (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? EnumFacing.EAST : EnumFacing.SOUTH);
        return this.getDefaultState().withProperty(FACING, facing);
    }

    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos pos) {
        return MapColor.WOOD;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.CENTER;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        super.onEntityCollision(world, pos, state, entity);
        entity.attackEntityFrom(DamageSource.GENERIC, Config.getThornblockDamage());
    }

    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        super.onEntityWalk(world, pos, entity);
        entity.attackEntityFrom(DamageSource.GENERIC, Config.getThornblockDamage());
    }

}

