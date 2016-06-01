package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.inventory.InventoryMagic;
import jp.plusplus.fbs.mod.ForSS2;
import net.minecraft.item.ItemStack;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class SlotMagicCopy extends SlotMagic {
    public SlotMagicCopy(InventoryMagic p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return p_75214_1_.getMaxStackSize()>1;
    }
    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }
}
