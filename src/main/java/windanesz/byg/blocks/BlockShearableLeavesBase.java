package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class BlockShearableLeavesBase extends BlockBygLeaves {
    private final Supplier<Block> shearedDropSupplier;
    private final Supplier<ItemStack> naturalDropSupplier;
    private final double naturalDropChance;
    private final int airDropCount;
    private final boolean silkHarvest;

    public BlockShearableLeavesBase(String name, int harvestLevel, Supplier<Block> shearedDropSupplier,
                                    Supplier<ItemStack> naturalDropSupplier, double naturalDropChance, int airDropCount) {
        this(name, harvestLevel, shearedDropSupplier, naturalDropSupplier, naturalDropChance, airDropCount,
                0.0f, BlockRenderLayer.CUTOUT_MIPPED, true);
    }

    public BlockShearableLeavesBase(String name, int harvestLevel, Supplier<Block> shearedDropSupplier,
                                    Supplier<ItemStack> naturalDropSupplier, double naturalDropChance, int airDropCount,
                                    float lightLevel, BlockRenderLayer renderLayer, boolean silkHarvest) {
        super(name, harvestLevel, lightLevel, renderLayer);
        this.shearedDropSupplier = shearedDropSupplier;
        this.naturalDropSupplier = naturalDropSupplier;
        this.naturalDropChance = naturalDropChance;
        this.airDropCount = airDropCount;
        this.silkHarvest = silkHarvest;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this.shearedDropSupplier.get(), 1));
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.silkHarvest;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(net.minecraft.init.Blocks.AIR, this.airDropCount));
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer entity, boolean willHarvest) {
        boolean retval = super.removedByPlayer(state, world, pos, entity, willHarvest);
        if ((entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHeldItemMainhand() : ItemStack.EMPTY).getItem() == Items.SHEARS) {
            if (!world.isRemote) {
                EntityItem entityToSpawn = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.shearedDropSupplier.get(), 1));
                entityToSpawn.setPickupDelay(10);
                world.spawnEntity((Entity) entityToSpawn);
            }
        } else if (Math.random() < this.naturalDropChance && !world.isRemote) {
            EntityItem entityToSpawn = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.naturalDropSupplier.get());
            entityToSpawn.setPickupDelay(10);
            world.spawnEntity((Entity) entityToSpawn);
        }
        return retval;
    }
}
