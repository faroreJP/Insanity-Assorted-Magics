package jp.plusplus.fbs.packet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.entity.EntityMagicArrowFlexible;
import jp.plusplus.fbs.particle.EntityVortexFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/20.
 */
public class MessageMagicVortex implements IMessage {
    private int shooterId;
    private double range;
    private NBTTagCompound data;

    public MessageMagicVortex(){}
    public MessageMagicVortex(EntityPlayer ep, double range, NBTTagCompound magics){
        shooterId=ep.getEntityId();
        this.range=range;
        data=magics;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shooterId= ByteBufUtils.readVarInt(buf, 4);
        range=ByteBufUtils.readVarInt(buf, 4)/100.0;
        data=ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, shooterId, 4);
        ByteBufUtils.writeVarInt(buf, (int)(range*100), 4);
        ByteBufUtils.writeTag(buf, data);
    }

    public static class Handler implements IMessageHandler<MessageMagicVortex, IMessage>{
        @Override
        public IMessage onMessage(MessageMagicVortex message, MessageContext ctx) {
            World w= FBS.proxy.getClientWorld();

            if(w!=null){
                Entity e=w.getEntityByID(message.shooterId);
                if(e!=null){
                    if(message.data.hasKey("EnchantMagics")){
                        //1.魔法のリストを生成
                        NBTTagList nbttaglist = (NBTTagList)message.data.getTag("EnchantMagics");
                        IMagicEnchant[] magics=new IMagicEnchant[nbttaglist.tagCount()];
                        for(int i=0;i<nbttaglist.tagCount();i++){
                            NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
                            magics[i]=(IMagicEnchant)Registry.GetMagic(nbt1.getString("MagicName")).getMagic(w, (EntityPlayer)e, false);
                        }

                        //2.ループする
                        Random rand=new Random();
                        IMagicEnchant.ParticleColor col;
                        for(int i=0;i<5;i++){
                            for(int k=0;k<8;k++){
                                col=magics[rand.nextInt(magics.length)].setParticleColor();
                                spawnParticle(w, e.posX, e.posY+e.getEyeHeight()/2, e.posZ, i, message.range, col.red, col.green, col.blue);
                            }
                        }
                    }
                    else{
                        //通常のやつ
                        for(int i=0;i<5;i++){
                            for(int k=0;k<8;k++){
                                spawnParticle(w, e.posX, e.posY+e.getEyeHeight()/2, e.posZ, i, message.range, 1.f, 1.f, 1.f);
                            }
                        }
                    }
                }

                if(e instanceof EntityMagicArrowFlexible){
                    ((EntityMagicArrowFlexible) e).shootingEntity=w.getEntityByID(message.shooterId);
                    ((EntityMagicArrowFlexible) e).readMagicsFromNBT(message.data);
                }
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void spawnParticle(World w, double x, double y, double z, int d, double r, float red, float green, float blue){
            EntityVortexFX fx=new EntityVortexFX(w, x, y, z, d, r, red, green, blue, 1.f);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }
}
