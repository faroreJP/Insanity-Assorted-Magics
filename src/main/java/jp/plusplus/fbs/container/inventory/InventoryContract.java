package jp.plusplus.fbs.container.inventory;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/04.
 */
public class InventoryContract implements IInventory {
    public EntityPlayer player;
    public ItemStack[] itemStacks=new ItemStack[4];
    protected Random rand=new Random();

    public InventoryContract(EntityPlayer player){
        this.player=player;
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return itemStacks[p_70301_1_];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.itemStacks[p_70298_1_] != null) {
            ItemStack itemstack;

            if (this.itemStacks[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.itemStacks[p_70298_1_];
                this.itemStacks[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.itemStacks[p_70298_1_].splitStack(p_70298_2_);

                if (this.itemStacks[p_70298_1_].stackSize == 0) {
                    this.itemStacks[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.itemStacks[p_70304_1_] != null) {
            ItemStack itemstack = this.itemStacks[p_70304_1_];
            this.itemStacks[p_70304_1_] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.itemStacks[p_70299_1_] = p_70299_2_;
        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return StatCollector.translateToLocal("book.fbs.contract.title");
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

    }

    @Override
    public void closeInventory() {
        if(player.worldObj.isRemote) return;

        for(int i=0;i<itemStacks.length;i++){
            ItemStack itemstack = this.getStackInSlot(i);

            if (itemstack != null){
                float f = this.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0){
                    int k1 = this.rand.nextInt(21) + 10;

                    if (k1 > itemstack.stackSize){
                        k1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= k1;
                    double x=player.posX+f;
                    double y=player.posY+f1+player.getEyeHeight();
                    double z=player.posZ+f2;
                    EntityItem entityitem = new EntityItem(player.worldObj, x, y, z, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                    if (itemstack.hasTagCompound()){
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                    player.worldObj.spawnEntityInWorld(entityitem);
                }
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
