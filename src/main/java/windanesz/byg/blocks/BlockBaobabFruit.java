package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBlocks;
import windanesz.byg.registry.ModItems;

import java.util.Random;

public class BlockBaobabFruit extends Block {
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 4);

    public BlockBaobabFruit() {
        super(Material.PLANTS);
        this.setRegistryName("baobab_fruit_block");
        this.setTranslationKey("baobab_fruit_block");
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        this.setHardness(1.0f);
        this.setResistance(5.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(null);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 4));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public int tickRate(World world) {
        return Config.getBaobabFruitStageTickRate();
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

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    private static void spawnFruitDrops(World world, BlockPos pos, int count) {
        if (world.isRemote || count <= 0) {
            return;
        }
        EntityItem entityToSpawn = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.baobab_fruit, count));
        entityToSpawn.setPickupDelay(10);
        world.spawnEntity(entityToSpawn);
    }

    private static int breakDropCountForStage(int stage) {
        if (stage >= 4) {
            return Config.getBaobabFruitBreakDropCount();
        }
        if (stage == 3) {
            return 2;
        }
        if (stage == 2) {
            return 1;
        }
        return 0;
    }

    private static int harvestDropCountForStage(int stage) {
        if (stage >= 4) {
            return Config.getBaobabFruitHarvestCount();
        }
        if (stage == 3 || stage == 2) {
            return 1;
        }
        return 0;
    }

    private static int regressedStage(int stage) {
        if (stage >= 4) {
            return 3;
        }
        if (stage == 3) {
            return 2;
        }
        if (stage == 2) {
            return 1;
        }
        return stage;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int count = breakDropCountForStage(state.getValue(STAGE));
        if (count > 0) {
            drops.add(new ItemStack(ModItems.baobab_fruit, count));
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            int stage = state.getValue(STAGE);
            if (stage < 4) {
                double configuredGrowChance = Config.getPlantStageGrowthChance("baobab_fruit_block", 0.3D);
                if (configuredGrowChance >= 1.0D || random.nextDouble() < configuredGrowChance) {
                    world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
                }
            }
            world.scheduleUpdate(pos, this, this.tickRate(world));
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, neighborBlock, fromPos);
        if (world.getBlockState(pos.up()).getBlock() != ModBlocks.baobab_leaves) {
            int count = breakDropCountForStage(state.getValue(STAGE));
            world.setBlockToAir(pos);
            spawnFruitDrops(world, pos, count);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entity, EnumHand hand, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        ItemStack heldItem = entity.getHeldItem(hand);
        int stage = state.getValue(STAGE);
        if (heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15 && stage < 4) {
            if (!world.isRemote) {
                double bonemealChance = Config.getPlantStageBonemealChance("baobab_fruit_block", 0.4D);
                if (bonemealChance >= 1.0D || world.rand.nextDouble() < bonemealChance) {
                    world.setBlockState(pos, state.withProperty(STAGE, stage + 1), 3);
                }
                if (!entity.capabilities.isCreativeMode) {
                    heldItem.shrink(1);
                }
            }
            return true;
        }
        if (stage >= 2) {
            if (!world.isRemote) {
                world.setBlockState(pos, state.withProperty(STAGE, regressedStage(stage)), 3);
                spawnFruitDrops(world, pos, harvestDropCountForStage(stage));
            }
            return true;
        }
        return false;
    }
}
