package jp.plusplus.fbs.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.container.ContainerDummy;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

/**
 * Created by pluslus_F on 2015/06/14.
 */
public class TileEntityFBSWorkbench extends TileEntity implements ISidedInventory,IFluidHandler {
    public static final int TANK_CAPACITY=4000;
    public TankFBS tank=new TankFBS(TANK_CAPACITY);

    private static final int[] slotsMaterial=new int[]{0,1,2,3,4,5,6,7,8}, slotsProduct=new int[]{9}, slotsMana=new int[]{10,11};
    public ItemStack[] itemStacks=new ItemStack[12];
    public Container dummy=new ContainerDummy();
    public InventoryCrafting dummyInv=new InventoryCrafting(dummy,3,3);
    public Registry.RecipePair product;


    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0>=0 && b0<itemStacks.length){
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }

        tank = new TankFBS(TANK_CAPACITY);
        if (par1NBTTagCompound.hasKey("Tank")) {
            tank.readFromNBT(par1NBTTagCompound.getCompoundTag("Tank"));
        }

        onInventoryChange();
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i=0;i<itemStacks.length;i++){
            if (itemStacks[i] != null){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbt);
                nbttaglist.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        NBTTagCompound nbt = new NBTTagCompound();
        tank.writeToNBT(nbt);
        par1NBTTagCompound.setTag("Tank", nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    public void updateEntity(){
        //checking inventory
        ItemStack fc=itemStacks[slotsMana[0]];
        ItemStack ec=itemStacks[slotsMana[1]];
        if (fc == null) return;
        if (fc.stackSize <= 0) {
            fc = null;
            return;
        }

        FluidStack fluid2 = FluidContainerRegistry.getFluidForFilledItem(fc);
        if(fluid2 != null && fluid2.getFluid()!=null && fluid2.getFluid()==BlockCore.mana) {
            int put = fill(ForgeDirection.UNKNOWN, fluid2, false);

            if (put == fluid2.amount) {
                ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(fc);
                if (emptyContainer != null) {
                    if (ec != null) {
                        if (!ec.isItemEqual(emptyContainer)) return;
                        if (ec.stackSize + emptyContainer.stackSize > ec.getMaxStackSize()) return;
                        ec.stackSize+=emptyContainer.stackSize;
                    }
                    else {
                        setInventorySlotContents(slotsMana[1], emptyContainer);
                    }
                }

                fc.stackSize--;
                if(fc.stackSize<=1){
                    setInventorySlotContents(slotsMana[0], null);
                }

                fill(ForgeDirection.UNKNOWN, fluid2, true);
                markDirty();
            }
        }
        else{
            FluidStack fluid = tank.getFluid();
            if (fluid != null && fluid.getFluid() != null) {

                ItemStack get = FluidContainerRegistry.fillFluidContainer(fluid.copy(), fc);
                if (get != null) {
                    int cap = FluidContainerRegistry.getContainerCapacity(get);
                    if (fluid.amount < cap) return;

                    if (ec != null) {
                        if (!ec.isItemEqual(get)) return;
                        if (ec.stackSize + get.stackSize > fc.getMaxStackSize()) return;
                    }

                    if (ec == null || ec.stackSize <= 0) {
                        setInventorySlotContents(slotsMana[1], get);
                    } else {
                        ec.stackSize += get.stackSize;
                    }

                    fc.stackSize--;
                    if (fc.stackSize <= 1) {
                        setInventorySlotContents(slotsMana[0], null);
                    }

                    drain(ForgeDirection.UNKNOWN, cap, true);
                    markDirty();
                }
            }
        }
    }

    //-----------------------------------------------------------------------------
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() == null){
            return 0;
        }

        FluidStack current = tank.getFluid();
        FluidStack resourceCopy = resource.copy();
        if (current != null && current.amount > 0 && !current.isFluidEqual(resourceCopy)){
            return 0;
        }

        int i = 0;
        int used = tank.fill(resourceCopy, doFill);
        resourceCopy.amount -= used;
        i += used;

        return i;
    }

    @Override
    public FluidStack drain(ForgeDirection forgeDirection,  FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (tank.getFluidType() == resource.getFluid()) {
            return tank.drain(resource.amount, doDrain);
        }
        return null;
    }
    @Override
    public FluidStack drain(ForgeDirection forgeDirection, int max, boolean b) {
        return tank.drain(max, b);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid == BlockCore.mana;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return !tank.isEmpty();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        Fluid fluid = tank.getFluidType();
        return fluid != null ? fluid.getIcon() : null;
    }

    public void onInventoryChange() {
        for (int i = 0; i < slotsMaterial.length; i++) {
            dummyInv.setInventorySlotContents(i, itemStacks[slotsMaterial[i]]);
        }
        product = Registry.FindMatchingRecipe(dummyInv, worldObj);
        if (product != null && tank.getFluidAmount()>=product.mana) setInventorySlotContents(slotsProduct[0], product.recipe.getCraftingResult(dummyInv));
        else setInventorySlotContents(slotsProduct[0], null);
    }

    //------------------------------------------------------------------
    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }
    @Override
    public ItemStack getStackInSlot(int i) {
        return itemStacks[i];
    }
    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (itemStacks[i] != null){
            ItemStack itemstack;

            if (itemStacks[i].stackSize <= j){
                itemstack = itemStacks[i];
                itemStacks[i] = null;

                if(i==slotsProduct[0] && product!=null){
                    tank.drain(product.mana*itemstack.stackSize, true);
                }
                onInventoryChange();

                markDirty();
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }

                if(i==slotsProduct[0] && product!=null){
                    tank.drain(product.mana*itemstack.stackSize, true);
                }
                onInventoryChange();

                markDirty();
                return itemstack;
            }
        }
        else{
            return null;
        }
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (itemStacks[i] != null){
            ItemStack itemstack = itemStacks[i];
            itemStacks[i] = null;
            markDirty();
            return itemstack;
        }
        else{
            return null;
        }
    }
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()){
            itemStack.stackSize = getInventoryStackLimit();
        }
        if(i!=slotsProduct[0]) onInventoryChange();
        markDirty();
    }
    @Override
    public String getInventoryName() {
        return BlockCore.workbench.getLocalizedName();
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
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityPlayer.getDistanceSq((double)xCoord+0.5D, (double)yCoord+0.5D, (double)zCoord+0.5D) <= 64.0D;
    }
    @Override
    public void openInventory() {
    }
    @Override
    public void closeInventory() {
    }
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if(i==10){
            FluidStack f=FluidContainerRegistry.getFluidForFilledItem(itemstack);
            return f!=null && f.getFluid()==BlockCore.mana;
        }
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return slotsMana;
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        if(p_102007_1_!=slotsMana[0]) return false;
        FluidStack fc=FluidContainerRegistry.getFluidForFilledItem(p_102007_2_);
        return fc!=null && fc.getFluid()==BlockCore.mana;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return p_102008_1_==slotsMana[1];
    }
}
