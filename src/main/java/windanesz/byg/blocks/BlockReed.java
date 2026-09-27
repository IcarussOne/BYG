package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.registry.ModItems;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockReed
        extends Block {


    public BlockReed() {
        super(Material.PLANTS);
        this.setRegistryName("reed");
        this.setTranslationKey("reed");
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", 0);
        this.setHardness(0.0f);
        this.setResistance(0.0f);
        this.setLightLevel(0.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(null);
    }



    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isReplaceable(IBlockAccess blockAccess, BlockPos pos) {
        return true;
    }

    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.reeds, 1);
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    // Reeds have no block item of their own, so the silk touch drop must name the reeds item.
    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(ModItems.reeds, 1);
    }

    // Reeds only drop when harvested with silk touch.
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockReed block = this;
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        
        boolean foundWater = false;
        
        // Search a symmetric three-block radius for water.
        for (int dz = -3; dz <= 3; dz++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dx = -3; dx <= 3; dx++) {
                    if (world.getBlockState(pos.add(dx, dy, dz)).getBlock() == Blocks.WATER) {
                        foundWater = true;
                        break;
                    }
                }
                if (foundWater) break;
            }
            if (foundWater) break;
        }

        if (!foundWater) {
            world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, net.minecraft.init.SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.setBlockToAir(pos);
        }
        
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }
}


