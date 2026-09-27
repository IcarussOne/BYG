package windanesz.byg.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import windanesz.byg.Config;

import java.util.Random;

public class BlockSpringwater extends BlockFluidClassic {
    private static final int REGENERATION_DURATION = 100;
    private static final int REGENERATION_REFRESH_THRESHOLD = 49;
    @GameRegistry.ObjectHolder("byg:springwater")
    public static final Item item = null;
    private static final Fluid FLUID = new Fluid("springwater", new ResourceLocation("byg:blocks/spring_water_flowing"), new ResourceLocation("byg:blocks/spring_water_stills")).setLuminosity(10).setDensity(1000).setViscosity(1000).setGaseous(false);


    public BlockSpringwater() {
        super(FLUID, Material.WATER);
        this.setTranslationKey("springwater");
        this.setRegistryName("springwater");
    }

    public static void preInit(FMLPreInitializationEvent event) {
        FluidRegistry.registerFluid(FLUID);
        FluidRegistry.addBucketForFluid(FLUID);
    }



    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        int bubbleParticleCount = Config.getSpringwaterBubbleParticleCount();
        if (bubbleParticleCount > 0 && random.nextDouble() < Config.getSpringwaterBubbleChance()
                && world instanceof WorldServer && world.getBlockState(pos).getBlock() == this) {
            for (int i = 0; i < bubbleParticleCount; i++) {
                double x = pos.getX() + 0.2D + random.nextDouble() * 0.6D;
                double y = pos.getY() + 0.1D + random.nextDouble() * 0.3D;
                double z = pos.getZ() + 0.2D + random.nextDouble() * 0.6D;
                ((WorldServer) world).spawnParticle(EnumParticleTypes.WATER_BUBBLE, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.02D);
            }
        }
        world.scheduleUpdate(pos, (Block) this, this.tickRate(world));
    }

    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        super.onEntityCollision(world, pos, state, entity);
        if (!world.isRemote && Config.doesSpringwaterApplyRegeneration() && entity instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) entity;
            PotionEffect active = living.getActivePotionEffect(MobEffects.REGENERATION);
            if (active == null) {
                living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, REGENERATION_DURATION, 0, false, false));
            } else if (active.getAmplifier() == 0 && active.getDuration() <= REGENERATION_REFRESH_THRESHOLD) {
                // Avoid resetting to a multiple of 50, which would trigger a heal on every refresh.
                living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, REGENERATION_DURATION - 1, 0, false, false));
            }
        }
    }
}
