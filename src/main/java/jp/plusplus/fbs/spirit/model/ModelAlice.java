// Date: 2016/03/03 11:24:46
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package jp.plusplus.fbs.spirit.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelAlice extends ModelBase {
    //fields
    ModelRenderer body1;
    ModelRenderer ornaments;
    ModelRenderer hair;
    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer rightarm;
    ModelRenderer leftarm;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer body2;
    ModelRenderer ornaments1;

    public ModelAlice() {
        textureWidth = 128;
        textureHeight = 64;

        body1 = new ModelRenderer(this, 32, 0);
        body1.addBox(-5F, 7F, -3F, 10, 6, 6);
        body1.setRotationPoint(0F, 0F, 0F);
        body1.setTextureSize(128, 64);
        body1.mirror = true;
        setRotation(body1, 0F, 0F, 0F);
        ornaments = new ModelRenderer(this, 0, 47);
        ornaments.addBox(-6F, -9F, -2F, 3, 2, 1);
        ornaments.setRotationPoint(0F, 0F, 0F);
        ornaments.setTextureSize(128, 64);
        ornaments.mirror = true;
        setRotation(ornaments, 0F, 0F, 0F);
        hair = new ModelRenderer(this, 64, 0);
        hair.addBox(-4F, 0F, 3F, 8, 10, 1);
        hair.setRotationPoint(0F, 0F, 0F);
        hair.setTextureSize(128, 64);
        hair.mirror = true;
        setRotation(hair, 0F, 0F, 0F);
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, 0F, 0F);
        head.setTextureSize(128, 64);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        body = new ModelRenderer(this, 16, 16);
        body.addBox(-4F, 0F, -2F, 8, 7, 4);
        body.setRotationPoint(0F, 0F, 0F);
        body.setTextureSize(128, 64);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        rightarm = new ModelRenderer(this, 56, 16);
        rightarm.addBox(-3F, -2F, -2F, 4, 12, 4);
        rightarm.setRotationPoint(-5F, 2F, 0F);
        rightarm.setTextureSize(128, 64);
        rightarm.mirror = true;
        setRotation(rightarm, 0F, 0F, 0F);
        leftarm = new ModelRenderer(this, 40, 16);
        leftarm.addBox(-1F, -2F, -2F, 4, 12, 4);
        leftarm.setRotationPoint(5F, 2F, 0F);
        leftarm.setTextureSize(128, 64);
        leftarm.mirror = true;
        setRotation(leftarm, 0F, 0F, 0F);
        rightleg = new ModelRenderer(this, 0, 16);
        rightleg.addBox(-2F, 0F, -2F, 4, 12, 4);
        rightleg.setRotationPoint(-2F, 12F, 0F);
        rightleg.setTextureSize(128, 64);
        rightleg.mirror = true;
        setRotation(rightleg, 0F, 0F, 0F);
        leftleg = new ModelRenderer(this, 0, 16);
        leftleg.addBox(-2F, 0F, -2F, 4, 12, 4);
        leftleg.setRotationPoint(2F, 12F, 0F);
        leftleg.setTextureSize(128, 64);
        leftleg.mirror = true;
        setRotation(leftleg, 0F, 0F, 0F);
        body2 = new ModelRenderer(this, 0, 32);
        body2.addBox(-6F, 10F, -4F, 12, 7, 8);
        body2.setRotationPoint(0F, 3F, 0F);
        body2.setTextureSize(128, 64);
        body2.mirror = true;
        setRotation(body2, 0F, 0F, 0F);
        ornaments1 = new ModelRenderer(this, 0, 50);
        ornaments1.addBox(3F, -9F, -2F, 3, 2, 1);
        ornaments1.setRotationPoint(0F, 0F, 0F);
        ornaments1.setTextureSize(128, 64);
        ornaments1.mirror = true;
        setRotation(ornaments1, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
        body1.render(f5);
        ornaments.render(f5);
        hair.render(f5);
        head.render(f5);
        body.render(f5);
        rightarm.render(f5);
        leftarm.render(f5);
        rightleg.render(f5);
        leftleg.render(f5);
        body2.render(f5);
        ornaments1.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float angleY, float angleX, float p_78087_6_, Entity entity) {
        this.head.rotateAngleY = angleY / (180F / (float) Math.PI);
        this.head.rotateAngleX = angleX / (180F / (float) Math.PI);
        this.ornaments1.rotateAngleY = this.ornaments.rotateAngleY = this.head.rotateAngleY;
        this.ornaments1.rotateAngleX = this.ornaments.rotateAngleX = this.head.rotateAngleX;
        this.rightarm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 2.0F * p_78087_2_ * 0.5F;
        this.leftarm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F;
        this.rightarm.rotateAngleZ = 0.0F;
        this.leftarm.rotateAngleZ = 0.0F;
        this.rightleg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
        this.leftleg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 1.4F * p_78087_2_;
        this.rightleg.rotateAngleY = 0.0F;
        this.leftleg.rotateAngleY = 0.0F;

        this.rightarm.rotateAngleY = 0.0F;
        this.leftarm.rotateAngleY = 0.0F;
        float f6;
        float f7;

        if (this.onGround > -9990.0F) {
            f6 = this.onGround;
            this.body2.rotateAngleY = this.body1.rotateAngleY = this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float) Math.PI * 2.0F) * 0.2F;
            this.rightarm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
            this.rightarm.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
            this.leftarm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
            this.leftarm.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
            this.rightarm.rotateAngleY += this.body.rotateAngleY;
            this.leftarm.rotateAngleY += this.body.rotateAngleY;
            this.leftarm.rotateAngleX += this.body.rotateAngleY;
            f6 = 1.0F - this.onGround;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0F - f6;
            f7 = MathHelper.sin(f6 * (float) Math.PI);
            float f8 = MathHelper.sin(this.onGround * (float) Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
            this.rightarm.rotateAngleX = (float) ((double) this.rightarm.rotateAngleX - ((double) f7 * 1.2D + (double) f8));
            this.rightarm.rotateAngleY += this.body.rotateAngleY * 2.0F;
            this.rightarm.rotateAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
        }

        this.body2.rotateAngleX = this.body1.rotateAngleX = this.body.rotateAngleX = 0.0F;
        this.rightleg.rotationPointZ = 0.1F;
        this.leftleg.rotationPointZ = 0.1F;
        this.rightleg.rotationPointY = 12.0F;
        this.leftleg.rotationPointY = 12.0F;
        this.head.rotationPointY = 0.0F;
        this.ornaments.rotationPointY = 0.0F;

        this.rightarm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
        this.leftarm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
        this.rightarm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
        this.leftarm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
    }
}
