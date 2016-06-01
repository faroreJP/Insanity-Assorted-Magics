package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/09/19.
 */
public class MagicContemporary extends MagicEnchantBase {
    public MagicContemporary() {
        super(3, 10);
    }

    @Override
    public boolean checkSuccess() {
        int d=getLvDiff();
        float prob=isSpelled?0.6f:0.2f;
        if(d>0) prob+=0.08f*d;
        return rand.nextFloat()<=prob;
    }
    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int d = getDuration(180, 1);
        int a = getAmplifier(8);

        if(success){
            if(this.isSpelled) d+=60*20;
            entity.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.resistance.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), d, a));
        }
        else{
            entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.blindness.getId(), d, a));
        }
    }
}
