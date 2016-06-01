package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.api.MagicEnchantBase;
import jp.plusplus.fbs.entity.EntityButterfly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

/**
 * Created by plusplus_F on 2015/08/23.
 * 重要な魔法
 */
public class MagicButterfly extends MagicEnchantBase {
    public MagicButterfly() {
        super(2, 4);
    }

    @Override
    public void success() {
        player.attackEntityFrom(new DamageSource("fbs.butterfly"), 10000);
        enchant(player, true);
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        if(!success) return;

        //死んでいれば絶対蝶になる
        //死んでなくても蝶になる
        boolean flag=entity.isDead;
        if(rand.nextFloat()<0.3f){
            entity.attackEntityFrom(new DamageSource("fbs.butterfly"), 10000);
            flag=true;
        }

        if(flag){
            EntityButterfly eb=new EntityButterfly(entity.worldObj, (float)entity.posX, (float)entity.posY+1, (float)entity.posZ);
            entity.worldObj.spawnEntityInWorld(eb);

            if(entity instanceof EntityPlayer){
                ((EntityPlayer) entity).triggerAchievement(AchievementRegistry.sublimation);
            }
        }
    }

    @Override
    public boolean checkSuccess() {
        int d=getLvDiff();
        float prob=isSpelled?0.35f:0.1f;
        if(d>0) prob+=0.05f*d;
        return rand.nextFloat()<=prob;
    }

    @Override
    public ParticleColor setParticleColor(){
        ParticleColor col=new ParticleColor();
        float v=0.1f+0.3f*rand.nextFloat();
        col.blue-=v;
        col.red-=v;
        col.green=0;
        return col;
    }
}
