package windanesz.byg.client;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import windanesz.byg.entity.EntityCrystalCrawler;

import java.util.Map;
import java.util.WeakHashMap;

// Crystal crawler stuff
// Uses the same model as a spider, but with rotations when climbing walls and ceilings
@SideOnly(Side.CLIENT)
public class RenderCrystalCrawler extends RenderLiving<EntityCrystalCrawler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("byg:textures/crystal_crawler.png");

    private final Map<EntityCrystalCrawler, CrawlerSurfaceTransition> surfaceTransitions = new WeakHashMap<>();

    public RenderCrystalCrawler(RenderManager renderManager) {
        super(renderManager, new ModelSpider(), 0.35F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCrystalCrawler entity) {
        return TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityCrystalCrawler entity, float partialTickTime) {
        GlStateManager.scale(0.7F, 0.7F, 0.7F);
    }

    @Override
    protected float getDeathMaxRotation(EntityCrystalCrawler entity) {
        return 180.0F;
    }

    @Override
    protected void applyRotations(EntityCrystalCrawler entity, float ageInTicks, float rotationYaw, float partialTicks) {
        if (entity.deathTime > 0) {
            super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
            return;
        }

        EnumFacing facing = entity.getClimbFacing();
        CrawlerSurfaceTransition transition = this.surfaceTransitions.computeIfAbsent(entity, key -> new CrawlerSurfaceTransition());
        CrawlerSurfaceTransition.Pose pose = transition.sample(facing, entity.width, entity.height, entity.ticksExisted + partialTicks);
        GlStateManager.translate(pose.offsetX, pose.offsetY, pose.offsetZ);
        float axisLength = (float) Math.sqrt(pose.x * pose.x + pose.y * pose.y + pose.z * pose.z);
        // skip rotation if the axis is too small
        if (axisLength > 1.0E-6F) {
            float angle = (float) Math.toDegrees(2.0D * Math.atan2(axisLength, pose.w));
            GlStateManager.rotate(angle, pose.x / axisLength, pose.y / axisLength, pose.z / axisLength);
        }
        if (facing == EnumFacing.UP) {
            super.applyRotations(entity, ageInTicks, rotationYaw, partialTicks);
        } else {
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        }
    }
}
