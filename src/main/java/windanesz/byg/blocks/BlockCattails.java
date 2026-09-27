package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumParticleTypes;
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

public class BlockCattails
        extends Block {


    public BlockCattails() {
        super(Material.PLANTS);
        this.setRegistryName("cattails");
        this.setTranslationKey("cattails");
        this.setSoundType(SoundType.PLANT);
        this.setHarvestLevel("axe", 1);
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
        return new ItemStack(ModItems.cattail, 1);
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(ModItems.cattail, 1);
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (RANDOM.nextInt(10) == 0) {
            drops.add(new ItemStack(ModItems.cattail_rhizome, 1));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        for (int i = 0; i < 10; i++) {
            double x = pos.getX() + 0.25D + RANDOM.nextDouble() * 0.5D;
            double y = pos.getY() + 0.75D + RANDOM.nextDouble() * 1.45D;
            double z = pos.getZ() + 0.25D + RANDOM.nextDouble() * 0.5D;
            double motionX = (RANDOM.nextDouble() - 0.5D) * 0.025D;
            double motionY = 0.05D + RANDOM.nextDouble() * 0.06D;
            double motionZ = (RANDOM.nextDouble() - 0.5D) * 0.025D;
            Particle fluff = manager.spawnEffectParticle(EnumParticleTypes.CLOUD.getParticleID(), x, y, z,
                    motionX, motionY, motionZ);
            if (fluff != null) {
                fluff.setRBGColorF(0.72F, 0.58F, 0.28F);
            }
        }
        return false;
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockCattails block = this;
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        
        boolean foundWater = false;
        
        // Search 6x6x6 area for water
        for (int dz = -3; dz <= 2; dz++) {
            for (int dy = -3; dy <= 2; dy++) {
                for (int dx = -3; dx <= 2; dx++) {
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


