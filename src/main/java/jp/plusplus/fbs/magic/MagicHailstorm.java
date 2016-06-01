package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.potion.PotionEffect;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public class MagicHailstorm extends MagicBase {
    @Override
    public boolean checkSuccess() {
        return isSpelled?rand.nextFloat()<0.5f+0.1f*Math.max(getLvDiff(), 0):true;
    }

    @Override
    public void success() {
        if(!isSpelled && !usingStaff){
            player.addPotionEffect(new PotionEffect(Registry.potionHailstorm.getId(), 2, 1));
        }
        else{
            int diff=Math.max(getLvDiff(), 0);
            int a=diff/4;
            int d=30+10*diff;
            player.addPotionEffect(new PotionEffect(Registry.potionHailstorm.getId(), 20*d, a));
        }
    }

    @Override
    public void failure() {
        sanity(2, 8);
    }
}
