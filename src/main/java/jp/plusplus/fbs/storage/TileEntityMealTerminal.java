package jp.plusplus.fbs.storage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class TileEntityMealTerminal extends TileEntity implements IMealDevice {
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
        markDirty();
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }

    @Override
    public boolean hasFragment() {
        return fragment!=null;
    }
}
