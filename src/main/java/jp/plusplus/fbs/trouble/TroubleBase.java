package jp.plusplus.fbs.trouble;

import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/17.
 * 解読に失敗したときとかのいたずら
 */
public abstract class TroubleBase {
    /**
     * 出現し始める適性魔術レベル
     */
    protected int minLv;

    public TroubleBase(int min){
        minLv=min;
    }

    public int getMinimumMagicLv(){ return minLv; }
    public abstract void done(World world, EntityPlayer player, Registry.BookData bd);
    public String getMessage(){ return "info.fbs.book.trouble"; };
}
