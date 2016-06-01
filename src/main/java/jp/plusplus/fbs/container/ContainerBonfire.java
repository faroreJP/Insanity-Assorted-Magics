package jp.plusplus.fbs.container;

import jp.plusplus.fbs.container.inventory.InventoryBonfire;
import jp.plusplus.fbs.container.slot.SlotBonfire;
import jp.plusplus.fbs.container.slot.SlotInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/10/19.
 */
public class ContainerBonfire extends Container {
    public InventoryBonfire inventory;

    public ContainerBonfire(int x, int y, int z, EntityPlayer player){
        inventory=new InventoryBonfire(x,y,z,player);
        inventory.openInventory();

        addSlotToContainer(new SlotBonfire(inventory, 0, 80, 21, 0));
        for(int i=0;i<3;i++){
            addSlotToContainer(new SlotBonfire(inventory, i+1, 62+18*i, 43, 1));
        }

        //player's inv
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotInventory(player.inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18, 2));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new SlotInventory(player.inventory, j, 8 + j * 18, 142, 2));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return inventory.isUseableByPlayer(p_75145_1_);
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.inventory.closeInventory();
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        return itemstack;
    }

}
