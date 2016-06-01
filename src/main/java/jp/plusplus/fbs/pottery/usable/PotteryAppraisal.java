package jp.plusplus.fbs.pottery.usable;

import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nullable;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public class PotteryAppraisal extends PotteryKeep {
    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.appraisal";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 1.5f;
    }

    @Override
    public ItemStack onInventoryClosing(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        if(itemStack!=null && !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index) && AlchemyRegistry.CanAppraisal(itemStack)){
            ItemStack ret=AlchemyRegistry.GetRandomAppraisal(itemStack);
            if(ret!=null){
                ret.stackSize=Math.min(itemStack.stackSize, ret.getMaxStackSize());
                return ret;
            }
        }
        return itemStack;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player, ItemStack pottery, int index, ItemStack itemStack){
        return !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index);
    }
}
