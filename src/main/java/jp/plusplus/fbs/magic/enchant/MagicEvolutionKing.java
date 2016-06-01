package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class MagicEvolutionKing extends MagicEnchantBase {
    public MagicEvolutionKing() {
        super(4, 6);
    }

    @Override
    public boolean checkSuccess() {
        float prob=(isSpelled?0.6f:0.2f)+0.05f*(property.getMagicLevel()-bookData.lv);
        return rand.nextFloat()<=prob;
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int d = getDuration(60, 2);
        int a = getAmplifier(5);

        if(success){
            if(this.isSpelled) d+=30*20;
            entity.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.resistance.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.field_76434_w.getId(), d, a));
        }
        else{
            entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.wither.getId(), d/5, a));
        }
    }
}
