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

public class BlockRudostalk
        extends BlockBush {
    private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 4);


    public BlockRudostalk() {
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(null);
        this.setHardness(0.01f);
        this.setResistance(2.0f);
        this.setLightLevel(0.0f);
        this.setTranslationKey("rudo_stalk");
        this.setRegistryName("rudo_stalk");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
    }

    private static void harvestBeans(EntityPlayer entity, World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        EntityItem entityToSpawn;
        world.setBlockState(new BlockPos(x, y, z), world.getBlockState(pos).withProperty(STAGE, 3), 3);
        if (!world.isRemote) {
            entityToSpawn = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(ModItems.rudo_beans, 1));
            entityToSpawn.setPickupDelay(10);
            world.spawnEntity((Entity) entityToSpawn);
        }
        if (!world.isRemote) {
            entityToSpawn = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(ModItems.rudo_beans, 1));
            entityToSpawn.setPickupDelay(10);
            world.spawnEntity((Entity) entityToSpawn);
        }
        if (!world.isRemote) {
            entityToSpawn = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(ModItems.rudo_beans, 1));
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
        return this.getDefaultState().withProperty(STAGE, Math.min(4, Math.max(0, meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    private static double getGrowthChance(int stage) {
        return stage == 0 ? 1.0D : 0.5D;
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) {
            return;
        }
        int stage = state.getValue(STAGE);
        if (stage >= 4) {
            return;
        }
        if (getGrowthChance(stage) >= 1.0D || random.nextDouble() < getGrowthChance(stage)) {
            world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
        }
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = entity.getHeldItem(hand);
        int stage = state.getValue(STAGE);
        if (heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15 && stage < 4) {
            if (!world.isRemote) {
                double bonemealChance = Config.getPlantStageBonemealChance("rudo_stalk", 0.4D);
                if (bonemealChance >= 1.0D || world.rand.nextDouble() < bonemealChance) {
                    world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
                }
                if (!entity.capabilities.isCreativeMode) {
                    heldItem.shrink(1);
                }
            }
            return true;
        }
        if (stage == 4) {
            harvestBeans(entity, world, pos);
            return true;
        }
        return false;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int stage = state.getValue(STAGE);
        if (stage == 4) {
            drops.add(new ItemStack(ModItems.rudo_beans, 3));
        } else if (stage == 0) {
            drops.add(new ItemStack(ModItems.rudo_beans, 1));
        }
    }

}
