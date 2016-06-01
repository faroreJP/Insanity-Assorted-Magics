package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.container.inventory.InventoryMagic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Createdby pluslus_Fon 2015/06/18.
 */
public class SlotMagicTakeOnly extends SlotMagic{
    public SlotMagicTakeOnly(InventoryMagic p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean isItemValid(ItemStack item){
        return false;
    }
}
