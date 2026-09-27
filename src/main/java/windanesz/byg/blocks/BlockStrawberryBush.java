package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.registry.ModItems;

import java.util.Random;

public class BlockStrawberryBush
        extends BlockBush {
    private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 2);


    public BlockStrawberryBush() {
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(null);
        this.setHardness(0.01f);
        this.setResistance(2.0f);
        this.setLightLevel(0.0f);
        this.setTranslationKey("strawberry_bush");
        this.setRegistryName("strawberry_bush");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
    }

    private static void harvestBerries(EntityPlayer entity, World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int berryCount = Config.getStrawberryBushHarvestYield();
        world.setBlockState(new BlockPos(x, y, z), world.getBlockState(pos).withProperty(STAGE, 1), 3);
        if (!world.isRemote && berryCount > 0) {
            EntityItem entityToSpawn = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(ModItems.strawberry, berryCount));
            entityToSpawn.setPickupDelay(10);
            world.spawnEntity((Entity) entityToSpawn);
        }
    }

    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock((Block) this), 1, this.damageDropped(state));
    }

    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        Block block2 = world.getBlockState(pos.down()).getBlock();
        return block2.canSustainPlant(world.getBlockState(pos.down()), (IBlockAccess) world, pos.down(), EnumFacing.UP, (IPlantable) this) || block2 == this;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, BlockPos pos, int pass) {
        return 0xFFFFFF;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STAGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, Math.min(2, Math.max(0, meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) {
            return;
        }
        int stage = state.getValue(STAGE);
        if (stage >= 2) {
            return;
        }
        double configuredGrowChance = Config.getPlantStageGrowthChance("strawberry_bush", 0.5D);
        if (configuredGrowChance >= 1.0D || random.nextDouble() < configuredGrowChance) {
            world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
        }
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = entity.getHeldItem(hand);
        int stage = state.getValue(STAGE);
        if (heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15 && stage < 2) {
            if (!world.isRemote) {
                double bonemealChance = Config.getPlantStageBonemealChance("strawberry_bush", 0.4D);
                if (bonemealChance >= 1.0D || world.rand.nextDouble() < bonemealChance) {
                    world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
                }
                if (!entity.capabilities.isCreativeMode) {
                    heldItem.shrink(1);
                }
            }
            return true;
        }
        if (stage == 2) {
            harvestBerries(entity, world, pos);
            return true;
        }
        return false;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int berryCount = Config.getStrawberryBushHarvestYield();
        if (state.getValue(STAGE) == 2 && berryCount > 0) {
            drops.add(new ItemStack(ModItems.strawberry, berryCount));
        }
    }

}
