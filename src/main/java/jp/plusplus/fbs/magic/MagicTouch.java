package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

/**
 * Created by pluslus_F on 2015/06/23.
 */
public class MagicTouch extends MagicBase {
    @Override
    public boolean checkSuccess() {
        return true;
    }

    @Override
    public void success() {
        Entity e=getTouchEntity();
        if(e!=null && e instanceof EntityLivingBase){
            float d=3.0f;
            int lv=getLvDiff();
            if(lv>0) d+=0.5*(lv/5);
            if(!isSpelled) d/=2.0f;
            e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, (EntityLivingBase)e), d);
        }
    }

    @Override
    public void failure() {
        sanity(1,4);
    }
}
