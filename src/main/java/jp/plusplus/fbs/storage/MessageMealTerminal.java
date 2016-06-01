package jp.plusplus.fbs.storage;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;

import java.util.Iterator;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class MessageMealTerminal implements IMessage {
    public int type;
    public TileEntityMeal tem;
    public ItemStack itemStack;
    public EntityPlayer player;

    public MessageMealTerminal(){}

    public MessageMealTerminal(EntityPlayer player, TileEntityMeal te, int type, ItemStack itemStack){
        tem=te;
        this.type=type;
        this.itemStack=itemStack;
        this.player=player;
    }


    @Override
    public void fromBytes(ByteBuf buf) {

        type=ByteBufUtils.readVarInt(buf, 4);
        int id=Integer.parseInt(ByteBufUtils.readUTF8String(buf));
        int x=Integer.parseInt(ByteBufUtils.readUTF8String(buf));
        int y=Integer.parseInt(ByteBufUtils.readUTF8String(buf));
        int z=Integer.parseInt(ByteBufUtils.readUTF8String(buf));
        itemStack=ByteBufUtils.readItemStack(buf);
        String name=ByteBufUtils.readUTF8String(buf);

        tem=(TileEntityMeal)ChunkLoadManager.getWorld(id).getTileEntity(x, y, z);
        player=tem.getWorldObj().getPlayerEntityByName(name);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, type, 4);
        ByteBufUtils.writeUTF8String(buf, "" + tem.getWorldObj().provider.dimensionId);
        ByteBufUtils.writeUTF8String(buf, "" + tem.xCoord);
        ByteBufUtils.writeUTF8String(buf, "" + tem.yCoord);
        ByteBufUtils.writeUTF8String(buf, "" + tem.zCoord);
        ByteBufUtils.writeItemStack(buf, itemStack);
        ByteBufUtils.writeUTF8String(buf, player.getCommandSenderName());
    }

    public static class Handler implements IMessageHandler<MessageMealTerminal, IMessage>{

        @Override
        public IMessage onMessage(MessageMealTerminal message, MessageContext ctx) {
            if (message.itemStack == null) return null;

            if (message.type == 0) {
                message.tem.insertItemStack(message.itemStack);
                if(message.player.openContainer instanceof ContainerMealTerminal){
                    message.player.inventory.setItemStack(null);
                }
            } else if (message.type == 1) {
                message.tem.exportItemStack(message.itemStack, message.itemStack.stackSize);
                if(message.player.openContainer instanceof ContainerMealTerminal){
                    message.player.inventory.setItemStack(message.itemStack.copy());
                }
            }

            Iterator it=message.tem.getWorldObj().playerEntities.iterator();
            while(it.hasNext()){
                EntityPlayer ep=(EntityPlayer)it.next();

                if(ep.openContainer instanceof ContainerMealTerminal){
                    ((ContainerMealTerminal)ep.openContainer).inv.markDirty();
                }
            }

            //return new MessageMealTerminal(message.player, message.tem, message.type, message.itemStack);
            //return new MessageMealTerminal();

            PacketHandler.INSTANCE.sendToAll(new MessageMealTerminal(message.player, message.tem, message.type, message.itemStack));

            return null;
        }
    }

    public static class HandlerClient implements IMessageHandler<MessageMealTerminal, IMessage>{

        @Override
        public IMessage onMessage(MessageMealTerminal message, MessageContext ctx) {
            EntityPlayer player=FBS.proxy.getEntityPlayerInstance();
            if(player!=null && player.openContainer instanceof ContainerMealTerminal){
                ((ContainerMealTerminal) player.openContainer).inv.markDirty();
                //FBS.logger.info("list updated");
            }
            return null;
        }
    }
}
