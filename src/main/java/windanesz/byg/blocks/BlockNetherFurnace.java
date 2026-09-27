package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import windanesz.byg.Config;
import windanesz.byg.client.BYGTab;

import java.util.Random;

public class BlockNetherFurnace extends Block {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    public BlockNetherFurnace() {
        super(Material.ROCK);
        this.setRegistryName("nether_furnace");
        this.setTranslationKey("nether_furnace");
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(5.0f);
        this.setResistance(14.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(2);
        this.setCreativeTab(BYGTab.tab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    private static void onTick(World world, BlockPos pos) {
        if (!Config.doesNetherFurnaceIgniteFromFireBelow()) {
            return;
        }
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.FIRE.getDefaultState().getBlock()) {
            world.playSound((EntityPlayer) null, (double) x, (double) y, (double) z, net.minecraft.init.SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.NEUTRAL, 3.0f, 1.0f);
            world.setBlockToAir(new BlockPos(x, y, z));
            world.setBlockState(new BlockPos(x, y, z), windanesz.byg.registry.ModBlocks.nether_furnace_lit.getDefaultState(), 3);
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex((int) meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        }
        return this.getDefaultState().withProperty(FACING, facing);
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public int tickRate(World world) {
        return Config.getNetherFurnaceTickRate();
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        onTick(world, pos);
        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.sendStatusMessage(new TextComponentTranslation("tooltip.byg.nether_furnace.unlit"), true);
        }
        return true;
    }

}


