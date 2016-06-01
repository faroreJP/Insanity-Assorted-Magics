package jp.plusplus.fbs.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
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
 * Createdby pluslus_Fon 2015/06/14.
 */
public class TileEntityFillingTable extends TileEntity implements ISidedInventory,IFluidHandler {
    public static final int TANK_CAPACITY=4000;
    public TankFBS tank=new TankFBS(TANK_CAPACITY);

    private static final int[] slotsBook=new int[]{0}, slotsFluid=new int[]{1,2};
    public ItemStack[] itemStacks=new ItemStack[3];

    public static final short MAX_PROGRESS=20*10;
    public short progress;

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        progress= par1NBTTagCompound.getShort("Progress");

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
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setShort("Progress", progress);

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

    @Override
    public void updateEntity() {
        if(worldObj.isRemote) return;

        //------------ filling --------------
        if(itemStacks[0]!=null && itemStacks[0].getItem()==ItemCore.bookSorcery){
            Registry.BookData bd=Registry.GetBookDataFromItemStack(itemStacks[0]);
            if(bd!=null && bd.isMagic){
                int amount=40*bd.lv;
                if(tank.getFluidAmount()>=amount){
                    if (progress >= MAX_PROGRESS) {
                        tank.drain(amount, true);

                        //一定確率で失敗する
                        float pr;
                        if(bd.lv<=5) pr=1.0f;
                        else if(bd.lv<40) pr=1.0f-0.02f*(bd.lv-5);
                        else pr=0.3f;
                        if(worldObj.rand.nextFloat()<pr){
                            ItemBookSorcery.setMagicMaxUse(itemStacks[0], ItemBookSorcery.getMagicMaxUse(itemStacks[0])+1);
                        }
                        else{
                            setInventorySlotContents(0, new ItemStack(ItemCore.bookBroken, 1, itemStacks[0].getItemDamage()));
                        }

                        markDirty();
                        progress=0;
                    }

                    progress++;
                }
            }
        }
        else{
            progress=0;
        }

        //------------ bucket ---------------

        //checking inventory
        if (itemStacks[1] == null) return;
        if (itemStacks[1].stackSize <= 0) {
            itemStacks[1] = null;
            return;
        }

        FluidStack fluid2 = FluidContainerRegistry.getFluidForFilledItem(itemStacks[1]);
        if(fluid2 != null && fluid2.getFluid()!=null && fluid2.getFluid()==BlockCore.mana) {
            int put = fill(ForgeDirection.UNKNOWN, fluid2, false);

            if (put == fluid2.amount) {
                ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(itemStacks[1]);
                if (emptyContainer != null) {
                    if (itemStacks[2] != null) {
                        if (!itemStacks[2].isItemEqual(emptyContainer)) return;
                        if (itemStacks[2].stackSize + emptyContainer.stackSize > itemStacks[2].getMaxStackSize()) return;
                        itemStacks[2].stackSize+=emptyContainer.stackSize;
                    }
                    else {
                        setInventorySlotContents(2, emptyContainer);
                    }
                }

                itemStacks[1].stackSize--;
                if(itemStacks[1].stackSize<=1){
                    setInventorySlotContents(1, null);
                }

                fill(ForgeDirection.UNKNOWN, fluid2, true);
                markDirty();
            }
        }
        else{
            FluidStack fluid = tank.getFluid();
            if (fluid != null && fluid.getFluid() != null) {

                ItemStack get = FluidContainerRegistry.fillFluidContainer(fluid.copy(), itemStacks[1]);
                if (get != null) {
                    int cap = FluidContainerRegistry.getContainerCapacity(get);
                    if (fluid.amount < cap) return;

                    if (itemStacks[2] != null) {
                        if (!itemStacks[2].isItemEqual(get)) return;
                        if (itemStacks[2].stackSize + get.stackSize > itemStacks[1].getMaxStackSize()) return;
                    }

                    if (itemStacks[2] == null || itemStacks[2].stackSize <= 0) {
                        setInventorySlotContents(2, get);
                    } else {
                        itemStacks[2].stackSize += get.stackSize;
                    }

                    itemStacks[1].stackSize--;
                    if (itemStacks[1].stackSize <= 1) {
                        setInventorySlotContents(1, null);
                    }

                    drain(ForgeDirection.UNKNOWN, cap, true);
                    markDirty();
                }
            }
        }
    }

    //-------------------------------------------------------------------------------------

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

    public int getProgressScaled(int par1){
        return par1*this.progress/MAX_PROGRESS;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getFluidIcon(){
        Fluid fluid = tank.getFluidType();
        return fluid != null ? fluid.getIcon() : null;
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
                markDirty();
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }
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
        markDirty();
    }
    @Override
    public String getInventoryName() {
        return BlockCore.fillingTable.getLocalizedName();
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
        if(i==0) {
            return itemstack.getItem()==ItemCore.bookSorcery;
        }
        else if(i==1){
            if(itemstack.getItem()==ItemCore.bookSorcery) return false;
            FluidStack f=FluidContainerRegistry.getFluidForFilledItem(itemstack);
            return f!=null && f.getFluid()==BlockCore.mana;
        }
        return false;
    }
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
        if(var1==0 || var1==1) return slotsBook;
        return slotsFluid;
    }
    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return i!=2 && isItemValidForSlot(i, itemstack);
    }
    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i==0 || i==2;
    }
}
