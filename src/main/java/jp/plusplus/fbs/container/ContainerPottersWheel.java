package jp.plusplus.fbs.container;

import jp.plusplus.fbs.container.slot.SlotCraftingPottery;
import jp.plusplus.fbs.pottery.TileEntityPottersWheel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by pluslus_F on 2015/08/29.
 */
public class ContainerPottersWheel extends Container {
    protected TileEntityPottersWheel entity;

    public ContainerPottersWheel(EntityPlayer player, TileEntityPottersWheel tileEntity) {
        this.entity = tileEntity;

        //inventory's inventory
        for(int i=0;i<25;i++) {
            this.addSlotToContainer(new Slot(this.entity, i, 8 + (i % 5) * 18, 17 + (i / 5) * 18));
        }
        this.addSlotToContainer(new SlotCraftingPottery(player, this.entity, 25, 140, 54));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 120 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 178));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return entity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            if(par2==25){
                if(!this.mergeItemStack(stack, 26+24, 26+24+9, true)){
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            }
            else if(par2>=26){
                if(par2<26+24){
                    if(!this.mergeItemStack(stack, 26+24, 26+24+9, false)){
                        return null;
                    }
                }
                else{
                    if(!this.mergeItemStack(stack, 26, 26+24, false)){
                        return null;
                    }
                }
            }

            if (stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (stack.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return itemStack;
    }
}
