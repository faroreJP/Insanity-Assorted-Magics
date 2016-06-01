package jp.plusplus.fbs.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class EntityMagicFireBolt extends EntityMagicProjectileBase {
    public EntityMagicFireBolt(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    public EntityMagicFireBolt(World par1World, EntityLivingBase par2EntityLivingBase, float speed, float speed2, float damage) {
        super(par1World, par2EntityLivingBase, speed, speed2, 0, 0, 0);
        setDamage(damage);
    }

    @Override
    public boolean canExist() {
        return worldObj.isRemote || ticksExisted < 20 * 1.5;
    }

    @Override
    public void onCollideWithPlayer(MovingObjectPosition pos, EntityPlayer entity) {
        if (!worldObj.isRemote && !shootingEntity.isEntityEqual(entity)) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            entity.setFire(20 * 3);
            setDead();
        }
    }

    @Override
    public void onCollideWithMob(MovingObjectPosition pos, EntityMob entity) {
        if (!worldObj.isRemote) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            entity.setFire(20 * 3);
            setDead();
        }
    }

    @Override
    public void onCollideWithLiving(MovingObjectPosition pos, EntityLiving entity) {
        if (!worldObj.isRemote) {
            entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, entity), getDamage());
            entity.setFire(20 * 3);
            setDead();
        }
    }

    @Override
    public void onCollideWithBlock(MovingObjectPosition pos, Block block) {
        if (!worldObj.isRemote) {

            int i = pos.blockX;
            int j = pos.blockY;
            int k = pos.blockZ;

            switch (pos.sideHit) {
                case 0:
                    --j;
                    break;
                case 1:
                    ++j;
                    break;
                case 2:
                    --k;
                    break;
                case 3:
                    ++k;
                    break;
                case 4:
                    --i;
                    break;
                case 5:
                    ++i;
            }

            if (this.worldObj.isAirBlock(i, j, k)) {
                this.worldObj.setBlock(i, j, k, Blocks.fire);
            }

            setDead();
        }
    }

    @Override
    protected void setParticleColor(){
        particleRed=0.8f+0.2f*rand.nextFloat();
        particleGreen=0.5f*rand.nextFloat();
        particleBlue=0;
    }

}
