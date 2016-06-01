package jp.plusplus.fbs.particle;

import jp.plusplus.fbs.FBS;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * Created by nori on 2016/03/11.
 */
public class EntityTrajectoryFX extends EntityFX {
    public double[][] plane=new double[4][3];

    public EntityTrajectoryFX(World world, double x, double y, double z, double mX, double mY, double mZ) {
        super(world, x, y, z, mX, mY, mZ);
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.prevPosX=this.posX = x;
        this.prevPosY=this.posY = y;
        this.prevPosZ=this.posZ = z;
        this.particleScale = 0.2F;
        this.particleRed = this.particleGreen = this.particleBlue =this.particleAlpha= 1.0F;
        this.particleMaxAge = 6;
        this.noClip = true;

        particleTextureIndexX=0;
        particleTextureIndexY=2;

        //FBS.logger.info("spawn");

        double mmsize=Math.sqrt(motionX*motionX+motionY*motionY+motionZ*motionZ);

        float rotYaw=(float)Math.atan2(motionX, motionZ)+(float)Math.PI/2.f;
        float rotPitch=(float)Math.asin(-motionY / mmsize);

        float r=2.25f;
        float minx=-r, maxx=r, miny=-r, maxy=r;
        double[][] t=new double[][]{
                {0,miny,minx},
                {0,maxy,minx},
                {0,maxy,maxx},
                {0,miny,maxx}
        };
        double[][] t2=new double[4][3];

        double cx=MathHelper.cos(rotYaw), sx=MathHelper.sin(rotYaw);
        double cy=MathHelper.cos(rotPitch), sy=MathHelper.sin(rotPitch);

        for(int i=0;i<4;i++){
            t2[i][0]=cy*t[i][0]-sy*t[i][1];
            t2[i][1]=sy*t[i][0]+cy*t[i][1];
            t2[i][2]=t[i][2];
        }
        for(int i=0;i<4;i++){
            plane[i][0]=cx*t2[i][0]+sx*t2[i][2];
            plane[i][1]=t2[i][1];
            plane[i][2]=-sx*t2[i][0]+cx*t2[i][2];
        }


        //this.setParticleTextureIndex(rand.nextInt(26));
    }

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        return 0xffffff;
    }

    @Override
    public float getBrightness(float p_70013_1_) {

        /*
        float f1 = super.getBrightness(p_70013_1_);
        float f2 = (float) this.particleAge / (float) this.particleMaxAge;
        f2 *= f2;
        f2 *= f2;
        return f1 * (1.0F - f2) + f2;
        */
        return 1.f;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        particleScale=0.2f+1.8f*particleAge/particleMaxAge;

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
    public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        float f10 = 0.2F * this.particleScale;
        float f6 = particleIcon.getMinU() + (particleTextureIndexX) / 128.f;
        float f7 = f6 + 4f / 128.0f;
        float f8 = particleIcon.getMinV() + (2 * particleTextureIndexY) / 128.f;
        float f9 = f8 + 4f / 128.0f;

        float f11 = (float) (this.prevPosX - interpPosX);
        float f12 = (float) (this.prevPosY - interpPosY);
        float f13 = (float) (this.prevPosZ - interpPosZ);

        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tessellator.addVertexWithUV(f11 + f10 * plane[0][0], f12 + f10 * plane[0][1], f13 + f10 * plane[0][2], f7, f9);
        tessellator.addVertexWithUV(f11 + f10 * plane[1][0], f12 + f10 * plane[1][1], f13 + f10 * plane[1][2], f7, f8);
        tessellator.addVertexWithUV(f11 + f10 * plane[2][0], f12 + f10 * plane[2][1], f13 + f10 * plane[2][2], f6, f8);
        tessellator.addVertexWithUV(f11 + f10 * plane[3][0], f12 + f10 * plane[3][1], f13 + f10 * plane[3][2], f6, f9);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f11 + f10 * plane[3][0], f12 + f10 * plane[3][1], f13 + f10 * plane[3][2], f6, f9);
        tessellator.addVertexWithUV(f11 + f10 * plane[2][0], f12 + f10 * plane[2][1], f13 + f10 * plane[2][2], f6, f8);
        tessellator.addVertexWithUV(f11 + f10 * plane[1][0], f12 + f10 * plane[1][1], f13 + f10 * plane[1][2], f7, f8);
        tessellator.addVertexWithUV(f11 + f10 * plane[0][0], f12 + f10 * plane[0][1], f13 + f10 * plane[0][2], f7, f9);
    }

    @Override
    protected void fall(float p_70069_1_) {}

    @Override
    public boolean isWet() {
        return false;
    }

    @Override
    public boolean isInWater()
    {
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }
}
