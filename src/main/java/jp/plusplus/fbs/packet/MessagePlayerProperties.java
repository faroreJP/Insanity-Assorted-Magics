package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class MessagePlayerProperties implements IMessage {
    public NBTTagCompound data;

    public MessagePlayerProperties(){}
    public MessagePlayerProperties(EntityPlayer entityPlayer) {
        this.data = new NBTTagCompound();
        FBSEntityProperties.get(entityPlayer).saveNBTData(data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }
}
