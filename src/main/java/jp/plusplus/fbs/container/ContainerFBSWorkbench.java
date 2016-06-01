package jp.plusplus.fbs.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.container.slot.SlotCrafting;
import jp.plusplus.fbs.container.slot.SlotTakeOnly;
import jp.plusplus.fbs.tileentity.TileEntityFBSWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class ContainerFBSWorkbench extends Container {
    protected TileEntityFBSWorkbench entity;
    public int lastAmount;

    public ContainerFBSWorkbench(EntityPlayer player, TileEntityFBSWorkbench tileEntity) {
        this.entity = tileEntity;

        //inventory's inventory
        for(int i=0;i<9;i++) {
            this.addSlotToContainer(new Slot(this.entity, i, 30 + (i % 3) * 18, 17 + (i / 3) * 18));
        }
        this.addSlotToContainer(new SlotCrafting(player, this.entity, 9, 124, 35));

        this.addSlotToContainer(new Slot(this.entity, 10, 66, 71));
        this.addSlotToContainer(new SlotTakeOnly(this.entity, 11, 30, 71));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 161));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluidAmount());
    }
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastAmount != entity.tank.getFluidAmount()) {
                icrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluidAmount());
            }
        }
        lastAmount = entity.tank.getFluidAmount();
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            for (int i = 0; i < this.crafters.size(); i++) {
                ICrafting icrafting = (ICrafting) this.crafters.get(i);
                if (lastAmount != entity.tank.getFluidAmount()) {
                    icrafting.sendProgressBarUpdate(this, 0, entity.tank.getFluidAmount());
                }
            }
            if (par2 > 0) {
                if (entity.tank.getFluid() == null) entity.tank.setFluid(new FluidStack(BlockCore.mana, par2));
                else entity.tank.setAmount(par2);
            } else {
                entity.tank.setFluid(null);
            }
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

            if (par2 >= 0 && par2 <= 11) {
                if(par2==9){
                    if(entity.product!=null){
                        entity.tank.drain(entity.product.mana*stack.stackSize, true);
                    }
                }
                if (!this.mergeItemStack(stack, 12, 48, true)) {
                    return null;
                }
                slot.onSlotChange(stack, itemStack);
            } else {
                if (entity.isItemValidForSlot(10, stack)) {
                    if (!this.mergeItemStack(stack, 10, 11, false)) {
                        return null;
                    }
                } else if (par2 >= 12 && par2 < 39) {
                    if (!this.mergeItemStack(stack, 39, 48, false)) {
                        return null;
                    }
                } else if (par2 >= 39 && par2 < 48 && !this.mergeItemStack(stack, 12, 39, false)) {
                    return null;
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
