package jp.plusplus.fbs.entity;

import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class EntityMagicArrow extends EntityMagicProjectileBase {
    public EntityMagicArrow(World p_i1582_1_) {
        super(p_i1582_1_);
    }
    public EntityMagicArrow(World par1World, EntityLivingBase par2EntityLivingBase, float speed, float speed2, float damage) {
        super(par1World, par2EntityLivingBase, speed, speed2, 0, 0, 0);
        setDamage(damage);
    }

    public boolean canExist(){ return worldObj.isRemote || ticksExisted<20*1.5; }

    public void onCollideWithPlayer(MovingObjectPosition pos, EntityPlayer entity){
        if(!worldObj.isRemote && !shootingEntity.isEntityEqual(entity)) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            setDead();
        }
    }
    public void onCollideWithMob(MovingObjectPosition pos, EntityMob entity){
        if(!worldObj.isRemote) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            setDead();
        }
    }
    public void onCollideWithLiving(MovingObjectPosition pos, EntityLiving entity){
        if(!worldObj.isRemote) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            setDead();
        }
    }
}
