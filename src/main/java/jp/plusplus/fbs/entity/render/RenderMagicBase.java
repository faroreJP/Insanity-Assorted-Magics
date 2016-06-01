package jp.plusplus.fbs.entity.render;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.model.ModelMagicBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Createdby pluslus_Fon 2015/06/07.
 */
public class RenderMagicBase extends Render {
    private static final ResourceLocation bulletTextures = new ResourceLocation(FBS.MODID+":textures/entity/magic0.png");

    protected ModelBase modelBullet;

    public RenderMagicBase(ModelBase par1ModelBase) {
        this.modelBullet = par1ModelBase;
        this.shadowSize = 0.0F;
    }

    @Override
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        ModelMagicBase model = (ModelMagicBase) this.modelBullet;

        this.bindEntityTexture(entity);
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 1.0F);
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, -1.0F, 0.0F, 0);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        model.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return bulletTextures;
    }
}
