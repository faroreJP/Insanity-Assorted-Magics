package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class MessagePlayerJoinInAnoucementHandler implements IMessageHandler<MessagePlayerJoinInAnnouncement, MessagePlayerProperties> {
    @Override
    public MessagePlayerProperties onMessage(MessagePlayerJoinInAnnouncement message, MessageContext ctx) {
        String uuidString = message.getUuid();
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (player.getGameProfile().getId().toString().equals(uuidString)) {
            return new MessagePlayerProperties(player);
        }
        return null;
    }
}