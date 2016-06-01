package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/11/09.
 */
public class MagicCleverness extends MagicEnchantBase {
    public MagicCleverness() {
        super(1, 6);
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
        int d = getDuration(90, 0.5f);
        int a = getAmplifier(7);

        if(success){
            if(this.isSpelled) d+=120*20;
            entity.addPotionEffect(new PotionEffect(Registry.potionCleverness.getId(), d, a));
        }
    }
}
