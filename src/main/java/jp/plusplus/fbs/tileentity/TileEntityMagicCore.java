package jp.plusplus.fbs.tileentity;

import jp.plusplus.fbs.container.inventory.InventoryMagic;
import jp.plusplus.fbs.entity.EntityButterfly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by pluslus_F on 2015/06/17.
 */
public class TileEntityMagicCore extends TileEntity {
    public int ticks;
    protected String circleName="null";
    protected int circleRadius;

    public InventoryMagic inv;
    public int progressMax;
    public int progress;

    public void setInventory(InventoryMagic inv){
        this.inv=inv;
    }
    public void removeInventory(){
        inv=null;
    }

    public void setProgressMax(int p){
        progressMax=p;
        markDirty();
    }
    public void resetProgress(){
        progress=0;
        progressMax=0;
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        circleName=par1NBTTagCompound.getString("CircleName");
        circleRadius= par1NBTTagCompound.getInteger("CircleRadius");

        progress=par1NBTTagCompound.getInteger("Progress");
        progressMax=par1NBTTagCompound.getInteger("ProgressMax");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setString("CircleName", circleName);
        par1NBTTagCompound.setInteger("CircleRadius", circleRadius);

        par1NBTTagCompound.setInteger("Progress", progress);
        par1NBTTagCompound.setInteger("ProgressMax", progressMax);
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
        ticks++;

        if(worldObj.isRemote) return;
        if(inv!=null && progressMax>0){
            progress++;
            if(progress>=progressMax){
                inv.work();
                progress=0;
            }
        }

        if (ticks % 60 != 0) return;

        //checking magic
        if (circleName.equals("null")) return;
        int bx = xCoord - circleRadius, bz = zCoord - circleRadius;
        int size = 2 * circleRadius + 1;
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                if (i == size / 2 && i == k) continue;
                if (!worldObj.isAirBlock(bx + k, yCoord, bz + i) && worldObj.isBlockNormalCubeDefault(bx+k, yCoord, bz+i, false)) {
                    setMagicCircle("null", 1);
                    markDirty();
                    return;
                }
            }
        }

        /*
        if(ticks%180!=0 || worldObj.rand.nextFloat()<=0.8f) return;
        EntityButterfly eb=new EntityButterfly(worldObj, xCoord, yCoord+1, zCoord);
        worldObj.spawnEntityInWorld(eb);
        */
    }

    public void setMagicCircle(String name, int radius){
        circleName=(name==null?"null":name);
        circleRadius=radius;
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    public void clearCharms() {
        if (circleName.equals("null")) return;
        int bx = xCoord - circleRadius, bz = zCoord - circleRadius;
        int size = 2 * circleRadius + 1;
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                if (i == size / 2 && i == k) continue;
                worldObj.setBlockToAir(bx + k, yCoord, bz + i);
            }
        }
    }

    public String getCircleName(){
        return circleName;
    }
    public int getCircleRadius(){
        return circleRadius;
    }
}
