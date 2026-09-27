package windanesz.byg.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlockConfiguredFlower extends BlockSimpleFlowerBase {
    private final boolean flammable;
    private final boolean replaceable;
    @Nullable
    private final Supplier<ItemStack> dropSupplier;
    @Nullable
    private final Supplier<ItemStack> pickBlockSupplier;

    public BlockConfiguredFlower(String name, @Nullable CreativeTabs tab, boolean flammable, boolean replaceable,
                                 @Nullable Supplier<ItemStack> dropSupplier, @Nullable Supplier<ItemStack> pickBlockSupplier) {
        super(name, tab, 0.0F);
        this.flammable = flammable;
        this.replaceable = replaceable;
        this.dropSupplier = dropSupplier;
        this.pickBlockSupplier = pickBlockSupplier;
    }

    @Override
    public boolean isReplaceable(IBlockAccess blockAccess, BlockPos pos) {
        return replaceable || super.isReplaceable(blockAccess, pos);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return pickBlockSupplier != null ? pickBlockSupplier.get() : super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (dropSupplier == null) {
            super.getDrops(drops, world, pos, state, fortune);
            return;
        }
        drops.add(dropSupplier.get());
    }

    @Override
    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, net.minecraft.util.EnumFacing face) {
        return flammable;
    }
}
