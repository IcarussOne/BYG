package windanesz.byg.client;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import windanesz.byg.BiomesYouGo;
import windanesz.byg.Config;
import windanesz.byg.registry.ModBiomes;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = BiomesYouGo.MODID, value = Side.CLIENT)
public final class BiomeFogHandler {
    private static final int SIDE_SAMPLE_DISTANCE = 8;
    private static final FogProfile[] PROFILES = FogProfile.values();
    private static final float[] currentWeights = new float[PROFILES.length];
    private static final float[] targetWeights = new float[PROFILES.length];
    private static World currentWorld;
    private static long lastUpdateNanos;
    private static long renderFrame;
    private static long sampledFrame = -1L;
    private static boolean fogActive;

    private BiomeFogHandler() {
    }

    private static FogProfile profileFor(Biome biome) {
        if (biome == ModBiomes.byg_bayou) return FogProfile.BAYOU;
        if (biome == ModBiomes.byg_cypress_swamplands) return FogProfile.CYPRESS;
        if (biome == ModBiomes.byg_mangrove_marshes) return FogProfile.MANGROVE;
        if (biome == ModBiomes.byg_bog) return FogProfile.BOG;
        if (biome == ModBiomes.byg_glowshroom_bayou) return FogProfile.GLOWSHROOM;
        if (biome == ModBiomes.byg_dead_sea) return FogProfile.DEAD_SEA;
        return null;
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        renderFrame++;
    }

    private static boolean updateFog(Entity entity) {
        if (!Config.clientSettings.enableBiomeFog) {
            resetFog();
            sampledFrame = -1L;
            return false;
        }
        if (entity == null || entity.world == null) return false;
        if (sampledFrame == renderFrame && currentWorld == entity.world) return fogActive;
        sampledFrame = renderFrame;

        // Keep vanilla underwater fog, but retain swamp mist when swimming at the surface.
        BlockPos eyePos = new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        if (entity.world.getBlockState(eyePos).getMaterial() == Material.WATER) {
            resetFog();
            currentWorld = entity.world;
            return false;
        }

        Arrays.fill(targetWeights, 0f);
        int x = eyePos.getX();
        int z = eyePos.getZ();
        double yaw = Math.toRadians(entity.rotationYaw);
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);
        addSample(entity.world, x, z, 0.5f, targetWeights);
        addForwardSample(entity.world, x, z, forwardX, forwardZ, 16, 0.1f, targetWeights);
        addForwardSample(entity.world, x, z, forwardX, forwardZ, 32, 0.1f, targetWeights);
        addForwardSample(entity.world, x, z, forwardX, forwardZ, 64, 0.25f, targetWeights);
        addSample(entity.world, x + (int) Math.round(-forwardZ * SIDE_SAMPLE_DISTANCE),
                z + (int) Math.round(forwardX * SIDE_SAMPLE_DISTANCE), 0.025f, targetWeights);
        addSample(entity.world, x + (int) Math.round(forwardZ * SIDE_SAMPLE_DISTANCE),
                z + (int) Math.round(-forwardX * SIDE_SAMPLE_DISTANCE), 0.025f, targetWeights);

        long now = System.nanoTime();
        if (currentWorld != entity.world || lastUpdateNanos == 0L) {
            currentWorld = entity.world;
            System.arraycopy(targetWeights, 0, currentWeights, 0, currentWeights.length);
        } else {
            float seconds = Math.min(0.1f, Math.max(0f, (now - lastUpdateNanos) / 1_000_000_000f));
            float blend = 1f - (float) Math.exp(-seconds / 0.35f);
            for (int i = 0; i < currentWeights.length; i++) {
                currentWeights[i] += (targetWeights[i] - currentWeights[i]) * blend;
            }
        }
        lastUpdateNanos = now;

        fogActive = false;
        for (float weight : currentWeights) {
            if (weight > 0.001f) fogActive = true;
        }
        return fogActive;
    }

    private static void addSample(World world, int x, int z, float weight, float[] targetWeights) {
        FogProfile profile = profileFor(world.getBiome(new BlockPos(x, 0, z)));
        if (profile != null) targetWeights[profile.ordinal()] += weight;
    }

    private static void addForwardSample(World world, int x, int z, double forwardX, double forwardZ,
                                         int distance, float weight, float[] targetWeights) {
        addSample(world, x + (int) Math.round(forwardX * distance),
                z + (int) Math.round(forwardZ * distance), weight, targetWeights);
    }

    private static void resetFog() {
        Arrays.fill(currentWeights, 0f);
        lastUpdateNanos = 0L;
        fogActive = false;
    }

    @SubscribeEvent
    public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        if (!updateFog(event.getEntity())) return;

        float red = event.getRed();
        float green = event.getGreen();
        float blue = event.getBlue();
        for (FogProfile profile : PROFILES) {
            float blend = currentWeights[profile.ordinal()] * profile.colorBlend;
            red += (profile.red - event.getRed()) * blend;
            green += (profile.green - event.getGreen()) * blend;
            blue += (profile.blue - event.getBlue()) * blend;
        }
        event.setRed(red);
        event.setGreen(green);
        event.setBlue(blue);
    }

    @SubscribeEvent
    public static void onRenderFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (!updateFog(event.getEntity())) return;

        float farPlane = event.getFarPlaneDistance();
        float fogStart = farPlane * 0.75f;
        float fogEnd = farPlane;
        for (FogProfile profile : PROFILES) {
            float weight = currentWeights[profile.ordinal()];
            float profileEnd = Math.max(32f, farPlane * profile.distanceFactor);
            fogStart += (profileEnd * 0.12f - farPlane * 0.75f) * weight;
            fogEnd += (profileEnd - farPlane) * weight;
        }
        GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
        GlStateManager.setFogStart(fogStart);
        GlStateManager.setFogEnd(fogEnd);
        GlStateManager.enableFog();
    }

    private enum FogProfile {
        BAYOU(0.43f, 0.48f, 0.43f, 0.50f, 0.42f),
        CYPRESS(0.40f, 0.46f, 0.40f, 0.46f, 0.47f),
        MANGROVE(0.46f, 0.49f, 0.43f, 0.43f, 0.50f),
        BOG(0.48f, 0.51f, 0.53f, 0.40f, 0.53f),
        GLOWSHROOM(0.43f, 0.47f, 0.58f, 0.38f, 0.56f),
        DEAD_SEA(0.39f, 0.41f, 0.43f, 0.33f, 0.61f);

        private final float red;
        private final float green;
        private final float blue;
        private final float colorBlend;
        private final float distanceFactor;

        FogProfile(float red, float green, float blue, float colorBlend, float distanceFactor) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.colorBlend = colorBlend;
            this.distanceFactor = distanceFactor;
        }
    }
}
