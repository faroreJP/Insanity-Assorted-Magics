package jp.plusplus.fbs.storage;

import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public interface IMealDevice {
    public ItemStack getFragment();
    public void setFragment(ItemStack f);
    public boolean hasFragment();
}
