package jp.plusplus.fbs.trouble;

import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/17.
 */
public class TroubleHunger extends TroubleBase {
    public TroubleHunger() {
        super(1);
    }

    @Override
    public void done(World world, EntityPlayer player, Registry.BookData bd) {
        int amount=5+(35*bd.lv/20);

        FoodStats fs=player.getFoodStats();
        if(fs.getFoodLevel()>0) fs.addExhaustion(amount);
    }

    @Override
    public String getMessage(){ return super.getMessage()+".hunger"; }
}
