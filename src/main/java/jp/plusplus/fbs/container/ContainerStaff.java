package jp.plusplus.fbs.container;

import jp.plusplus.fbs.container.slot.SlotInventory;
import jp.plusplus.fbs.container.inventory.InventoryStaff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class ContainerStaff extends Container {
    public InventoryStaff inventory;

    public ContainerStaff(InventoryPlayer invP) {
        inventory = new InventoryStaff(invP);
        inventory.openInventory();

        //player's inv
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotInventory(invP, k + j * 9 + 9, 8 + k * 18, 84 + j * 18, 2));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new SlotInventory(invP, j, 8 + j * 18, 142, 2));
        }

        //staff
        if(inventory.bookNum==1) addSlotToContainer(new SlotInventory(inventory, 0, 80, 25, 0));
        if(inventory.bookNum==2){
            addSlotToContainer(new SlotInventory(inventory, 0, 67, 25, 0));
            addSlotToContainer(new SlotInventory(inventory, 1, 93, 25, 0));
        }
        for (int i = 0; i < inventory.gemNum; i++)
            addSlotToContainer(new SlotInventory(inventory, inventory.bookNum + i, 44 + i * 18, 53, 1));
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        inventory.closeInventory();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(p_82846_2_);
        return itemstack;
    }
}