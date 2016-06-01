package jp.plusplus.fbs.storage;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class TileEntityMealOutletSingle extends TileEntityMealInlet {
    public ItemStack requirement;

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        if(par1NBTTagCompound.hasKey("Requirement")) requirement=ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("Requirement"));
        else requirement=null;
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        if(requirement!=null) par1NBTTagCompound.setTag("Requirement", requirement.writeToNBT(new NBTTagCompound()));
    }

    public boolean hasRequirement(){
        return requirement!=null;
    }
    public ItemStack getRequirement(){
        return requirement;
    }
    public void setRequirement(ItemStack req){
        requirement=req;
    }

    protected ItemStack getItemStack(){
        if(!hasRequirement() && !hasFragment()) return null;

        TileEntityMeal tem=ItemMealFragment.getTileEntity(getFragment());
        return tem.exportItemStack(requirement, requirement.stackSize);
    }

    @Override
    public void updateEntity(){
        if(!worldObj.isRemote && requirement!=null && worldObj.getWorldTime()%10==0){
            TileEntityMeal tem=ItemMealFragment.getTileEntity(fragment);
            if(tem==null) return;

            ForgeDirection dir=ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
            TileEntity te=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);

            if(te instanceof ISidedInventory){
                ISidedInventory inv=(ISidedInventory)te;
                int side=dir.ordinal()^1;
                int[] index=inv.getAccessibleSlotsFromSide(side);

                for(int i=0;i<index.length;i++){
                    if(((ISidedInventory) te).canInsertItem(index[i], requirement, side)){
                        ItemStack itemStack=inv.getStackInSlot(index[i]);
                        if((itemStack==null || (itemStack.isItemEqual(requirement) && ItemStack.areItemStackTagsEqual(itemStack, requirement) && itemStack.stackSize+1<=itemStack.getMaxStackSize()))){
                            ItemStack p=tem.exportItemStack(requirement, 1);
                            if(p==null) return;

                            if(itemStack==null) inv.setInventorySlotContents(i, p.copy());
                            else itemStack.stackSize++;

                            te.markDirty();
                            return;
                        }
                    }
                }
            }
            else if(te instanceof TileEntityChest){
                TileEntityChest[] c=new TileEntityChest[2];
                c[0]=(TileEntityChest)te;

                if(c[0].adjacentChestXPos!=null) c[1]=c[0].adjacentChestXPos;
                if(c[0].adjacentChestXNeg!=null) c[1]=c[0].adjacentChestXNeg;
                if(c[0].adjacentChestZPos!=null) c[1]=c[0].adjacentChestZPos;
                if(c[0].adjacentChestZNeg!=null) c[1]=c[0].adjacentChestZNeg;

                for(TileEntityChest inv : c){
                    if(inv==null) break;

                    int size=inv.getSizeInventory();
                    for(int i=0;i<size;i++){
                        ItemStack itemStack=inv.getStackInSlot(i);
                        if((itemStack==null || (itemStack.isItemEqual(requirement) && ItemStack.areItemStackTagsEqual(itemStack, requirement) && itemStack.stackSize+1<=itemStack.getMaxStackSize()))){
                            ItemStack p=tem.exportItemStack(requirement, 1);
                            if(p==null) return;

                            if(itemStack==null) inv.setInventorySlotContents(i, p.copy());
                            else itemStack.stackSize++;

                            te.markDirty();
                            return;
                        }
                    }
                }
            }
            else if(te instanceof IInventory){
                IInventory inv=(IInventory)te;
                int size=inv.getSizeInventory();
                for(int i=0;i<size;i++){
                    ItemStack itemStack=inv.getStackInSlot(i);
                    if((itemStack==null || (itemStack.isItemEqual(requirement) && ItemStack.areItemStackTagsEqual(itemStack, requirement) && itemStack.stackSize+1<=itemStack.getMaxStackSize()))){
                        ItemStack p=tem.exportItemStack(requirement, 1);
                        if(p==null) return;

                        if(itemStack==null) inv.setInventorySlotContents(i, p.copy());
                        else itemStack.stackSize++;

                        te.markDirty();
                        return;
                    }
                }
            }
        }
    }
}
