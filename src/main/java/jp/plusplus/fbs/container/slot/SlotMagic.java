package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.container.inventory.InventoryMagic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class SlotMagic extends Slot {
    public SlotMagic(InventoryMagic p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public void onSlotChanged(){
        super.onSlotChanged();
        ((InventoryMagic)inventory).onInventoryChanged(getSlotIndex());
    }
}
