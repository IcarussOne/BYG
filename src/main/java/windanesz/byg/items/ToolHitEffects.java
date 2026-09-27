package windanesz.byg.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

public final class ToolHitEffects {
    private ToolHitEffects() {
    }

    public static void applyKasai(EntityLivingBase entity) {
        entity.setFire(10);
    }

    public static void applyLatharium(EntityLivingBase entity) {
        if (entity.world instanceof WorldServer) {
            ((WorldServer) entity.world).spawnParticle(EnumParticleTypes.END_ROD, entity.posX, entity.posY, entity.posZ, 5, 3.0, 3.0, 3.0, 1.0, new int[0]);
        }
        entity.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 40, 2, false, false));
    }
}
