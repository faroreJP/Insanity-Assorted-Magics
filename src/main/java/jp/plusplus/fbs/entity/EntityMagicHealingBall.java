package jp.plusplus.fbs.entity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.event.FBSEventHandler;
import jp.plusplus.fbs.particle.EntityTracksFX;
import jp.plusplus.fbs.particle.EntityTrajectoryFX;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.LinkedList;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class EntityMagicHealingBall extends EntityMagicProjectileBase {
    LinkedList<Integer> entityIds=new LinkedList<Integer>();
    int amplifier;

    public EntityMagicHealingBall(World p_i1582_1_) {
        super(p_i1582_1_);
        this.setSize(0.25F, 0.25F);
    }
    public EntityMagicHealingBall(World par1World, EntityLivingBase par2EntityLivingBase, int amp) {
        super(par1World, par2EntityLivingBase, 0, 0, 0, 0, 0);
        amplifier=amp;
        setPosition(posX, posY - 0.2D, posZ);
        this.setSize(0.8F, 0.8F);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        amplifier=nbt.getInteger("Amplifier");

        entityIds=new LinkedList<Integer>();
        int s=nbt.getInteger("ListSize");
        for(int i=0;i<s;i++){
            entityIds.add(nbt.getInteger("EntityIds-Id"+i));
        }
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Amplifier", amplifier);

        nbt.setInteger("ListSize", entityIds.size());
        for(int i=0;i<entityIds.size();i++){
            nbt.setInteger("EntityIds-Id"+i, entityIds.get(i));
        }
    }


    @Override
    public void onUpdate() {
        super.onUpdate();
        float s=MathHelper.sin(2 * 3.14159265f / 10.0f * (ticksExisted % 10));
        float c=MathHelper.cos(2 * 3.14159265f / 10.0f * (ticksExisted % 10));

        float mx=0.15f*c;
        float my=0;
        float mz=0.15f*s;

        this.motionX = mx;
        this.motionY = my;
        this.motionZ = mz;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(mx * mx + mz * mz);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(mx, mz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(my, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public boolean canExist() {
        return worldObj.isRemote || ticksExisted < 20 * 20;
    }

    @Override
    public void onCollideWithPlayer(MovingObjectPosition pos, EntityPlayer entity) {
        if (!worldObj.isRemote) {
            for(Integer id : entityIds){
                if(id==entity.getEntityId()){
                    return;
                }
            }

            //entity.addPotionEffect(new PotionEffect(Potion.heal.getId(), 20, amplifier));
            entity.setHealth(entity.getHealth()+0.5f*amplifier);
            entityIds.add(entity.getEntityId());
        }
    }

    @Override
    public void onCollideWithLiving(MovingObjectPosition pos, EntityLiving entity) {
        if (!worldObj.isRemote) {
            for(Integer id : entityIds){
                if(id==entity.getEntityId()){
                    return;
                }
            }

            //entity.addPotionEffect(new PotionEffect(Potion.heal.getId(), 20, amplifier));
            entity.setHealth(entity.getHealth() + 0.5f * amplifier);
            entityIds.add(entity.getEntityId());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void spawnParticle(){
        //setParticleColor();
        particleRed=particleBlue=0.5f+0.2f*rand.nextFloat();
        particleGreen=1.f;

        EntityTracksFX fx=new EntityTracksFX(worldObj, posX, posY, posZ, 0, -(0.3f+0.6f*rand.nextFloat()), 0, particleRed, particleGreen, particleBlue, getParticleSize());
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }

}
