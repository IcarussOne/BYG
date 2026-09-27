package windanesz.byg.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import windanesz.byg.entity.EntityKiwiBird;

public class ModelKiwi extends ModelBase {
    ModelRenderer Body;
    ModelRenderer Head;
    ModelRenderer Beak;
    ModelRenderer legpart1;
    ModelRenderer legpart2;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer rightfoot;
    ModelRenderer leftfoot;
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;

    public ModelKiwi() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.Body = new ModelRenderer((ModelBase) this, 27, 0);
        this.Body.addBox(0.0f, 0.0f, 0.0f, 9, 9, 9);
        this.Body.setRotationPoint(-4.0f, 9.0f, -4.0f);
        this.Body.setTextureSize(64, 32);
        this.Body.mirror = true;
        this.setRotation(this.Body, -0.2082002f, 0.0f, 0.0f);
        this.Head = new ModelRenderer((ModelBase) this, 29, 19);
        this.Head.addBox(-3.0f, 0.0f, -7.0f, 6, 6, 7);
        this.Head.setRotationPoint(0.5f, 10.0f, -4.0f);
        this.Head.setTextureSize(64, 32);
        this.Head.mirror = true;
        this.setRotation(this.Head, 0.2974289f, 0.0f, 0.0f);
        this.Beak = new ModelRenderer((ModelBase) this, 10, 22);
        this.Beak.addBox(0.0f, 0.0f, 0.0f, 1, 1, 7);
        this.Beak.setRotationPoint(-0.5f, 7.0f, -11.0f);
        this.Beak.setTextureSize(64, 32);
        this.Beak.mirror = true;
        this.setRotation(this.Beak, 0.4461433f, 0.0f, 0.0f);
        this.Head.addChild(this.Beak);
        this.legpart1 = new ModelRenderer((ModelBase) this, 11, 0);
        this.legpart1.addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
        this.legpart1.setRotationPoint(3.0f, 19.0f, 0.0f);
        this.legpart1.setTextureSize(64, 32);
        this.legpart1.mirror = true;
        this.setRotation(this.legpart1, 0.0f, 0.0f, 0.0f);
        this.legpart2 = new ModelRenderer((ModelBase) this, 12, 0);
        this.legpart2.addBox(0.0f, 0.0f, 0.0f, 2, 1, 2);
        this.legpart2.setRotationPoint(-4.0f, 19.0f, 0.0f);
        this.legpart2.setTextureSize(64, 32);
        this.legpart2.mirror = true;
        this.setRotation(this.legpart2, 0.0f, 0.0f, 0.0f);
        this.rightleg = new ModelRenderer((ModelBase) this, 0, 9);
        this.rightleg.addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
        this.rightleg.setRotationPoint(-3.5f, 20.0f, 0.5f);
        this.rightleg.setTextureSize(64, 32);
        this.rightleg.mirror = true;
        this.setRotation(this.rightleg, 0.0f, 0.0f, 0.0f);
        this.leftleg = new ModelRenderer((ModelBase) this, 0, 9);
        this.leftleg.addBox(0.0f, 0.0f, 0.0f, 1, 4, 1);
        this.leftleg.setRotationPoint(3.5f, 20.0f, 0.5f);
        this.leftleg.setTextureSize(64, 32);
        this.leftleg.mirror = true;
        this.setRotation(this.leftleg, 0.0f, 0.0f, 0.0f);
        this.rightfoot = new ModelRenderer((ModelBase) this, 0, 9);
        this.rightfoot.addBox(-1.0f, 0.0f, -1.0f, 3, 0, 3);
        this.rightfoot.setRotationPoint(-3.0f, 24.0f, 1.0f);
        this.rightfoot.setTextureSize(64, 32);
        this.rightfoot.mirror = true;
        this.setRotation(this.rightfoot, 0.0f, 0.0f, 0.0f);
        this.leftfoot = new ModelRenderer((ModelBase) this, 0, 9);
        this.leftfoot.addBox(-1.0f, 0.0f, -1.0f, 3, 0, 3);
        this.leftfoot.setRotationPoint(4.0f, 24.0f, 1.0f);
        this.leftfoot.setTextureSize(64, 32);
        this.leftfoot.mirror = true;
        this.setRotation(this.leftfoot, 0.0f, 0.0f, 0.0f);
        this.Shape1 = new ModelRenderer((ModelBase) this, 0, 0);
        this.Shape1.addBox(0.0f, 0.0f, 0.0f, 0, 1, 1);
        this.Shape1.setRotationPoint(0.0f, 9.0f, 0.0f);
        this.Shape1.setTextureSize(64, 32);
        this.Shape1.mirror = true;
        this.setRotation(this.Shape1, 0.0f, 0.0f, 0.0f);
        this.Shape2 = new ModelRenderer((ModelBase) this, 0, 0);
        this.Shape2.addBox(0.0f, 0.0f, 0.0f, 0, 1, 1);
        this.Shape2.setRotationPoint(0.0f, 12.0f, 4.0f);
        this.Shape2.setTextureSize(64, 32);
        this.Shape2.mirror = true;
        this.setRotation(this.Shape2, 0.0f, 0.0f, 0.0f);
        this.Shape3 = new ModelRenderer((ModelBase) this, 0, 0);
        this.Shape3.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
        this.Shape3.setRotationPoint(5.0f, 11.0f, -1.0f);
        this.Shape3.setTextureSize(64, 32);
        this.Shape3.mirror = true;
        this.setRotation(this.Shape3, 0.0f, 0.0f, 0.0f);
        this.Shape4 = new ModelRenderer((ModelBase) this, 0, 0);
        this.Shape4.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
        this.Shape4.setRotationPoint(-5.0f, 11.0f, -1.0f);
        this.Shape4.setTextureSize(64, 32);
        this.Shape4.mirror = true;
        this.setRotation(this.Shape4, 0.0f, 0.0f, 0.0f);
        this.Shape5 = new ModelRenderer((ModelBase) this, 0, 0);
        this.Shape5.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
        this.Shape5.setRotationPoint(5.0f, 17.0f, 3.0f);
        this.Shape5.setTextureSize(64, 32);
        this.Shape5.mirror = true;
        this.setRotation(this.Shape5, 0.0f, 0.0f, 0.0f);
        this.Shape6 = new ModelRenderer((ModelBase) this, 0, 0);
        this.Shape6.addBox(0.0f, 0.0f, 0.0f, 1, 1, 0);
        this.Shape6.setRotationPoint(0.0f, 19.0f, 0.0f);
        this.Shape6.setTextureSize(64, 32);
        this.Shape6.mirror = true;
        this.setRotation(this.Shape6, 0.0f, 0.0f, 0.0f);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.Body.render(f5);
        this.Head.render(f5);
        this.legpart1.render(f5);
        this.legpart2.render(f5);
        this.rightleg.render(f5);
        this.leftleg.render(f5);
        this.rightfoot.render(f5);
        this.leftfoot.render(f5);
        this.Shape1.render(f5);
        this.Shape2.render(f5);
        this.Shape3.render(f5);
        this.Shape4.render(f5);
        this.Shape5.render(f5);
        this.Shape6.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @SuppressWarnings("unchecked")
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        if (entity instanceof EntityKiwiBird) {
            EntityKiwiBird kiwi = (EntityKiwiBird) entity;

            if (kiwi.isSleeping()) {
                float breathe = MathHelper.sin(ageInTicks * 0.05f) * 0.04f;
                this.Body.rotateAngleX = 0.45f + breathe;
                this.Head.rotateAngleX = 1.35f + breathe;
                this.Head.rotateAngleY = 0.0f;
                this.legpart1.rotateAngleX = 0.5f;
                this.legpart2.rotateAngleX = 0.5f;
                this.rightleg.rotateAngleX = 0.4f;
                this.leftleg.rotateAngleX = 0.4f;
                this.Body.rotateAngleZ = 0.0f;
                return;
            }

            if (kiwi.isForaging()) {
                this.Body.rotateAngleX = -0.2082002f;
                this.Head.rotateAngleX = 0.8f + MathHelper.sin(ageInTicks * 0.35f) * 0.55f;
                this.Head.rotateAngleY = 0.0f;
                this.legpart1.rotateAngleX = 0.0f;
                this.legpart2.rotateAngleX = 0.0f;
                this.rightleg.rotateAngleX = 0.0f;
                this.leftleg.rotateAngleX = 0.0f;
                this.Body.rotateAngleZ = 0.0f;
                return;
            }
        }

        float swing = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        this.legpart1.rotateAngleX = swing;
        this.legpart2.rotateAngleX = -swing;
        this.rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + (float) Math.PI * 0.25f) * 1.4f * limbSwingAmount;
        this.leftleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + (float) Math.PI * 1.25f) * 1.4f * limbSwingAmount;
        this.Head.rotateAngleY = netHeadYaw * (float) Math.PI / 180f;
        this.Head.rotateAngleX = 0.2974289f + headPitch * (float) Math.PI / 180f;
        this.Body.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662f) * 0.15f * limbSwingAmount;
    }
}
