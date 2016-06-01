package jp.plusplus.fbs.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/10/15.
 */
public class EntityTracksFX extends EntityFX {
    protected float initialParticleScale;

    public EntityTracksFX(World world, double x, double y, double z, double mx, double my, double mz, float red, float green, float blue) {
        this(world, x, y, z, mx, my, mz, 1.0F, red, green, blue);
    }

    public EntityTracksFX(World world, double x, double y, double z, double mx, double my, double mz, float red, float green, float blue, float scale) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += mx * 0.4D;
        this.motionY += my * 0.4D;
        this.motionZ += mz * 0.4D;
        this.particleRed = this.particleGreen = this.particleBlue = (float) (Math.random() * 0.30000001192092896D + 0.6000000238418579D);
        this.particleScale *= 0.75F;
        this.particleScale *= scale;
        this.initialParticleScale = this.particleScale;
        this.particleMaxAge = (int) (6.0D / (Math.random() * 0.8D + 0.6D));
        this.particleMaxAge = (int) ((float) this.particleMaxAge * scale);
        this.noClip = false;

        particleRed = red;
        particleGreen = green;
        particleBlue = blue;

        this.setParticleTextureIndex(65);
        this.onUpdate();
    }

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

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        //this.particleGreen = (float) ((double) this.particleGreen * 0.96D);
        //this.particleBlue = (float) ((double) this.particleBlue * 0.9D);
        this.motionX *= 0.699999988079071D;
        this.motionY *= 0.699999988079071D;
        this.motionZ *= 0.699999988079071D;
        this.motionY -= 0.019999999552965164D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
