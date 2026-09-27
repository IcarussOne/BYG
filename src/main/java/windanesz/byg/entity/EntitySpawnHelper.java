package windanesz.byg.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import windanesz.byg.BiomesYouGo;

import java.util.ArrayList;
import java.util.List;

public final class EntitySpawnHelper {

    private static final Logger LOGGER = LogManager.getLogger(BiomesYouGo.MODID);

    private EntitySpawnHelper() {
    }

    public static void addSpawnIfBiomesPresent(Class<? extends EntityLiving> entityClass, int weightedProb, int min, int max, EnumCreatureType creatureType, ResourceLocation... biomeIds) {
        List<Biome> spawnBiomes = new ArrayList<>();
        List<String> missingBiomes = new ArrayList<>();

        for (ResourceLocation biomeId : biomeIds) {
            Biome biome = Biome.REGISTRY.getObject(biomeId);
            if (biome != null) {
                spawnBiomes.add(biome);
            } else {
                missingBiomes.add(biomeId.toString());
            }
        }

        if (spawnBiomes.isEmpty()) {
            LOGGER.warn("Skipping spawn registration for {} because all configured biomes are unavailable: {}", entityClass.getSimpleName(), missingBiomes);
            return;
        }

        if (!missingBiomes.isEmpty()) {
            LOGGER.warn("Skipping unavailable spawn biomes for {}: {}", entityClass.getSimpleName(), missingBiomes);
        }

        EntityRegistry.addSpawn(entityClass, weightedProb, min, max, creatureType, spawnBiomes.toArray(new Biome[0]));
    }
}
