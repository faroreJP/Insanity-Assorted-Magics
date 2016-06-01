package jp.plusplus.fbs.container.slot;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.IAlchemyMaterial;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.item.ItemStoneSpirit;
import jp.plusplus.fbs.spirit.SpiritManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import shift.mceconomy2.api.MCEconomyAPI;

/**
 * Created by plusplus_F on 2015/11/04.
 */
public class SlotContract extends Slot{
    protected int type;

    public SlotContract(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, int type) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.type=type;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if(type==0){
            //武器
            return SpiritManager.isTool(itemStack.getItem());
        }
        else if(type==1){
            //精霊石
            return itemStack.getItem() instanceof ItemStoneSpirit;
        }
        else if(type==2){
            //書物
            if(!(itemStack.getItem() instanceof ItemBookSorcery)) return false;
            Registry.BookData bd=Registry.GetBookDataFromItemStack(itemStack);
            if(bd==null) return false;
            return bd.title.equals("fbs.contract");
        }

        return true;
    }


}
