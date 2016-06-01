package jp.plusplus.fbs.api.event;

import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class PlayerUseMagicEvent extends PlayerEvent {
    public MagicBase magic;
    public ItemStack[] books;

    public PlayerUseMagicEvent(EntityPlayer player, MagicBase mb, ItemStack[] b) {
        super(player);
        magic=mb;
        books=b;
    }

    @Override
    public boolean isCancelable(){
        return true;
    }

    public static class Pre extends PlayerUseMagicEvent{
        public Pre(EntityPlayer player, MagicBase mb, ItemStack[] b) {
            super(player, mb, b);
        }
    }
    public static class Post extends PlayerUseMagicEvent{
        public Post(EntityPlayer player, MagicBase mb, ItemStack[] b) {
            super(player, mb, b);
        }
    }
}
