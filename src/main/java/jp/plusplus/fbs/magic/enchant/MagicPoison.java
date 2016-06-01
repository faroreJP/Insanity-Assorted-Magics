package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Createdby pluslus_Fon 2015/10/22.
 */
public class MagicPoison extends MagicEnchantBase {
    public MagicPoison() {
        super(4, 6);
    }

    @Override
    public boolean checkSuccess() {
        if(!isSpelled) return false;

        float prob=0.4f+0.02f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        Entity e=getTouchEntity();
        if(e instanceof EntityLivingBase){
            enchant((EntityLivingBase)e, true);
        }
    }


    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        int d = getDuration(5, 3);
        int a = getAmplifier(10);

        if(success){
            if(isSpelled) d+=3*20;
            entity.addPotionEffect(new PotionEffect(Potion.poison.getId(), d, a));
        }
    }

    @Override
    public ParticleColor setParticleColor(){
        ParticleColor col=new ParticleColor();
        float v=0.1f+0.5f*rand.nextFloat();
        col.blue-=v;
        col.red-=v;
        col.green=0;
        return col;
    }
}
