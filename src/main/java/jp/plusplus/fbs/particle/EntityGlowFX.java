package jp.plusplus.fbs.particle;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class EntityGlowFX extends EntityFX {
    public EntityGlowFX(World p_i1218_1_, double p_i1218_2_, double p_i1218_4_, double p_i1218_6_) {
        super(p_i1218_1_, p_i1218_2_, p_i1218_4_, p_i1218_6_);
        Random r=p_i1218_1_.rand;

        particleAge = 0;
        particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        particleScale *= 0.75F;
        noClip = false;

        particleRed = 0.8f + r.nextFloat() * 0.2f;
        particleGreen = 0;
        particleBlue = 0.8f + r.nextFloat() * 0.2f;

        motionX = (r.nextFloat()-r.nextFloat()) * 0.02f;
        motionY = (r.nextFloat()-r.nextFloat()) * 0.02f;
        motionZ = (r.nextFloat()-r.nextFloat()) * 0.02f;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround){
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @Override
    public int getFXLayer() {
        return 0;
    }
    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_){
        float f6 = ((float)this.particleAge + p_70539_2_) / (float)this.particleMaxAge * 32.0F;

        if (f6 < 0.0F) f6 = 0.0F;
        if (f6 > 1.0F) f6 = 1.0F;

        this.particleScale *= f6;
        super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);
    }

    /*
    public static void SpawnParticle(World world, double x, double y, double z){
        EntityGlowFX e=new EntityGlowFX(world, x,y,z);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(e);
    }
    */
}
