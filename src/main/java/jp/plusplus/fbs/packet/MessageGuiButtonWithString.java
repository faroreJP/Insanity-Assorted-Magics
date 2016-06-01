package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerContract;
import jp.plusplus.fbs.container.ContainerWarp;
import jp.plusplus.fbs.container.spirit.ContainerSpiritLearn;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.spirit.SpiritManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by plusplus_F on 2015/10/23.
 */
public class MessageGuiButtonWithString implements IMessage {
    protected int index;
    protected String name;

    public MessageGuiButtonWithString(){}
    public MessageGuiButtonWithString(int index, String str){
        this.index=index;
        this.name=str;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        index= ByteBufUtils.readVarInt(buf, 4);
        name=ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, index, 4);
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public static class Handler implements IMessageHandler<MessageGuiButtonWithString, IMessage>{
        @Override
        public IMessage onMessage(MessageGuiButtonWithString message, MessageContext ctx) {
            Container con=ctx.getServerHandler().playerEntity.openContainer;
            EntityPlayer player=ctx.getServerHandler().playerEntity;

            if(con instanceof ContainerWarp){
                //時空間の航行
                FBSEntityProperties prop=FBSEntityProperties.get(player);
                prop.getDestinations().get(message.index).setName(message.name);
                SanityManager.sendPacket(player);
            }
            else if(con instanceof ContainerContract){
                //精霊との契約
                String ch=((ContainerContract) con).contract(message.name, player);
            }
            else if(con instanceof ContainerSpiritLearn){
                ContainerSpiritLearn c=(ContainerSpiritLearn)con;
                //FBS.logger.info(message.name);
                c.learn(message.name);
            }

            return null;
        }
    }
}
