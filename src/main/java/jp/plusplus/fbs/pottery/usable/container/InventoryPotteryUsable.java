package jp.plusplus.fbs.pottery.usable.container;

import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.pottery.ItemBlockPottery;
import jp.plusplus.fbs.pottery.PotteryRegistry;
import jp.plusplus.fbs.pottery.usable.PotteryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by plusplus_F on 2016/03/30.
 * インベントリ持ち魔法の壺のインベントリ
 */
public class InventoryPotteryUsable implements IInventory{
    public EntityPlayer player;
    public ItemStack potteryStack;
    public int potteryStackIndex;
    public PotteryBase potteryEffect;
    public IPottery pottery;

    protected ItemStack[] itemStacks;
    protected int inventorySize;

    public InventoryPotteryUsable(EntityPlayer player){
        this.player=player;
        this.potteryStack=player.getCurrentEquippedItem();
        this.potteryStackIndex=player.inventory.currentItem;
        this.potteryEffect= PotteryRegistry.getPotteryEffect(ItemBlockPottery.getId(potteryStack));
        this.pottery=(IPottery)((ItemBlock)potteryStack.getItem()).field_150939_a;

        switch (pottery.getSize(potteryStack.getTagCompound())){
            case MEDIUM: inventorySize=9*2; break;
            case LARGE: inventorySize=9*3; break;
            default: inventorySize=9*1; break;
        }
        itemStacks=new ItemStack[inventorySize];
    }

    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return itemStacks[p_70301_1_];
    }

    @Override
    public ItemStack decrStackSize(int i, int size) {
        if (this.itemStacks[i] != null) {
            ItemStack itemstack;

            if (this.itemStacks[i].stackSize <= size) {
                itemstack = this.itemStacks[i];
                this.itemStacks[i] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.itemStacks[i].splitStack(size);

                if (this.itemStacks[i].stackSize == 0) {
                    this.itemStacks[i] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.itemStacks[i] != null) {
            ItemStack itemstack = this.itemStacks[i];
            this.itemStacks[i] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack item) {
        this.itemStacks[i] = item;

        if (item != null && item.stackSize > this.getInventoryStackLimit()) {
            item.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return potteryStack.getDisplayName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {
        if (!potteryStack.hasTagCompound()) {
            potteryStack.setTagCompound(new NBTTagCompound());
            potteryStack.getTagCompound().setTag(PotteryBase.ITEM_STACKS, new NBTTagList());
        }
        else if(!potteryStack.getTagCompound().hasKey(PotteryBase.ITEM_STACKS)){
            potteryStack.getTagCompound().setTag(PotteryBase.ITEM_STACKS, new NBTTagList());
        }

        NBTTagList tags = (NBTTagList) potteryStack.getTagCompound().getTag(PotteryBase.ITEM_STACKS);
        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound tagCompound = tags.getCompoundTagAt(i);
            int slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < itemStacks.length) {
                itemStacks[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        //インベントリが開くときの処理
        for(int i=0;i<inventorySize;i++){
            itemStacks[i]=potteryEffect.onInventoryOpening(player, potteryStack, i, itemStacks[i]);
        }
    }

    @Override
    public void closeInventory() {
        //インベントリが閉じるときのの処理
        for(int i=0;i<inventorySize;i++){
            itemStacks[i]=potteryEffect.onInventoryClosing(player, potteryStack, i, itemStacks[i]);
        }

        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i] != null) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(compound);
                tagList.appendTag(compound);
            }
        }
        ItemStack result = new ItemStack(potteryStack.getItem(), 1, potteryStack.getItemDamage());
        result.setTagCompound((NBTTagCompound)potteryStack.getTagCompound().copy());
        result.getTagCompound().setTag(PotteryBase.ITEM_STACKS, tagList);

        player.inventory.mainInventory[potteryStackIndex] = result;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return potteryEffect.isItemValid(player, potteryStack, p_94041_1_, p_94041_2_);
    }
}
