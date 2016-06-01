package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.exprop.FBSEntityProperties;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class MessagePlayerPropertiesHandler implements IMessageHandler<MessagePlayerProperties, IMessage> {
    @Override
    public IMessage onMessage(MessagePlayerProperties message, MessageContext ctx) {
        FBSEntityProperties.get(FBS.proxy.getEntityPlayerInstance()).loadNBTData(message.data);
        return null;
    }
}
