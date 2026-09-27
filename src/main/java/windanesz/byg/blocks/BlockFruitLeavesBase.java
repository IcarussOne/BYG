package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import java.util.Random;
import java.util.function.Supplier;

public class BlockFruitLeavesBase extends Block {
    private final Supplier<ItemStack> dropSupplier;

    public BlockFruitLeavesBase(String name, int harvestLevel, Supplier<ItemStack> dropSupplier) {
        super(Material.LEAVES);
        this.dropSupplier = dropSupplier;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(0.2f);
        this.setResistance(5.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(1);
        this.setCreativeTab(BYGTab.tab);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LeafDecay.CHECK_DECAY, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LeafDecay.CHECK_DECAY);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LeafDecay.CHECK_DECAY, (meta & 1) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LeafDecay.CHECK_DECAY) ? 1 : 0;
    }

    @Override
    public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {
        LeafDecay.beginLeavesDecay(world, pos, state);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        LeafDecay.breakBlock(world, pos);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        LeafDecay.updateTick(this, world, pos, state, random);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(this.dropSupplier.get());
    }
}

