package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class MagicSpeed extends MagicEnchantBase {
    public MagicSpeed() {
        super(4, 6);
    }

    @Override
    public boolean checkSuccess() {
        if(!isSpelled) return false;

        float prob=0.4f+0.02f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int d = getDuration(30, 1);
        int a = getAmplifier(8);

        if(success){
            if(isSpelled) d+=120*20;
            entity.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), d, a));
            entity.addPotionEffect(new PotionEffect(Potion.jump.getId(), d, a));
        }
        else{
            entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), d, a));
        }
    }

    @Override
    public ParticleColor setParticleColor(){
        ParticleColor col=new ParticleColor();
        float v=0.1f+0.5f*rand.nextFloat();
        col.blue-=v;
        col.red-=v;
        return col;
    }
}
