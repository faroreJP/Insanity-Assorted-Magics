package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/09/19.
 */
public class MagicFireArmor extends MagicEnchantBase {
    public MagicFireArmor() {
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
        int d = getDuration(60, 1);
        int a = getAmplifier(8);

        if(success){
            if(this.isSpelled) d+=30*20;
            entity.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), d, a));
        }
        else entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), d, a));
    }

    @Override
    public ParticleColor setParticleColor(){
        ParticleColor col=new ParticleColor();
        col.green-=0.3f+0.25f*rand.nextFloat();
        col.blue-=0.7f+0.3f*rand.nextFloat();
        return col;
    }
}
