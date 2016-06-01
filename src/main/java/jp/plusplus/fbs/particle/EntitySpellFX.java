package jp.plusplus.fbs.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/27.
 */
public class EntitySpellFX extends EntityFX {
    private double firstX;
    private double firstY;
    private double firstZ;

    public EntitySpellFX(World world, double x, double y, double z, double mX, double mY, double mZ) {
        super(world, x, y, z, mX, mY, mZ);
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.firstX = this.posX = x;
        this.firstY = this.posY = y;
        this.firstZ = this.posZ = z;
        this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int) (Math.random() * 10.0D) + 30;
        this.noClip = true;
        this.setParticleTextureIndex(rand.nextInt(26));
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        return 0xffffff;
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        return 1.f;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float) this.particleAge / (float) this.particleMaxAge;
        f = 1.0F - f;

        this.posX = this.firstX + this.motionX * (double) f;
        this.posY = this.firstY + this.motionY * (double) f;
        this.posZ = this.firstZ + this.motionZ * (double) f;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }

    @Override
    public int getFXLayer() {
        return 2;
    }

    @Override
    public void setParticleTextureIndex(int p_70536_1_) {
        this.particleTextureIndexX = p_70536_1_ % 16;
        this.particleTextureIndexY = p_70536_1_ / 16;
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        float f10 = 0.1F * this.particleScale;
        float f6=particleIcon.getMinU()+(2*particleTextureIndexX)/128.f;
        float f7=f6+2f/128.0f;
        float f8=particleIcon.getMinV()+(2*particleTextureIndexY)/128.f;
        float f9=f8+2f/128.0f;

        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) p_70539_2_ - interpPosX);
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) p_70539_2_ - interpPosY);
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) p_70539_2_ - interpPosZ);
        p_70539_1_.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        p_70539_1_.addVertexWithUV((double) (f11 - p_70539_3_ * f10 - p_70539_6_ * f10), (double) (f12 - p_70539_4_ * f10), (double) (f13 - p_70539_5_ * f10 - p_70539_7_ * f10), (double) f7, (double) f9);
        p_70539_1_.addVertexWithUV((double) (f11 - p_70539_3_ * f10 + p_70539_6_ * f10), (double) (f12 + p_70539_4_ * f10), (double) (f13 - p_70539_5_ * f10 + p_70539_7_ * f10), (double) f7, (double) f8);
        p_70539_1_.addVertexWithUV((double) (f11 + p_70539_3_ * f10 + p_70539_6_ * f10), (double) (f12 + p_70539_4_ * f10), (double) (f13 + p_70539_5_ * f10 + p_70539_7_ * f10), (double) f6, (double) f8);
        p_70539_1_.addVertexWithUV((double) (f11 + p_70539_3_ * f10 - p_70539_6_ * f10), (double) (f12 - p_70539_4_ * f10), (double) (f13 + p_70539_5_ * f10 - p_70539_7_ * f10), (double) f6, (double) f9);
    }
}
