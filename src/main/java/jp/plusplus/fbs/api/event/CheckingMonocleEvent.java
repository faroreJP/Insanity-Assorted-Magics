package jp.plusplus.fbs.api.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by plusplus_F on 2016/03/06.
 *
 * プレイヤーがモノクルを持っているかの判定イベント
 */
public class CheckingMonocleEvent extends PlayerEvent {
    private boolean has;
    private ItemStack monocle;

    public CheckingMonocleEvent(EntityPlayer player, ItemStack has) {
        super(player);
        this.has=(has!=null);
        monocle=has;
    }

    public boolean hasMonocle(){
        return has;
    }
    public ItemStack getMonocle(){
        return monocle;
    }

    public void setMonocle(ItemStack itemStack){
        if(itemStack!=null){
            has=true;
            monocle=itemStack;
        }
    }
}
