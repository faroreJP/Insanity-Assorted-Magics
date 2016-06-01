package jp.plusplus.fbs.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class ModelMagicBall extends ModelMagicBase {
    public ModelMagicBall(int offX, int offY){
        super(offX, offY);
        model = new ModelRenderer(this, offX, offY);
        model.addBox(-2F, -2F, -2F, 4, 4, 4);
        model.setRotationPoint(0F, 0F, 0F);
        model.setTextureSize(64, 32);
        //model.mirror = true;
        setRotation(model, 0F, 0F, 0F);
    }
}
