package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.entity.EntityMagicFireBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class MagicFireBolt extends MagicBase implements IMagicEnchant {
    @Override
    public boolean checkSuccess() {
        if(isSpelled) return true;

        float prob=0.4f+0.03f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        int d=getLvDiff();
        float dm=isSpelled?1.5f:0.5f;
        if(d>0) dm+=0.5f*(d/5);

        Entity e=new EntityMagicFireBolt(world, player, 1.0F, 1.0F, dm);
        world.spawnEntityInWorld(e);
    }

    @Override
    public void failure() {
        sanity(2, 4);
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        if(success) entity.setFire(10);
    }

    @Override
    public float damageScale(EntityLivingBase entity) {
        return 1.2f;
    }

    @Override
    public float damageValue(EntityLivingBase entity) {
        return 0.f;
    }

    @Override
    public ParticleColor setParticleColor(){
        ParticleColor col=new ParticleColor();
        float v=0.5f+0.5f*rand.nextFloat();
        col.blue-=v;
        col.green-=v;
        return col;
    }
}
