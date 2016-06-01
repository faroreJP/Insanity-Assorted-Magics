package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/06/16.
 */
public class MagicHealingSelf extends MagicEnchantBase {
    public MagicHealingSelf() {
        super(4, 6);
    }

    @Override
    public boolean checkSuccess() {
        int d=getLvDiff();
        float prob=isSpelled?0.35f:0.1f;
        if(d>0) prob+=0.05f*d;
        return rand.nextFloat()<=prob;
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int d = getDuration(8, 2);
        int a = getAmplifier(8);
        if(success){
            entity.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.heal.getId(), 1, a));
        }
        else{
            entity.addPotionEffect(new PotionEffect(Potion.wither.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.harm.getId(), 1, a));
        }
    }
}
