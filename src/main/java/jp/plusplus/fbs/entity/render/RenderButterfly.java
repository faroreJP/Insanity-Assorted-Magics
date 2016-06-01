package jp.plusplus.fbs.entity.render;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.entity.EntityButterfly;
import jp.plusplus.fbs.model.ModelButterfly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by pluslus_F on 2015/06/25.
 */
public class RenderButterfly extends Render {
    private static final ResourceLocation bulletTextures = new ResourceLocation(FBS.MODID+":textures/entity/butterfly.png");

    protected ModelButterfly model;

    public RenderButterfly() {
        this.model = new ModelButterfly();
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        //FMLLog.severe("render! at:"+par2+","+par4+","+par6);

        this.bindEntityTexture(entity);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glEnable(GL11.TRa);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

        GL11.glRotatef(((EntityButterfly)entity).rotationW, 0, 0, 1);
        model.renderWingsR(0.0625F);
        GL11.glRotatef(-2*((EntityButterfly) entity).rotationW, 0, 0, 1);
        model.renderWingsL(0.0625F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return bulletTextures;
    }
}
