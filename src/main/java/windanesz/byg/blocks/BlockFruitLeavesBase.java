package windanesz.byg.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.List;
import java.util.function.Supplier;

public class BlockFruitLeavesBase extends BlockBygLeaves {
    private final Supplier<ItemStack> dropSupplier;

    public BlockFruitLeavesBase(String name, int harvestLevel, Supplier<ItemStack> dropSupplier) {
        super(name, harvestLevel, 0.0f, BlockRenderLayer.CUTOUT_MIPPED);
        this.dropSupplier = dropSupplier;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.create();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(this.dropSupplier.get());
    }
}

