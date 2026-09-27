package windanesz.byg.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.Config;
import windanesz.byg.registry.ModItems;

public class EntityMudBall extends EntityThrowable {
    public EntityMudBall(World world) {
        super(world);
    }

    public EntityMudBall(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public EntityMudBall(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public static void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMudBall.class, renderManager -> new RenderSnowball<>(renderManager, ModItems.mud_balls, Minecraft.getMinecraft().getRenderItem()));
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), Config.getMudBallDamage());
        }

        if (!world.isRemote) {
            world.setEntityState(this, (byte) 3);
            setDead();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            int particleItemId = Item.getIdFromItem(ModItems.mud_balls);
            for (int i = 0; i < 8; ++i) {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, 0.0, 0.0, 0.0, particleItemId);
            }
            return;
        }
        super.handleStatusUpdate(id);
    }
}
