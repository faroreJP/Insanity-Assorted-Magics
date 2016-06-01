package jp.plusplus.fbs.pottery.usable;

import jp.plusplus.fbs.api.IPottery;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by plusplus_F on 2016/03/30.
 * 魔法の壺の基底クラス
 */
public abstract class PotteryBase {
    public static final String ITEM_STACKS="ItemStacks";

    public NBTTagCompound getPotteryNBT(ItemStack pottery){
        if(!pottery.hasTagCompound()){
            pottery.setTagCompound(new NBTTagCompound());
        }
        return pottery.getTagCompound();
    }

    /**
     * 壺のUnlocalizedNameを返す<br>
     * ここで返した文字列がそのままローカライズに使用される
     * @return UnlocalizedName
     */
    public abstract String getUnlocalizedName();

    /**
     * 名前の後ろにつく付加的な情報を返す
     * @param pottery 壺のアイテムスタック
     * @return
     */
    public String getNameModifier(ItemStack pottery){
        IPottery ip=(IPottery)((ItemBlock)pottery.getItem()).field_150939_a;

        int slot;
        switch (ip.getSize(pottery.getTagCompound())){
            case SMALL: slot=9; break;
            case LARGE: slot=27; break;
            default: slot=18; break;
        }

        if(pottery.getTagCompound().hasKey(ITEM_STACKS)){
            NBTTagList list=(NBTTagList)pottery.getTagCompound().getTag(ITEM_STACKS);
            slot-=list.tagCount();
        }

        return "["+slot+"]";
    }

    /**
     * 売却値にかかる補正を返す
     * @param pottery 壺のアイテムスタック
     * @return 売却値補正
     */
    public float getPriceScale(ItemStack pottery){
       return 1.f;
    }

    /**
     * 壺が焼かれた時に呼び出される
     * @param pottery 壺のアイテムスタック
     */
    public void onBaked(ItemStack pottery){}

    /**
     * この壺が使用できるか判定する
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     * @return
     */
    public boolean canUse(EntityPlayer player, ItemStack pottery){
        return true;
    }

    /**
     * 壺を使用したときの処理
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     * @return
     */
    public abstract ItemStack onUse(EntityPlayer player, ItemStack pottery);

    /**
     * 壺が破壊されたときの処理
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     */
    public void onCrash(EntityPlayer player, ItemStack pottery){
        NBTTagCompound nbt=getPotteryNBT(pottery);

        if(nbt.hasKey(ITEM_STACKS)){
            NBTTagList list=(NBTTagList)nbt.getTag(ITEM_STACKS);
            for(int i=0;i<list.tagCount();i++){
                NBTTagCompound tag=list.getCompoundTagAt(i);
                ItemStack itemStack=ItemStack.loadItemStackFromNBT(tag);
                player.entityDropItem(itemStack, player.getEyeHeight());
            }
        }
    }

    /**
     * 魔法の壺のインベントリが開く際に全てのスロットに対して行われる処理
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     * @param index スロットインデックス
     * @param itemStack 何か処理するアイテムスタック
     * @return スロットに格納されるItemStack
     */
    public ItemStack onInventoryOpening(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        return itemStack;
    }

    /**
     * 魔法の壺のインベントリが閉じる際に全てのスロットに対して行われる処理
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     * @param index スロットインデックス
     * @param itemStack 何か処理するアイテムスタック
     * @return NBTに書き込まれるItemStack
     */
    public ItemStack onInventoryClosing(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        return itemStack;
    }

    /**
     * 魔法の壺のインベントリがそのアイテムが適しているか
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     * @param index スロットインデックス
     * @param itemStack 判定アイテムスタック
     * @return スロットに入るかどうか
     */
    public boolean isItemValid(EntityPlayer player, ItemStack pottery, int index, ItemStack itemStack){
        return true;
    }

    /**
     * 魔法の壺のインベントリからそのアイテムを取り出せるか
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     * @param index スロットインデックス
     * @param itemStack 判定アイテムスタック
     * @return 取り出せるかどうか
     */
    public boolean canTakeStack(EntityPlayer player, ItemStack pottery, int index,  ItemStack itemStack){
        return true;
    }
}
