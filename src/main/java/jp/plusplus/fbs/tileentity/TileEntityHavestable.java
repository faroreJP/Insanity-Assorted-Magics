package jp.plusplus.fbs.tileentity;

import jp.plusplus.fbs.Registry;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/09.
 * 10秒ごとに1つ成長する
 */
public class TileEntityHavestable extends TileEntity {
    public int glowingTicks;
    public int age;
    public int ageMax;
    public int meta;

    public TileEntityHavestable(){
        ageMax=60;
    }
    public TileEntityHavestable(int ageMax, int meta){
        this();
        this.ageMax=ageMax;
        this.meta=meta;
    }
    //---------------------------------------------------------------------------------------------------------
    public boolean canGlow(){
        if(age>=ageMax) return false;
        BiomeGenBase bgb=worldObj.getBiomeGenForCoords(xCoord, zCoord);

        boolean flag=false;
        if(meta==0){
            //ベースハーブはどこでも育つ
            flag=true;
        }
        else if(meta==4){
            //ソウルハーブは狭間なら育つ
            flag=bgb==Registry.biomeCrack;
        }
        else if(meta==5){
            //マンドレイクはy=32未満で育つ
            flag=yCoord<32 && yCoord!=worldObj.getHeightValue(xCoord, zCoord);
        }
        else if(meta==6){
            //ゴールドハーブはy=20未満で育つ
            flag=yCoord<20 && yCoord!=worldObj.getHeightValue(xCoord, zCoord);
        }
        else if(meta==7){
            //エンダーハーブはエンドなら育つ
            flag=worldObj.provider.dimensionId==1;
        }
        else if(meta==8){
            //エクスプローシブハーブはネザーなら育つ
            flag=worldObj.provider.dimensionId==-1;
        }
        else{
            //直射日光があたらなければ育つ
            flag=yCoord!=worldObj.getHeightValue(xCoord, zCoord);

            if(!flag){
                switch (meta){
                    case 1:
                        //ブラッディハーブは温暖なら育つ
                        for(BiomeManager.BiomeEntry be : BiomeManager.getBiomes(BiomeManager.BiomeType.WARM)){
                            if(bgb==be.biome){
                                flag=true;
                                break;
                            }
                        }
                        break;

                    case 2:
                        //マナハーブは秋なら育つ
                        flag=bgb==Registry.biomeAutumn;
                        break;

                    case 3:
                        //スタミナハーブは砂漠なら育つ
                        flag=(bgb==BiomeGenBase.desert || bgb==BiomeGenBase.desertHills || bgb==BiomeGenBase.savanna || bgb==BiomeGenBase.savannaPlateau || bgb==BiomeGenBase.mesa || bgb==BiomeGenBase.mesaPlateau|| bgb==BiomeGenBase.mesaPlateau_F);
                        if(!flag){
                            for(BiomeManager.BiomeEntry be : BiomeManager.getBiomes(BiomeManager.BiomeType.DESERT)){
                                if(bgb==be.biome){
                                    flag=true;
                                    break;
                                }
                            }
                        }
                        break;
                }
            }

        }

        return flag;
    }
    public boolean canHarvest(){
        return age>=ageMax;
    }
    public void glow(Random rand){
        age+=1+rand.nextInt(3);
    }
    public void onHarvest(){
        age=0;
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    //---------------------------------------------------------------------------------------------------------
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        age=par1NBTTagCompound.getInteger("Age");
        ageMax=par1NBTTagCompound.getInteger("AgeMax");
        glowingTicks=par1NBTTagCompound.getInteger("GlowingTicks");
        meta=par1NBTTagCompound.getInteger("Meta");
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("Age", age);
        par1NBTTagCompound.setInteger("AgeMax", ageMax);
        par1NBTTagCompound.setInteger("GlowingTicks", glowingTicks);
        par1NBTTagCompound.setInteger("Meta", meta);
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
    public void updateEntity(){
        if(!worldObj.isRemote) {
            glowingTicks++;
            if (glowingTicks >= 200) {
                glowingTicks = 0;
                if (canGlow()) {
                    age++;
                    if (age > ageMax) {
                        age = ageMax;
                    }
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }
}
