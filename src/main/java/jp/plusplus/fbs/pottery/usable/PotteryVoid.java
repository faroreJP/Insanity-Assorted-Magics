package jp.plusplus.fbs.pottery.usable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotteryVoid extends PotteryKeep {
    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.void";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 0.5f;
    }

    public ItemStack onInventoryClosing(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        return null;
    }

}
