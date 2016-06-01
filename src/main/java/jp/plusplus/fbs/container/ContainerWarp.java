package jp.plusplus.fbs.container;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.item.ItemBookNoDecoded;
import jp.plusplus.fbs.item.ItemBookSorcery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/10/22.
 */
public class ContainerWarp extends Container {
    public EntityPlayer player;
    public boolean close;

    public ContainerWarp(EntityPlayer ep){
        player=ep;
        close=false;
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        if(close) return false;

        ItemStack item=p_75145_1_.getCurrentEquippedItem();
        if(item==null) return false;
        if(!(item.getItem() instanceof ItemBookSorcery)) return false;

        Registry.BookData bd=Registry.GetBookDataFromItemStack(item);
        if(bd==null) return false;

        if(!bd.title.equals("fbs.warp")) return false;

        return ItemBookSorcery.getMagicMaxUse(item)>0;
    }
}
