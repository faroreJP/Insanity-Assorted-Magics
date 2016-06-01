package jp.plusplus.fbs.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.entity.Entity;

/**
 * Createdby pluslus_Fon 2015/06/07.
 */
public class ModelMagicBase extends ModelBase {
    protected ModelRenderer model;

    public ModelMagicBase(int offX, int offY){
        model = new ModelRenderer(this, offX, offY);
        model.addBox(-1.5F, -1.5F, -2.5F, 3, 3, 5);
        model.setRotationPoint(0.0F, 0.0F, 2.5F);
        //model.setRotationPoint(0F, 0F, 0F);
        model.setTextureSize(64, 32);
        //model.mirror = true;
        setRotation(model, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        model.render(f5);
    }

    protected void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
