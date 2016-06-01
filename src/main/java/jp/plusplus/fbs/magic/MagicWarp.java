package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.exprop.SanityManager;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/10/23.
 */
public class MagicWarp extends MagicBase {
    @Override
    public boolean checkSuccess() {
        return true;
    }

    @Override
    public String getMagicCircleName(){
        return "fbs.warp";
        //return  "null";

    }

    @Override
    public void success() {
        int x=MathHelper.floor_double(player.posX);
        int y=MathHelper.floor_double(player.posY);
        int z=MathHelper.floor_double(player.posZ);
        SanityManager.addDestination(player, world.provider.dimensionId, x, y, z);
        player.openGui(FBS.instance, FBS.GUI_MAGIC_WARP_ID, world, x, y, z);
    }

    @Override
    public void failure() {

    }
}
