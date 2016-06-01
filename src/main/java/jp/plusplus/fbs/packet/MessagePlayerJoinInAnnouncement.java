package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class MessagePlayerJoinInAnnouncement implements IMessage {

    private String uuid;

    public MessagePlayerJoinInAnnouncement(){}
    public MessagePlayerJoinInAnnouncement(EntityPlayer player) {
        this.uuid = player.getGameProfile().getId().toString();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
