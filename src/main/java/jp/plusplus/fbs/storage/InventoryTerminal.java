package jp.plusplus.fbs.storage;

import jp.plusplus.fbs.FBS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class InventoryTerminal implements IInventory {
    public ItemStack[] allItem;
    public ItemStack[] itemStacks=new ItemStack[54];
    public TileEntityMeal meal;
    public float scroll;

    public boolean dontUpdate;

    public InventoryTerminal(TileEntityMeal meal){
        this.meal=meal;
    }

    public void updateItemList(){
        if(!dontUpdate){
            allItem=meal.getAllItemStacks();
            if(allItem.length<=54){
                for(int i=0;i<itemStacks.length;i++) itemStacks[i]=null;
                for(int i=0;i<allItem.length;i++) itemStacks[i]=allItem[i];
            }
            else{
                //スクロールに合わせて変更
                int shift=(int)(((allItem.length-54)/9+1)*scroll);
                for(int i=0;i<54;i++){
                    int index=i+shift*9;
                    if(index<allItem.length) itemStacks[i]=allItem[index];
                    else itemStacks[i]=null;
                }
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 54;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return itemStacks[p_70301_1_];
    }

    @Override
    public ItemStack decrStackSize(int index, int stackSize) {
        if (this.itemStacks[index] != null) {
            ItemStack itemstack;

            if (this.itemStacks[index].stackSize <= stackSize) {
                itemstack = this.itemStacks[index].copy();
                this.itemStacks[index] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.itemStacks[index].splitStack(stackSize);

                if (this.itemStacks[index].stackSize == 0) {
                    this.itemStacks[index] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        //meal.insertItemStack(p_70299_2_);
        itemStacks[p_70299_1_]=p_70299_2_;
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return "fbs.terminal";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void markDirty() {
        updateItemList();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {
        updateItemList();
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
