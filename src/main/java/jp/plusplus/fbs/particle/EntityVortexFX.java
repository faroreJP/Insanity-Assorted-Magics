package jp.plusplus.fbs.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/10/18.
 */
public class EntityVortexFX extends EntityFX{
    public static final double spd=0.4;

    protected int delay;
    protected float spinPitch;
    protected float spinYaw;
    protected float radius;
    protected double firstX, firstY, firstZ;

    public EntityVortexFX(World w, double x, double y, double z, int d, double r, float red, float green, float blue, float scale) {
        super(w, x, y, z);
        firstX=x;
        firstY=y;
        firstZ=z;
        rotationPitch=2*(float)Math.PI*rand.nextFloat();
        rotationYaw=2*(float)Math.PI*rand.nextFloat();

        float a=2*(float)Math.PI*rand.nextFloat();
        spinPitch=2*(float)Math.PI/9.f* MathHelper.cos(a);
        spinYaw=2*(float)Math.PI/9.f* MathHelper.sin(a);

        delay=d;
        radius=0;
        particleRed=red;
        particleGreen=green;
        particleBlue=blue;
        particleAlpha=0.f;
        particleScale=scale;
        particleMaxAge=MathHelper.floor_double(r/spd)+delay;
        this.noClip = false;

        this.setParticleTextureIndex(65);
        this.onUpdate();
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

        if(particleAge>=delay){
            //座標の決定
            posX=firstX-radius*MathHelper.cos(-rotationPitch)*MathHelper.sin(-rotationYaw);
            posY=firstY+radius*MathHelper.sin(-rotationPitch);
            posZ=firstZ-radius*MathHelper.cos(-rotationPitch)*MathHelper.cos(-rotationYaw);

            rotationPitch+=spinPitch;
            rotationYaw+=spinYaw;
            radius+=spd;

            particleAlpha=1.f;
        }
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        if(particleAge<delay) return;
        super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
    }
}
