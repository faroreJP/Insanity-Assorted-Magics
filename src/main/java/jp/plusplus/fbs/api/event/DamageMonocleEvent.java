package jp.plusplus.fbs.api.event;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/03/06.
 *
 * モノクルの耐久度を消費するときのイベント
 */
public class DamageMonocleEvent extends PlayerEvent {
    private ItemStack monocle;

    public DamageMonocleEvent(EntityPlayer player, ItemStack monocle) {
        super(player);
        this.monocle=monocle;
    }

    public ItemStack getMonocle(){
        return monocle;
    }
}
