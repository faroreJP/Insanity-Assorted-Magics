package jp.plusplus.fbs.pottery;

import com.google.common.collect.ImmutableList;
import jp.plusplus.fbs.api.IPottery;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;

/**
 * Created by plusplus_F on 2015/08/26.
 * レンダラのための無機能
 */
public class TileEntityPottery extends TileEntity {
    public static int PROGRESS_MAX=10*20*60*20;

    public IPottery.PotteryState state= IPottery.PotteryState.INVALID_VALUE;
    public IPottery.PotterySize size;
    public IPottery.PotteryGrade grade;
    public byte pattern;
    public boolean hasEffect;

    public int progress;

    public NBTTagCompound dummyNBT;

    public void setData(ItemStack itemStack){
        IPottery ip=(IPottery)((ItemBlock) itemStack.getItem()).field_150939_a;

        NBTTagCompound nbt=itemStack.getTagCompound();
        if(nbt==null) return;

        state=ip.getState(nbt);
        size=ip.getSize(nbt);
        grade=ip.getGrade(nbt);
        pattern=ip.getPattern(nbt);
        hasEffect=ip.hasEffect(nbt);
    }
    public int getMetadata(){
        int meta=(pattern%0xf);
        meta=(meta|((state.getValue())<<6));
        meta=(meta|((size.getValue())<<4));
        meta=(meta|((grade.getValue())<<8));
        return meta;
    }

    public void writePotteryData(NBTTagCompound nbt){
        nbt.setByte("state", state.getValue());
        nbt.setByte("size", size.getValue());
        nbt.setByte("grade", grade.getValue());
        nbt.setByte("pattern", pattern);
        nbt.setBoolean("hasEffect", hasEffect);
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);
        progress=par1NBTTagCompound.getInteger("Progress");

        state= IPottery.PotteryState.Get(par1NBTTagCompound.getByte("pState"));
        size= IPottery.PotterySize.Get(par1NBTTagCompound.getByte("pSize"));
        grade= IPottery.PotteryGrade.Get(par1NBTTagCompound.getByte("pGrade"));
        pattern=par1NBTTagCompound.getByte("pPattern");
        hasEffect=par1NBTTagCompound.getBoolean("pEffect");

        dummyNBT=new NBTTagCompound();
        writePotteryData(dummyNBT);
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Progress", progress);

        par1NBTTagCompound.setByte("pState", state.getValue());
        par1NBTTagCompound.setByte("pSize", size.getValue());
        par1NBTTagCompound.setByte("pGrade", grade.getValue());
        par1NBTTagCompound.setByte("pPattern", pattern);
        par1NBTTagCompound.setBoolean("pEffect", hasEffect);
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
        //--------------------------IPotteryを得る-------------------------------
        Block b = getBlockType();
        IPottery ip;
        if (b instanceof IPottery) {
            ip = (IPottery) b;
        } else {
            //なんか妙だったら即破壊
            if(!worldObj.isRemote) worldObj.func_147480_a(xCoord, yCoord, zCoord, true);
            return;
        }

        /*
        //--------------------------Stateを得る-------------------------------
        if (state == IPottery.PotteryState.INVALID_VALUE) {
            state = ip.getState(potteryMetadata);
        }
        */

        //--------------------------乾燥-------------------------------
        if(state== IPottery.PotteryState.MOLDED){
            int base=10;

            //乾燥地帯なら乾燥は早い
            BiomeGenBase bgb=worldObj.getBiomeGenForCoords(xCoord, zCoord);
            ImmutableList<BiomeManager.BiomeEntry> list=BiomeManager.getBiomes(BiomeManager.BiomeType.DESERT);
            for(BiomeManager.BiomeEntry be : list){
                if(bgb==be.biome){
                    base*=2;
                    break;
                }
            }

            //雨が降るバイームでかつ雨なら乾燥は遅くなる
            if(worldObj.isRaining() && bgb.canSpawnLightningBolt()){
                if(yCoord==worldObj.getHeightValue(xCoord, zCoord)){
                    //雨で直接濡れていようものなら・・・
                    base=0;
                }
                else{
                    base/=2;
                }
            }

            if(dummyNBT==null){
                dummyNBT=new NBTTagCompound();
                writePotteryData(dummyNBT);
            }

            progress+=base;
            if(progress>=10*20*ip.getDrySec(dummyNBT)){
                progress=0;

                ItemStack tmp=new ItemStack(getBlockType(), 1, 0);
                ip.setState(tmp, IPottery.PotteryState.DRY);
                state=ip.getState(tmp.getTagCompound());
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }

    }
}
