package windanesz.byg.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import windanesz.byg.biome.BiomeStonePillarSavanna;
import windanesz.byg.worldgen.QuagmireCaveGenerator;
import windanesz.byg.worldgen.QuagmireRavineGenerator;

public final class TerrainGenEventHandler {

    @SubscribeEvent
    public void onInitMapGen(InitMapGenEvent event) {
        if (event.getType() == InitMapGenEvent.EventType.CAVE) {
            event.setNewGen(new QuagmireCaveGenerator());
        } else if (event.getType() == InitMapGenEvent.EventType.RAVINE) {
            event.setNewGen(new QuagmireRavineGenerator());
        }
    }

    @SubscribeEvent
    public void onChunkPopulate(PopulateChunkEvent.Populate event) {
        if (event.getType() != PopulateChunkEvent.Populate.EventType.LAKE) {
            return;
        }
        BlockPos center = new BlockPos(event.getChunkX() * 16 + 8, 64, event.getChunkZ() * 16 + 8);
        Biome biome = event.getWorld().getBiome(center);
        if (biome instanceof BiomeStonePillarSavanna) {
            event.setResult(Event.Result.DENY);
        }
    }
}
