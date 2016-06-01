package jp.plusplus.fbs.pottery.usable;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import jp.plusplus.fbs.api.IPottery;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotteryEnchantment extends PotteryKeep {
    private Random rand=new Random();

    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.enchant";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 3.5f;
    }

    @Override
    public ItemStack onInventoryClosing(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        NBTTagCompound nbt=pottery.getTagCompound();

        if(itemStack!=null && !nbt.getBoolean(CHANGED_INDEXES + index)){
            if(!itemStack.isItemEnchanted() && itemStack.isItemEnchantable()){
                IPottery ip=(IPottery)((ItemBlock)pottery.getItem()).field_150939_a;

                int lv;
                switch (ip.getGrade(nbt)){
                    case BAD: lv=5; break;
                    case GOOD: lv=20; break;
                    case GREAT: lv=30; break;
                    case SOULFUL: lv=40; break;
                    default: lv=10; break;
                }
                return EnchantmentHelper.addRandomEnchantment(rand, itemStack, lv);
            }
        }
        return itemStack;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player, ItemStack pottery, int index, ItemStack itemStack){
        return !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index);
    }
}
