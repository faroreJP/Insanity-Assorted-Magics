package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.IAlchemyMaterial;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shift.mceconomy2.api.MCEconomyAPI;

/**
 * Created by plusplus_F on 2015/10/31.
 */
public class SlotBonfire extends Slot{
    protected int type;

    public SlotBonfire(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, int type) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.type=type;
    }

    public boolean isItemValid(ItemStack itemStack) {
        if(type==0){
            //焼くもの
            if(itemStack.isItemStackDamageable()){
                ItemStack it = itemStack.copy();
                it.setItemDamage(0);
                return MCEconomyAPI.getPurchase(it) >= 0 || itemStack.isItemEnchanted();
            }
            else {
                return MCEconomyAPI.getPurchase(itemStack)>=0 || itemStack.isItemEnchanted() || itemStack.getItem()== Items.enchanted_book;
            }
        }
        if(type==1){
            //ハーブ
            Item item=itemStack.getItem();
            if(!(item instanceof IAlchemyMaterial)) return false;
            return AlchemyRegistry.IsMatching("herb", itemStack);
        }

        return true;
    }


}
