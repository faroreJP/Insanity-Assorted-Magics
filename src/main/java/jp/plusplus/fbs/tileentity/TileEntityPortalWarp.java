package jp.plusplus.fbs.tileentity;

import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by plusplus_F on 2015/10/28.
 */
public class TileEntityPortalWarp extends TileEntity {
    public FBSEntityProperties.WarpPosition destination=new FBSEntityProperties.WarpPosition(1,0,0,0);

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        destination=new FBSEntityProperties.WarpPosition(par1NBTTagCompound.getCompoundTag("Dest"));
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        NBTTagCompound nbt=new NBTTagCompound();
        destination.writeToNBT(nbt);
        par1NBTTagCompound.setTag("Dest", nbt);
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
}
