package jp.plusplus.fbs.storage;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class TileEntityMealInlet extends TileEntity implements IMealDevice {
    public ItemStack fragment;

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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        if(par1NBTTagCompound.hasKey("Fragment")) fragment=ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("Fragment"));
        else fragment=null;
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        if(fragment!=null) par1NBTTagCompound.setTag("Fragment", fragment.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public ItemStack getFragment() {
        return fragment;
    }

    @Override
    public void setFragment(ItemStack f){
        fragment=f;
    }

    @Override
    public boolean hasFragment() {
        return fragment!=null;
    }

    @Override
    public void updateEntity(){
        if(!worldObj.isRemote && worldObj.getWorldTime()%10==0){
            ItemStack packet=null;
            TileEntityMeal tem=ItemMealFragment.getTileEntity(fragment);
            if(tem==null) return;

            //パケットを得る
            ForgeDirection dir=ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)&7);
            TileEntity te=worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
            if(te instanceof ISidedInventory){
                ISidedInventory inv=(ISidedInventory)te;
                int side=dir.ordinal()^1;
                int[] index=inv.getAccessibleSlotsFromSide(side);

                for(int i=0;i<index.length;i++){
                    ItemStack itemStack=inv.getStackInSlot(index[i]);
                    if(itemStack!=null && inv.canExtractItem(index[i], itemStack, side)){
                        packet=itemStack.copy();
                        packet.stackSize=1;
                        itemStack.stackSize--;
                        if(itemStack.stackSize<=0) inv.setInventorySlotContents(index[i], null);
                        te.markDirty();
                        break;
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

                    boolean f=false;
                    int size=inv.getSizeInventory();
                    for(int i=0;i<size;i++){
                        ItemStack itemStack=inv.getStackInSlot(i);
                        if(itemStack!=null){
                            packet=itemStack.copy();
                            packet.stackSize=1;
                            itemStack.stackSize--;
                            if(itemStack.stackSize<=0) inv.setInventorySlotContents(i, null);
                            te.markDirty();
                            f=true;
                            break;
                        }
                    }

                    if(f) break;
                }
            }
            else if(te instanceof IInventory){
                IInventory inv=(IInventory)te;
                int size=inv.getSizeInventory();
                for(int i=0;i<size;i++){
                    ItemStack itemStack=inv.getStackInSlot(i);
                    if(itemStack!=null){
                        packet=itemStack.copy();
                        packet.stackSize=1;
                        itemStack.stackSize--;
                        if(itemStack.stackSize<=0) inv.setInventorySlotContents(i, null);
                        te.markDirty();
                        break;
                    }
                }
            }

            //パケットがあったら転送
            if(packet!=null){
                tem.insertItemStack(packet);
            }
        }
    }
}
