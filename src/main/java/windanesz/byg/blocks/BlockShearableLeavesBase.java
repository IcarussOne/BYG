package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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

public class BlockShearableLeavesBase extends Block {
    private final Supplier<Block> shearedDropSupplier;
    private final Supplier<ItemStack> naturalDropSupplier;
    private final double naturalDropChance;
    private final int airDropCount;
    private final BlockRenderLayer renderLayer;
    private final boolean silkHarvest;

    public BlockShearableLeavesBase(String name, int harvestLevel, Supplier<Block> shearedDropSupplier,
                                    Supplier<ItemStack> naturalDropSupplier, double naturalDropChance, int airDropCount) {
        this(name, harvestLevel, shearedDropSupplier, naturalDropSupplier, naturalDropChance, airDropCount,
                0.0f, BlockRenderLayer.CUTOUT_MIPPED, true);
    }

    public BlockShearableLeavesBase(String name, int harvestLevel, Supplier<Block> shearedDropSupplier,
                                    Supplier<ItemStack> naturalDropSupplier, double naturalDropChance, int airDropCount,
                                    float lightLevel, BlockRenderLayer renderLayer, boolean silkHarvest) {
        super(Material.LEAVES);
        this.shearedDropSupplier = shearedDropSupplier;
        this.naturalDropSupplier = naturalDropSupplier;
        this.naturalDropChance = naturalDropChance;
        this.airDropCount = airDropCount;
        this.renderLayer = renderLayer;
        this.silkHarvest = silkHarvest;
        this.setRegistryName(name);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", harvestLevel);
        this.setHardness(0.2f);
        this.setResistance(5.0f);
        this.setLightLevel(lightLevel);
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
        return this.renderLayer;
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
