package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.entity.EntityMagicArrowFlexible;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * Created by plusplus_F on 2015/10/17.
 */
public class MessageMagicFlexible implements IMessage {
    public int shooterId;
    public int entityId;
    public NBTTagCompound data;

    public MessageMagicFlexible(){}
    public MessageMagicFlexible(EntityMagicArrowFlexible entity){
        shooterId=entity.shootingEntity.getEntityId();
        entityId=entity.getEntityId();
        data=new NBTTagCompound();
        entity.writeMagicsToNBT(data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shooterId=ByteBufUtils.readVarInt(buf, 4);
        entityId=ByteBufUtils.readVarInt(buf, 4);
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, shooterId, 4);
        ByteBufUtils.writeVarInt(buf, entityId, 4);
        ByteBufUtils.writeTag(buf, data);
    }
}
