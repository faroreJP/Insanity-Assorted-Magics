package jp.plusplus.fbs.container.inventory;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.fbs.container.ContainerMagic;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public abstract class InventoryMagic implements IInventory {
    public ItemStack[] itemStacks;
    public TileEntityMagicCore magicCore;
    public Random rand=new Random();
    public EntityPlayer player;

    public InventoryMagic(int size, TileEntityMagicCore te, EntityPlayer player){
        itemStacks=new ItemStack[size];
        magicCore=te;
        this.player=player;
    }

    public abstract void work();
    public abstract void onInventoryChanged(int index);
    public abstract void addSlotToContainer(ContainerMagic cm);

    public int getProgressScaled(int sc){
        if(magicCore==null) return 0;
        if(magicCore.progressMax==0) return 0;
        return sc*magicCore.progress/magicCore.progressMax;
    }

    public abstract ResourceLocation getResource();
    public int getProgressX(){ return 68;}
    public int getProgressY(){ return 36;}

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
    public abstract String getInventoryName();

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
        if(magicCore!=null){
            magicCore.setInventory(this);
            //FMLLog.info("registered inventory");
        }
    }

    @Override
    public void closeInventory() {
        if(magicCore!=null){
            magicCore.removeInventory();
        }

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
