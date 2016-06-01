package jp.plusplus.fbs.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by pluslus_F on 2015/06/25.
 */
public class ModelButterfly  extends ModelBase {
    //fields
    ModelRenderer head;
    ModelRenderer belly;
    ModelRenderer wingL0;
    ModelRenderer wingL1;
    ModelRenderer wingL2;
    ModelRenderer wingL3;
    ModelRenderer wingL4;
    ModelRenderer wingL11;
    ModelRenderer wingL12;
    ModelRenderer wingL13;
    ModelRenderer wingL14;
    ModelRenderer wingR3;
    ModelRenderer wingR0;
    ModelRenderer wingR1;
    ModelRenderer wingR2;
    ModelRenderer wingR11;
    ModelRenderer wingR12;
    ModelRenderer wingR13;
    ModelRenderer wingR14;
    ModelRenderer wingL15;
    ModelRenderer wingR15;
    ModelRenderer wingR4;

    public ModelButterfly() {
        textureWidth = 64;
        textureHeight = 32;

        head = new ModelRenderer(this, 0, 0);
        head.addBox(0F, 0F, -1F, 1, 1, 3);
        head.setRotationPoint(0F, 0F, 0F);
        head.setTextureSize(64, 32);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);

        belly = new ModelRenderer(this, 0, 4);
        belly.addBox(0F, 0F, 2F, 1, 1, 5);
        belly.setRotationPoint(0F, 0F, 0F);
        belly.setTextureSize(64, 32);
        belly.mirror = true;
        setRotation(belly, 0F, 0F, 0F);

        wingL0 = new ModelRenderer(this, 27, 2);
        wingL0.addBox(3F, 0F, -2F, 6, 0, 1);
        wingL0.setRotationPoint(0F, 0F, 0F);
        wingL0.setTextureSize(64, 32);
        wingL0.mirror = true;
        setRotation(wingL0, 0F, 0F, 0F);

        wingL1 = new ModelRenderer(this, 28, 3);
        wingL1.addBox(2F, 0F, -1F, 6, 0, 1);
        wingL1.setRotationPoint(0F, 0F, 0F);
        wingL1.setTextureSize(64, 32);
        wingL1.mirror = true;
        setRotation(wingL1, 0F, 0F, 0F);

        wingL2 = new ModelRenderer(this, 26, 4);
        wingL2.addBox(1F, 0F, 0F, 7, 0, 2);
        wingL2.setRotationPoint(0F, 0F, 0F);
        wingL2.setTextureSize(64, 32);
        wingL2.mirror = true;
        setRotation(wingL2, 0F, 0F, 0F);

        wingL3 = new ModelRenderer(this, 29, 1);
        wingL3.addBox(4F, 0F, -3F, 4, 0, 1);
        wingL3.setRotationPoint(0F, 0F, 0F);
        wingL3.setTextureSize(64, 32);
        wingL3.mirror = true;
        setRotation(wingL3, 0F, 0F, 0F);

        wingL4 = new ModelRenderer(this, 31, 0);
        wingL4.addBox(5F, 0F, -4F, 2, 0, 1);
        wingL4.setRotationPoint(0F, 0F, 0F);
        wingL4.setTextureSize(64, 32);
        wingL4.mirror = true;
        setRotation(wingL4, 0F, 0F, 0F);

        wingL11 = new ModelRenderer(this, 31, 6);
        wingL11.addBox(1F, 0F, 2F, 5, 0, 1);
        wingL11.setRotationPoint(0F, 0F, 0F);
        wingL11.setTextureSize(64, 32);
        wingL11.mirror = true;
        setRotation(wingL11, 0F, 0F, 0F);

        wingL12 = new ModelRenderer(this, 30, 7);
        wingL12.addBox(2F, 0F, 3F, 5, 0, 1);
        wingL12.setRotationPoint(0F, 0F, 0F);
        wingL12.setTextureSize(64, 32);
        wingL12.mirror = true;
        setRotation(wingL12, 0F, 0F, 0F);

        wingL13 = new ModelRenderer(this, 29, 8);
        wingL13.addBox(3F, 0F, 4F, 5, 0, 2);
        wingL13.setRotationPoint(0F, 0F, 0F);
        wingL13.setTextureSize(64, 32);
        wingL13.mirror = true;
        setRotation(wingL13, 0F, 0F, 0F);

        wingL14 = new ModelRenderer(this, 30, 10);
        wingL14.addBox(3F, 0F, 6F, 4, 0, 2);
        wingL14.setRotationPoint(0F, 0F, 0F);
        wingL14.setTextureSize(64, 32);
        wingL14.mirror = true;
        setRotation(wingL14, 0F, 0F, 0F);

        wingR3 = new ModelRenderer(this, 29, 1);
        wingR3.addBox(-7F, 0F, -3F, 4, 0, 1);
        wingR3.setRotationPoint(0F, 0F, 0F);
        wingR3.setTextureSize(64, 32);
        wingR3.mirror = true;
        setRotation(wingR3, 0F, 0F, 0F);

        wingR0 = new ModelRenderer(this, 27, 2);
        wingR0.addBox(-8F, 0F, -2F, 6, 0, 1);
        wingR0.setRotationPoint(0F, 0F, 0F);
        wingR0.setTextureSize(64, 32);
        wingR0.mirror = true;
        setRotation(wingR0, 0F, 0F, 0F);
        wingR0.mirror = false;

        wingR1 = new ModelRenderer(this, 28, 3);
        wingR1.addBox(-7F, 0F, -1F, 6, 0, 1);
        wingR1.setRotationPoint(0F, 0F, 0F);
        wingR1.setTextureSize(64, 32);
        wingR1.mirror = true;
        setRotation(wingR1, 0F, 0F, 0F);
        wingR1.mirror = false;

        wingR2 = new ModelRenderer(this, 26, 4);
        wingR2.addBox(-7F, 0F, 0F, 7, 0, 2);
        wingR2.setRotationPoint(0F, 0F, 0F);
        wingR2.setTextureSize(64, 32);
        wingR2.mirror = true;
        setRotation(wingR2, 0F, 0F, 0F);
        wingR2.mirror = false;

        wingR11 = new ModelRenderer(this, 31, 6);
        wingR11.addBox(-5F, 0F, 2F, 5, 0, 1);
        wingR11.setRotationPoint(0F, 0F, 0F);
        wingR11.setTextureSize(64, 32);
        wingR11.mirror = true;
        setRotation(wingR11, 0F, 0F, 0F);
        wingR11.mirror = false;

        wingR12 = new ModelRenderer(this, 30, 7);
        wingR12.addBox(-6F, 0F, 3F, 5, 0, 1);
        wingR12.setRotationPoint(0F, 0F, 0F);
        wingR12.setTextureSize(64, 32);
        wingR12.mirror = true;
        setRotation(wingR12, 0F, 0F, 0F);
        wingR12.mirror = false;

        wingR13 = new ModelRenderer(this, 29, 8);
        wingR13.addBox(-7F, 0F, 4F, 5, 0, 2);
        wingR13.setRotationPoint(0F, 0F, 0F);
        wingR13.setTextureSize(64, 32);
        wingR13.mirror = true;
        setRotation(wingR13, 0F, 0F, 0F);
        wingR13.mirror = false;

        wingR14 = new ModelRenderer(this, 30, 10);
        wingR14.addBox(-6F, 0F, 6F, 4, 0, 2);
        wingR14.setRotationPoint(0F, 0F, 0F);
        wingR14.setTextureSize(64, 32);
        wingR14.mirror = true;
        setRotation(wingR14, 0F, 0F, 0F);
        wingR14.mirror = false;

        wingL15 = new ModelRenderer(this, 31, 12);
        wingL15.addBox(5F, 0F, 8F, 1, 0, 1);
        wingL15.setRotationPoint(0F, 0F, 0F);
        wingL15.setTextureSize(64, 32);
        wingL15.mirror = true;
        setRotation(wingL15, 0F, 0F, 0F);

        wingR15 = new ModelRenderer(this, 31, 12);
        wingR15.addBox(-5F, 0F, 8F, 1, 0, 1);
        wingR15.setRotationPoint(0F, 0F, 0F);
        wingR15.setTextureSize(64, 32);
        wingR15.mirror = true;
        setRotation(wingR15, 0F, 0F, 0F);

        wingR4 = new ModelRenderer(this, 31, 0);
        wingR4.addBox(-6F, 0F, -4F, 2, 0, 1);
        wingR4.setRotationPoint(0F, 0F, 0F);
        wingR4.setTextureSize(64, 32);
        wingR4.mirror = true;
        setRotation(wingR4, 0F, 0F, 0F);
        wingR4.mirror = false;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head.render(f5);
        belly.render(f5);
    }

    public void renderWingsR(float f5) {
        wingR3.render(f5);
        wingR0.render(f5);
        wingR1.render(f5);
        wingR2.render(f5);
        wingR11.render(f5);
        wingR12.render(f5);
        wingR13.render(f5);
        wingR14.render(f5);
        wingR15.render(f5);
        wingR4.render(f5);
    }

    public void renderWingsL(float f5) {
        wingL0.render(f5);
        wingL1.render(f5);
        wingL2.render(f5);
        wingL3.render(f5);
        wingL4.render(f5);
        wingL11.render(f5);
        wingL12.render(f5);
        wingL13.render(f5);
        wingL14.render(f5);
        wingL15.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
    }
}
