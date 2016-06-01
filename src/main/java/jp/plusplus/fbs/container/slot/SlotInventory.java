package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.IAlchemyMaterial;
import jp.plusplus.fbs.alchemy.IAlchemyProduct;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemStaff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class SlotInventory extends Slot {
    private int type;//0 book, 1 charm,book, 2 inv, 3 alchemy
    public SlotInventory(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, int type) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.type=type;
    }


    public boolean isItemValid(ItemStack p_75214_1_) {
        if (type == 0) return p_75214_1_.getItem() == ItemCore.bookSorcery;
        if (type == 1){
            ArrayList<ItemStack> list=OreDictionary.getOres("fbs.charm");
            for(ItemStack item : list){
                if(OreDictionary.itemMatches(item, p_75214_1_, false)) return true;
            }
            return false;
        }
        if(type==3){
            Item it=p_75214_1_.getItem();
            return it instanceof IAlchemyMaterial || it instanceof IAlchemyProduct || AlchemyRegistry.isBasketItem(p_75214_1_);
        }
        return true;
    }

    @Override
    public boolean canTakeStack(EntityPlayer p_82869_1_) {
        return !(getHasStack() && getStack().getItem() instanceof ItemStaff);
    }
}
