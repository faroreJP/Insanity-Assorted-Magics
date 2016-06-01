package jp.plusplus.fbs.pottery.usable.container;

import jp.plusplus.fbs.container.slot.SlotShowOnly;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class ContainerPotteryUsableBase extends Container {
    public ItemStack currentItem;
    public EntityPlayer player;
    public InventoryPotteryUsable inventory;
    public int inventoryRows;

    public ContainerPotteryUsableBase(EntityPlayer player) {
        this.player = player;
        currentItem = player.getCurrentEquippedItem();
        inventory = new InventoryPotteryUsable(player);

        //壺のスロット
        inventoryRows = (inventory.getSizeInventory() + 8) / 9;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            addSlotToContainer(new SlotPotteryUsable(inventory, i, 8 + (i % 9) * 18, 18 + (i / 9) * 18));
        }

        //player slots
        int y = (inventoryRows - 4) * 18;
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + y));
            }
        }
        for (int j = 0; j < 9; ++j) {
            if (j == inventory.potteryStackIndex) this.addSlotToContainer(new SlotShowOnly(player.inventory, j, 8 + j * 18, 161 + y));
            else this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 161 + y));
        }

        inventory.openInventory();
    }

    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        inventory.closeInventory();
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player1, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slot.getStack()==null || slotIndex==inventory.potteryStackIndex){
                //スロットがNULLだったり開いてる壺なら何もしない
                return null;
            }
            else if (slotIndex < this.inventory.getSizeInventory()) {
                //壺のインベントリ内であれば他所に移す
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if(!inventory.isItemValidForSlot(slotIndex, itemstack)){
                //プレイヤーのインベントリにあり、それが壺のインベントリに適さない場合何もしない
                return null;
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false)) {
                //壺のインベントリに移せるか試してる
                return null;
            }

            //アイテムの消去と更新処理
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
