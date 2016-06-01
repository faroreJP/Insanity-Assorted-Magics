package jp.plusplus.fbs.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by pluslus_F on 2015/06/24.
 */
public class ModelMirror extends ModelBase {
    //variables init:
    public ModelRenderer box;

    //constructor:
    public ModelMirror(){
        box = new ModelRenderer(this, 0, 0);
        box.addBox(-6F, 0F, -2f, 12, 30, 1);
        box.rotateAngleX = 6.1086523819801535F;
        box.setTextureSize(64, 32);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        //render:
        box.render(f5);
    }
}
