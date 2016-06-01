package jp.plusplus.fbs.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.container.slot.SlotInventory;
import jp.plusplus.fbs.container.inventory.InventoryMagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class ContainerMagic extends Container {
    public InventoryMagic inventory;
    private int lastProgress;

    public ContainerMagic(InventoryPlayer invP, InventoryMagic invM){
        inventory=invM;
        invM.openInventory();

        //player's inv
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotInventory(invP, k + j * 9 + 9, 8 + k * 18, 84 + j * 18, 2));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new SlotInventory(invP, j, 8 + j * 18, 142, 2));
        }

        invM.addSlotToContainer(this);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
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


    public Slot addSlotToContainer(Slot p_75146_1_){
        return super.addSlotToContainer(p_75146_1_);
    }


    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        if(inventory.magicCore!=null){
            par1ICrafting.sendProgressBarUpdate(this, 0, inventory.magicCore.progress);
        }

    }
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if(inventory.magicCore==null) return;

        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if (lastProgress != inventory.magicCore.progress) {
                icrafting.sendProgressBarUpdate(this, 0, inventory.magicCore.progress);
            }
        }
        lastProgress = inventory.magicCore.progress;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if(par1==0) {
            for (int i = 0; i < this.crafters.size(); i++) {
                ICrafting icrafting = (ICrafting) this.crafters.get(i);
                if (inventory.magicCore!=null && lastProgress != inventory.magicCore.progress) {
                    icrafting.sendProgressBarUpdate(this, 0, inventory.magicCore.progress);
                }
            }
            if(inventory.magicCore!=null){
                inventory.magicCore.progress=par2;
            }
        }
    }
}
