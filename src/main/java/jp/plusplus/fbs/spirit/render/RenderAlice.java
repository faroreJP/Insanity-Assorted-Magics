package jp.plusplus.fbs.spirit.render;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.spirit.model.ModelAlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2016/03/03.
 */
public class RenderAlice extends RendererLivingEntity {
    private static final ResourceLocation rl = new ResourceLocation(FBS.MODID, "textures/entity/Alice.png");

    public RenderAlice() {
        super(new ModelAlice(), 0.5f);
        renderManager= RenderManager.instance;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return rl;
    }

    public float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
        float f3;

        for (f3 = p_77034_2_ - p_77034_1_; f3 < -180.0F; f3 += 360.0F) {
            ;
        }

        while (f3 >= 180.0F) {
            f3 -= 360.0F;
        }

        return p_77034_1_ + p_77034_3_ * f3;
    }

    @Override
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(p_76986_1_, p_76986_9_);

        if (this.renderPassModel != null) {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = false;

        if (this.renderPassModel != null) {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = false;

        if (this.renderPassModel != null) {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try {
            float f2 = this.interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
            float f3 = this.interpolateRotation(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
            float f4;

            float f13 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
            this.renderLivingAt(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
            f4 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
            this.rotateCorpse(p_76986_1_, f4, f2, p_76986_9_);
            float f5 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(p_76986_1_, p_76986_9_);
            GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
            float f6 = p_76986_1_.prevLimbSwingAmount + (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
            float f7 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0F - p_76986_9_);

            if (f6 > 1.0F) {
                f6 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(p_76986_1_, f7, f6, p_76986_9_);
            this.renderModel(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
            int j;
            float f8;
            float f9;
            float f10;

            for (int i = 0; i < 4; ++i) {
                j = this.shouldRenderPass(p_76986_1_, i, p_76986_9_);

                if (j > 0) {
                    this.renderPassModel.setLivingAnimations(p_76986_1_, f7, f6, p_76986_9_);
                    this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);

                    if ((j & 240) == 16) {
                        this.func_82408_c(p_76986_1_, i, p_76986_9_);
                        this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
                    }

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            GL11.glDepthMask(true);
            this.renderEquippedItems(p_76986_1_, p_76986_9_);
            float f14 = p_76986_1_.getBrightness(p_76986_9_);
            j = this.getColorMultiplier(p_76986_1_, f14, p_76986_9_);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((j >> 24 & 255) > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (p_76986_1_.hurtTime > 0 || p_76986_1_.deathTime > 0) {
                    GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);

                    for (int l = 0; l < 4; ++l) {
                        if (this.inheritRenderPass(p_76986_1_, l, p_76986_9_) >= 0) {
                            GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
                        }
                    }
                }

                if ((j >> 24 & 255) > 0) {
                    f8 = (float) (j >> 16 & 255) / 255.0F;
                    f9 = (float) (j >> 8 & 255) / 255.0F;
                    float f15 = (float) (j & 255) / 255.0F;
                    f10 = (float) (j >> 24 & 255) / 255.0F;
                    GL11.glColor4f(f8, f9, f15, f10);
                    this.mainModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);

                    for (int i1 = 0; i1 < 4; ++i1) {
                        if (this.inheritRenderPass(p_76986_1_, i1, p_76986_9_) >= 0) {
                            GL11.glColor4f(f8, f9, f15, f10);
                            this.renderPassModel.render(p_76986_1_, f7, f6, f4, f3 - f2, f13, f5);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } catch (Exception exception) {
            FBS.logger.error("Couldn\'t render entity", exception);
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        //this.passSpecialRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
    }
}
