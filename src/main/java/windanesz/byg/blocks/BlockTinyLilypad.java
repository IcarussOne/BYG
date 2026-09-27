package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.client.BYGTab;

import java.util.Random;

public class BlockTinyLilypad extends Block {

    public BlockTinyLilypad() {
        super(Material.CORAL);
        this.setRegistryName("tiny_lilypad");
        this.setTranslationKey("tiny_lilypad");
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", 0);
        this.setHardness(0.01f);
        this.setResistance(1.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(BYGTab.tab);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1, 1.0);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFlammable(IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
        return true;
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        if (world.getBlockState(pos.down()).getBlock() != Blocks.WATER) {
            state.getBlock().dropBlockAsItem(world, pos, state, 1);
            world.setBlockToAir(pos);
        }
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }
}


