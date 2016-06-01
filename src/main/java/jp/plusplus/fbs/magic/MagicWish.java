package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2016/04/01.
 */
public class MagicWish extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(!isSpelled) return false;
        int d=getLvDiff();
        return rand.nextFloat()<(d<0?0.05f:0.1f+0.05f*d);
    }

    @Override
    public void success() {
        player.openGui(FBS.instance, FBS.GUI_WISH_ID, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
    }

    @Override
    public void failure() {
        FBSEntityPropertiesAPI.LoseSanity(player, 3, 8, true);
    }
}
