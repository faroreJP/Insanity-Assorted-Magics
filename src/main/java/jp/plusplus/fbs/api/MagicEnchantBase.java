package jp.plusplus.fbs.api;

import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by pluslus_F on 2015/06/22.
 */
public abstract class MagicEnchantBase extends MagicBase implements IMagicEnchant {
    protected int trial, max;

    public MagicEnchantBase(int t, int m){
        trial=t;
        max=m;
    }

    @Override
    public void success() {
        enchant(player, true);
    }

    @Override
    public void failure() {
        sanity(trial, max);
        enchant(player, false);
    }

    public int getAmplifier(int rate){
        int t=getLvDiff();
        if(t<=0) return 1;
        int a=1+t/rate;
        return a>5?5:a;
    }
    public int getDuration(int base, float rate){
        int t=getLvDiff();
        if(t<=0) return 20*base;
        return (int)(20*(base+t/rate));
    }

    @Override
    public float damageScale(EntityLivingBase entity){
        return 0.f;
    }

    @Override
    public float damageValue(EntityLivingBase entity){
        return 0.f;
    }

    @Override
    public ParticleColor setParticleColor(){
        return new ParticleColor();
    }
}
