package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.registry.ModItems;

import java.util.Random;

public class BlockBlueberryBush
        extends BlockFlower {
    private static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 2);


    public BlockBlueberryBush() {
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(null);
        this.setHardness(0.01f);
        this.setResistance(2.0f);
        this.setLightLevel(0.0f);
        this.setTranslationKey("blueberry_bush");
        this.setRegistryName("blueberry_bush");
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(this.getTypeProperty(), BlockFlower.EnumFlowerType.DANDELION)
                .withProperty(STAGE, 0));
    }

    private static void harvestBerries(EntityPlayer entity, World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int berryCount = Config.getBlueberryBushHarvestYield();
        world.setBlockState(new BlockPos(x, y, z), world.getBlockState(pos).withProperty(STAGE, 1), 3);
        if (!world.isRemote && berryCount > 0) {
            EntityItem entityToSpawn = new EntityItem(world, (double) x, (double) y, (double) z, new ItemStack(ModItems.blueberry, berryCount));
            entityToSpawn.setPickupDelay(10);
            world.spawnEntity((Entity) entityToSpawn);
        }
    }

    public BlockFlower.EnumFlowerColor getBlockType() {
        return BlockFlower.EnumFlowerColor.YELLOW;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (BlockFlower.EnumFlowerType blockflower$enumflowertype : BlockFlower.EnumFlowerType.getTypes((BlockFlower.EnumFlowerColor) this.getBlockType())) {
            list.add(new ItemStack((Block) this, 1, blockflower$enumflowertype.getMeta()));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, this.getTypeProperty(), STAGE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, Math.min(2, Math.max(0, meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote) {
            return;
        }
        int stage = state.getValue(STAGE);
        if (stage >= 2) {
            return;
        }
        double configuredGrowChance = Config.getPlantStageGrowthChance("blueberry_bush", 0.5D);
        if (configuredGrowChance >= 1.0D || random.nextDouble() < configuredGrowChance) {
            world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
        }
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = entity.getHeldItem(hand);
        int stage = state.getValue(STAGE);
        if (heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15 && stage < 2) {
            if (!world.isRemote) {
                double bonemealChance = Config.getPlantStageBonemealChance("blueberry_bush", 0.4D);
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
        int berryCount = Config.getBlueberryBushHarvestYield();
        if (state.getValue(STAGE) == 2 && berryCount > 0) {
            drops.add(new ItemStack(ModItems.blueberry, berryCount));
        }
    }

}
