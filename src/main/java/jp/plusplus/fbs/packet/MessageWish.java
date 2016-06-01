package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.event.wish.WishHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class MessageWish implements IMessage {
    public String wish;

    public MessageWish(){}
    public MessageWish(String wish){
        this.wish=wish;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        wish=ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, wish);
    }

    public static class Handler implements IMessageHandler<MessageWish, IMessage>{
        @Override
        public IMessage onMessage(MessageWish message, MessageContext ctx) {
            EntityPlayer ep=ctx.getServerHandler().playerEntity;
            ctx.getServerHandler().processChatMessage(new C01PacketChatMessage(message.wish+"!!!"));
            WishHandler.handleWish(ep, message.wish);
            return null;
        }
    }
}
