package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import windanesz.byg.client.BYGTab;

import java.util.Random;

public class BlockRockyGrassAlps
        extends Block {


    public BlockRockyGrassAlps() {
        super(Material.ROCK);
        this.setRegistryName("rocky_grass_alps");
        this.setTranslationKey("rocky_grass_alps");
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(255);
        this.setCreativeTab(BYGTab.tab);
    }



    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(windanesz.byg.registry.ModBlocks.rocky_grass, 1);
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return true;
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(Blocks.COBBLESTONE, 1));
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockRockyGrassAlps block = this;
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        if (world.getBlockState(pos.up()).getBlock() == windanesz.byg.registry.ModBlocks.rocky_grass_alps) {
            world.setBlockToAir(pos);
            world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3);
        }
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }
}
