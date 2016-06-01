package jp.plusplus.fbs.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/04/03.
 */
public class EntitySnowFX extends EntityFX {
    protected float initialParticleScale;

    public EntitySnowFX(World p_i1218_1_, double p_i1218_2_, double p_i1218_4_, double p_i1218_6_) {
        super(p_i1218_1_, p_i1218_2_, p_i1218_4_, p_i1218_6_);
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.30000001192092896D + 0.6000000238418579D);
        this.particleScale = 1.0F;
        this.initialParticleScale = this.particleScale;
        this.particleMaxAge = (int) (90.0D / (Math.random() * 0.8D + 0.6D));
        //this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
        this.noClip = false;

        this.setParticleTextureIndex(65);
        this.onUpdate();
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.699999988079071D;
        this.motionY *= 0.699999988079071D;
        this.motionZ *= 0.699999988079071D;
        this.motionY -= 0.25*0.019999999552965164D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        float f6 = ((float) this.particleAge + p_70539_2_) / (float) this.particleMaxAge * 32.0F;

        if (f6 < 0.0F) {
            f6 = 0.0F;
        }

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.particleScale = this.initialParticleScale * f6;
        super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
    }
}
