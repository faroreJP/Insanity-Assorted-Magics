package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.entity.EntityMagicWedge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class MagicWedge extends MagicBase implements IMagicEnchant {
    @Override
    public boolean checkSuccess() {
        if(isSpelled) return true;

        float prob=0.4f+0.03f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        int l=getLvDiff();
        int eLv=1;
        int eDu=20*(isSpelled?20:10);
        float dm=isSpelled?1.0f:0.5f;
        if(l>0){
            eLv+=l/5;
            eDu+=20*(l/2);
            dm+=0.5f*(l/8);
        }

        Entity e=new EntityMagicWedge(world, player, 1.0F, 1.0F, dm, eLv, eDu);
        world.spawnEntityInWorld(e);
    }

    @Override
    public void failure() {
        sanity(1,6);
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int l=getLvDiff();
        int eLv=1;
        int eDu=20*(isSpelled?20:10);
        if(l>0){
            eLv+=l/5;
            eDu+=20*(l/2);
        }

        entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), eDu, eLv));
    }

    @Override
    public float damageScale(EntityLivingBase entity) {
        return 0.75f;
    }

    @Override
    public float damageValue(EntityLivingBase entity) {
        return 0.f;
    }

    @Override
    public ParticleColor setParticleColor() {
        return new ParticleColor();
    }
}
