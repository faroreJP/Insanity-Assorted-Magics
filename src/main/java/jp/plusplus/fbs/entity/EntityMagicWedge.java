package jp.plusplus.fbs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class EntityMagicWedge extends EntityMagicProjectileBase {
    protected int encLv;
    protected int encDur;

    public EntityMagicWedge(World p_i1582_1_) {
        super(p_i1582_1_);
    }
    public EntityMagicWedge(World par1World, EntityLivingBase par2EntityLivingBase, float speed, float speed2, float damage, int lv, int du) {
        super(par1World, par2EntityLivingBase, speed, speed2, 0, 0, 0);
        setDamage(damage);
        encLv=lv;
        encDur=du;
    }

    public boolean canExist(){ return worldObj.isRemote || ticksExisted<20*3; }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        encLv=nbt.getInteger("EnchantLv");
        encDur=nbt.getInteger("EnchantDur");
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("EnchantLv", encLv);
        nbt.setInteger("EnchantDur", encDur);
    }

    public void onCollideWithPlayer(MovingObjectPosition pos, EntityPlayer entity){
        if(!worldObj.isRemote && !shootingEntity.isEntityEqual(entity)) {
            entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), encDur, encLv));
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            setDead();
        }
    }
    public void onCollideWithMob(MovingObjectPosition pos, EntityMob entity){
        if(!worldObj.isRemote) {
            hitAt(entity);
        }
    }
    public void onCollideWithLiving(MovingObjectPosition pos, EntityLiving entity){
        if(!worldObj.isRemote) {
            hitAt(entity);
        }
    }

    protected void hitAt(EntityLiving entity){
        entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), encDur, encLv));
        entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
        setDead();
    }
}
