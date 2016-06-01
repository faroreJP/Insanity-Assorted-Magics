package jp.plusplus.fbs.particle;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by plusplus_F on 2016/03/20.
 */
public class EntitySpellCircleFX extends EntityFX{
    private double firstX;
    private double firstY;
    private double firstZ;

    private double radius;
    private float angle;
    private float rps;

    public EntitySpellCircleFX(World world, double x, double y, double z, double radius, float angle) {
        super(world, x, y, z, 0,0,0);
        this.firstX = this.posX = x;
        this.firstY = this.posY = y;
        this.firstZ = this.posZ = z;
        this.radius=radius;
        this.angle=angle;
        this.particleScale = this.rand.nextFloat() * 0.4F + 0.4F;
        this.particleAlpha=this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int) (Math.random() * 20.0D) + 60;
        this.noClip = true;
        this.setParticleTextureIndex(rand.nextInt(26));

        posX=firstX-radius* MathHelper.sin((float)angle);
        posZ=firstZ+radius*MathHelper.cos((float)angle);

        rps=(float)(2*Math.PI/40.0f);
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

        //螺旋運動
        posX=firstX-radius*MathHelper.sin((float)angle);
        posY+=2.2f/particleMaxAge;
        posZ=firstZ+radius*MathHelper.cos((float)angle);

        angle+=rps;
        radius*=0.98f;


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
