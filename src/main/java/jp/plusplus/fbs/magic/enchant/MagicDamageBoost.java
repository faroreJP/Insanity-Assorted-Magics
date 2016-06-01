package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/06/16.
 */
public class MagicDamageBoost extends MagicEnchantBase {
    public MagicDamageBoost() {
        super(4, 6);
    }

    @Override
    public boolean checkSuccess() {
        int d=getLvDiff();
        float prob=isSpelled?0.5f:0.3f;
        if(d>0) prob+=0.08f*d;
        return rand.nextFloat()<=prob;
    }
    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int d = getDuration(60, 0.3f);
        int a = getAmplifier(8);

        if(success){
            if(this.isSpelled) d+=60*20;
            entity.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), d, a));
        }
        else entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), d, a));
    }
}
