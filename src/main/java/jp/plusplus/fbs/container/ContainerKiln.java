package jp.plusplus.fbs.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.container.slot.SlotTakeOnly;
import jp.plusplus.fbs.pottery.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/08/27.
 */
public class ContainerKiln extends Container {
    public int lastFurnaceBurn;
    public int lastItemBurn;
    public int[] lastProgress=new int[9];
    public TileEntityKiln entity;


    public ContainerKiln(EntityPlayer player, TileEntityKiln tileEntity) {
        this.entity = tileEntity;

        //inventory's inventory
        for(int i=0;i<9;i++) addSlotToContainer(new Slot(entity, i, 8+i*18, 58));
        this.addSlotToContainer(new Slot(this.entity, 9, 80, 93));
        for(int i=0;i<9;i++) addSlotToContainer(new SlotTakeOnly(entity, 10+i, 8+i*18, 23, 1));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 115 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 173));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return entity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, entity.furnaceBurnTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, entity.currentItemBurnTime);
        for(int i=0;i<9;i++) par1ICrafting.sendProgressBarUpdate(this, 2+i, entity.progress[i]);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            if(lastFurnaceBurn!=entity.furnaceBurnTime) icrafting.sendProgressBarUpdate(this, 0, entity.furnaceBurnTime);
            if(lastItemBurn!=entity.currentItemBurnTime) icrafting.sendProgressBarUpdate(this, 1, entity.currentItemBurnTime);
            for(int k=0;k<9;k++){
                if (lastProgress[k] != entity.progress[k]) icrafting.sendProgressBarUpdate(this, 2+k, entity.progress[k]);
            }
        }
        lastFurnaceBurn=entity.furnaceBurnTime;
        lastItemBurn=entity.currentItemBurnTime;
        for(int k=0;k<9;k++) lastProgress[k] = entity.progress[k];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            for (int i = 0; i < this.crafters.size(); i++) {
                ICrafting icrafting = (ICrafting) this.crafters.get(i);
                if (lastFurnaceBurn != entity.furnaceBurnTime) {
                    icrafting.sendProgressBarUpdate(this, 0, entity.furnaceBurnTime);
                }
            }
            entity.furnaceBurnTime=par2;
        }
        else if(par1==1){
            for (int i = 0; i < this.crafters.size(); i++) {
                ICrafting icrafting = (ICrafting) this.crafters.get(i);
                if (lastItemBurn != entity.currentItemBurnTime) {
                    icrafting.sendProgressBarUpdate(this, 0, entity.currentItemBurnTime);
                }
            }
            entity.currentItemBurnTime=par2;
        }
        else {
            int k = par1 - 2;
            if (k < 0 || k >= 9) return;

            for (int i = 0; i < this.crafters.size(); i++) {
                ICrafting icrafting = (ICrafting) this.crafters.get(i);
                if (lastProgress != entity.progress) {
                    icrafting.sendProgressBarUpdate(this, 1, entity.progress[k]);
                }
            }
            entity.progress[k] = (short) par2;
        }
    }


    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        return itemStack;
    }
}
