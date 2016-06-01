package jp.plusplus.fbs.pottery.usable;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public class PotteryFurnace extends PotteryKeep {
    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.furnace";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 1.5f;
    }

    @Override
    public ItemStack onInventoryClosing(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        if(itemStack!=null && !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index)){
            ItemStack ret=FurnaceRecipes.smelting().getSmeltingResult(itemStack);
            if(ret!=null){
                ItemStack t=ret.copy();
                t.stackSize=Math.min(itemStack.stackSize, t.getMaxStackSize());
                return t;
            }
        }
        return itemStack;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player, ItemStack pottery, int index, ItemStack itemStack){
        return !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index);
    }
}
