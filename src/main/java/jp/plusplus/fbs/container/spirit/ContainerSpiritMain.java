package jp.plusplus.fbs.container.spirit;

import jp.plusplus.fbs.container.slot.SlotInventory;
import jp.plusplus.fbs.container.slot.SlotShowOnly;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SpiritManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/11/15.
 */
public class ContainerSpiritMain extends Container{
    public EntityPlayer player;
    public int type;

    public ContainerSpiritMain(EntityPlayer player, int type){
        this.player=player;
        this.type=type;

        for (int j = 0; j < 9; ++j) {
            ItemStack is=player.inventory.getStackInSlot(j);
            if(is!=null && is.getItem() instanceof ISpiritTool) this.addSlotToContainer(new SlotShowOnly(player.inventory, j, 8 + j * 18, 142));
            else this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        return itemstack;
    }
}
