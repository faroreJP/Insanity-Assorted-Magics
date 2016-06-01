package jp.plusplus.fbs.storage;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by plusplus_F on 2016/03/09.
 */
public class MessageMealTerminalScroll implements IMessage {
    public int dId;
    public EntityPlayer player;
    public float scroll;

    public MessageMealTerminalScroll(){}
    public MessageMealTerminalScroll(int id, EntityPlayer ep, float f){
        dId=id;
        player=ep;
        scroll=f;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dId=buf.readInt();
        player= DimensionManager.getWorld(dId).getPlayerEntityByName(ByteBufUtils.readUTF8String(buf));
        scroll=buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dId);
        ByteBufUtils.writeUTF8String(buf, player.getCommandSenderName());
        buf.writeFloat(scroll);
    }

    public static class Handler implements IMessageHandler<MessageMealTerminalScroll, IMessage>{
        @Override
        public IMessage onMessage(MessageMealTerminalScroll message, MessageContext ctx) {
            Container container=message.player.openContainer;
            if(container instanceof ContainerMealTerminal){
                ((ContainerMealTerminal) container).scrollTo(message.scroll);
                //FBS.logger.info("scroll to " + message.scroll);
            }
            return null;
        }
    }
}
