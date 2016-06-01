package jp.plusplus.fbs.pottery.usable;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotteryUnbreakable extends PotteryKeep {
    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.unbreakable";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 5.0f;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player, ItemStack pottery, int index, ItemStack itemStack){
        return !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index);
    }

    @Override
    public void onCrash(EntityPlayer player, ItemStack pottery){}
}
