package jp.plusplus.fbs.entity;

import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by pluslus_F on 2015/06/25.
 * 蝶。
 */
public class EntityButterfly extends EntityBat {
    public float rotationW;
    public boolean rotationFlag;
    public int sinTicks;
    public int nextDirTicks;

    public EntityButterfly(World p_i1582_1_) {
        super(p_i1582_1_);
        this.setIsBatHanging(false);
    }

    public EntityButterfly(World w, float x, float y, float z) {
        this(w);
        setLocationAndAngles(x, y, z, 0, 0);
        setIsBatHanging(false);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        super.readEntityFromNBT(p_70037_1_);
        rotationFlag = p_70037_1_.getBoolean("RotationWingsFlag");
        rotationW = p_70037_1_.getFloat("RotationWingsAngle");
        sinTicks = p_70037_1_.getInteger("SinTicks");
        nextDirTicks = p_70037_1_.getInteger("NextDirTicks");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setBoolean("RotationWingsFlag", rotationFlag);
        p_70014_1_.setFloat("RotationWingsAngle", rotationW);
        p_70014_1_.setInteger("SinTicks", sinTicks);
        p_70014_1_.setInteger("NextDirTicks", nextDirTicks);
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.isEntityInvulnerable()) {
            return false;
        } else {
            if (!this.worldObj.isRemote) {
                setDead();
                if (p_70097_1_.getSourceOfDamage() instanceof EntityPlayer) {

                    EntityPlayer ep=(EntityPlayer)p_70097_1_.getSourceOfDamage();
                    SanityManager.loseSanity(ep, 1, 6, true);
                    this.entityDropItem(new ItemStack(ItemCore.butterfly), 0);
                }
            }
            return true;
        }
    }

    @Override
    public void onUpdate(){
        //setDead();
        super.onUpdate();

        //羽
        float rotS=20;
        if(rotationFlag){
            rotationW+=rotS;
            if(rotationW>=80){
                rotationFlag=!rotationFlag;
                rotationW=80;
            }
        }
        else{
            rotationW-=rotS;
            if(rotationW<=-80){
                rotationFlag=!rotationFlag;
                rotationW=-80;
            }
        }

        //func_145775_I();
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound()
    {
        return null;
    }

    @Override
    protected String getDeathSound()
    {
        return null;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute(){ return EnumCreatureAttribute.UNDEFINED; }
}
