package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.inventory.InventoryMagic;
import jp.plusplus.fbs.mod.ForSS2;
import net.minecraft.item.ItemStack;

/**
 * Created by pluslus_F on 2015/02/28.
 */
public class SlotMagicTimeTrace extends SlotMagic {
    public SlotMagicTimeTrace(InventoryMagic p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        if(FBS.cooperatesSS2 && ForSS2.canTimeTrace(p_75214_1_)){
            return true;
        }
        return p_75214_1_.getItem().isItemTool(p_75214_1_);
    }
    @Override
    public int getSlotStackLimit()
    {
        return 64;
    }
}
