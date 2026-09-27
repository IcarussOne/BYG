package windanesz.byg.event;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.Config;
import windanesz.byg.biome.BiomeWeepingWitchForest;

@Mod.EventBusSubscriber(modid = BiomesYouGo.MODID)
public final class WeepingWitchSpawnHandler {
    private WeepingWitchSpawnHandler() {
    }

    @SubscribeEvent
    public static void allowDaytimeWitches(LivingSpawnEvent.CheckSpawn event) {
        if (!Config.allowDaytimeWitchesInWeepingWitchForest()
                || event.isSpawner() || !(event.getEntityLiving() instanceof EntityWitch)
                || event.getWorld().isRemote || event.getWorld().getDifficulty() == EnumDifficulty.PEACEFUL
                || !event.getWorld().isDaytime()) {
            return;
        }
        BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
        if (event.getWorld().getBiome(pos) instanceof BiomeWeepingWitchForest
                && event.getWorld().canSeeSky(pos)) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
