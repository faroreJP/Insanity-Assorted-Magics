package jp.plusplus.fbs.entity.render;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.model.ModelFork;
import jp.plusplus.fbs.block.model.ModelKnife;
import jp.plusplus.fbs.block.model.ModelSpoon;
import jp.plusplus.fbs.entity.EntityButterfly;
import jp.plusplus.fbs.entity.EntityTableware;
import jp.plusplus.fbs.model.ModelButterfly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/11/16.
 */
public class RenderTableware extends Render {
    private static final ResourceLocation textures = new ResourceLocation(FBS.MODID+":textures/entity/Tableware.png");
    protected ModelSpoon spoon=new ModelSpoon();
    protected ModelFork fork=new ModelFork();
    protected ModelKnife knife=new ModelKnife();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTick) {
        if(!(entity instanceof EntityTableware)) return;

        this.bindEntityTexture(entity);
        int meta=((EntityTableware) entity).getItemMetadata();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTick, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTick, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(1.0F*.3f, -1.0F*.3f, -1.0F*.3f);

        switch (meta){
            case 0:
                spoon.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                break;
            case 1:
                knife.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                break;
            case 2:
                fork.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                break;
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return textures;
    }
}
