package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import java.util.Random;

public class BlockWoodSlabBase extends Block {
    public static final PropertyEnum<SlabHalf> HALF = PropertyEnum.create("half", SlabHalf.class);
    public static final PropertyBool DOUBLE = PropertyBool.create("double");
    private static final AxisAlignedBB BOTTOM_SLAB_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
    private static final AxisAlignedBB TOP_SLAB_AABB = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);

    public BlockWoodSlabBase(String name, int harvestLevel) {
        super(Material.WOOD);
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(1.0f);
        this.setResistance(8.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, SlabHalf.BOTTOM).withProperty(DOUBLE, false));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{HALF, DOUBLE});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(HALF, (meta & 1) == 0 ? SlabHalf.BOTTOM : SlabHalf.TOP)
                .withProperty(DOUBLE, (meta & 2) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(HALF) == SlabHalf.TOP ? 1 : 0;
        if (state.getValue(DOUBLE)) {
            meta |= 2;
        }
        return meta;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                            int meta, net.minecraft.entity.EntityLivingBase placer, net.minecraft.util.EnumHand hand) {
        SlabHalf half = facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5F) ? SlabHalf.BOTTOM : SlabHalf.TOP;
        return this.getDefaultState().withProperty(HALF, half).withProperty(DOUBLE, false);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(DOUBLE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(DOUBLE)) {
            return FULL_BLOCK_AABB;
        }
        return state.getValue(HALF) == SlabHalf.TOP ? TOP_SLAB_AABB : BOTTOM_SLAB_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(DOUBLE);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return state.getValue(DOUBLE) ? 2 : 1;
    }

    public enum SlabHalf implements IStringSerializable {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        SlabHalf(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
