package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.entity.EntityMagicArrowFlexible;
import jp.plusplus.fbs.entity.IPacketThrower;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/04/03.
 */
public class MessagePacketThrower implements IMessage {
    public int id;
    public NBTTagCompound nbt;

    public MessagePacketThrower(){}
    public MessagePacketThrower(Entity entity){
        id=entity.getEntityId();
        nbt=new NBTTagCompound();
        if(entity instanceof IPacketThrower) {
            ((IPacketThrower) entity).writeToPacketNBT(nbt);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id=buf.readInt();
        nbt= ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class Handler implements IMessageHandler<MessagePacketThrower, IMessage>{
        @Override
        public IMessage onMessage(MessagePacketThrower message, MessageContext ctx) {
            World w= FBS.proxy.getClientWorld();

            Entity e=w.getEntityByID(message.id);
            if(e instanceof IPacketThrower){
                ((IPacketThrower)e).readFromPacketNBT(message.nbt);
            }
            return null;
        }
    }
}
