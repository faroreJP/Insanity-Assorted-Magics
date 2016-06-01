// Date: 2016/03/07 16:29:33
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package jp.plusplus.fbs.storage;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMealInlet extends ModelBase {
    //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;

    public ModelMealInlet(boolean io) {
        textureWidth = 64;
        textureHeight = 32;

        Shape1 = new ModelRenderer(this, 0, io?19:6);
        Shape1.addBox(-5F, 5F, -5F, 10, 3, 10);
        Shape1.setRotationPoint(0F, 0F, 0F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 0, 23);
        Shape2.addBox(-0.3333333F, 0.06666667F, -6F, 2, 4, 2);
        Shape2.setRotationPoint(0F, 0F, 0F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0.7716627F, -1.07818F, 0F);
        Shape3 = new ModelRenderer(this, 40, 25);
        Shape3.addBox(-1F, 1F, 1F, 3, 4, 3);
        Shape3.setRotationPoint(0F, 0F, 0F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, -0.3717861F, 0F);
        Shape4 = new ModelRenderer(this, 30, 23);
        Shape4.addBox(-0.6F, 1F, 2.733333F, 2, 4, 2);
        Shape4.setRotationPoint(0F, 0F, 0F);
        Shape4.setTextureSize(64, 32);
        Shape4.mirror = true;
        setRotation(Shape4, -0.3717861F, -2.41661F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5);
        Shape1.render(f5);
    }
    public void renderCrystal(float f5){
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
    }
}
