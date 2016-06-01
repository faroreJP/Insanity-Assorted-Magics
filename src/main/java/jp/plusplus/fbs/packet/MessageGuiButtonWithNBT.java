package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerAlchemyCauldron;
import jp.plusplus.fbs.container.ContainerTFKEnchantment;
import jp.plusplus.fbs.container.spirit.ContainerSpiritMain;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/10/21.
 */
public class MessageGuiButtonWithNBT implements IMessage {
    protected int buttonId;
    protected NBTTagCompound nbt;

    public MessageGuiButtonWithNBT(){}
    public MessageGuiButtonWithNBT(int id, NBTTagCompound nbt){
        buttonId=id;
        this.nbt=nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        buttonId= ByteBufUtils.readVarInt(buf, 4);
        nbt=ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, buttonId, 4);
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class Handler implements IMessageHandler<MessageGuiButtonWithNBT, IMessage>{

        @Override
        public IMessage onMessage(MessageGuiButtonWithNBT message, MessageContext ctx) {
            EntityPlayer player=ctx.getServerHandler().playerEntity;
            Container con=player.openContainer;
            int x= MathHelper.floor_double(player.posX);
            int y=MathHelper.floor_double(player.posY);
            int z=MathHelper.floor_double(player.posZ);

            if(con instanceof ContainerSpiritMain){
                int type=((ContainerSpiritMain) con).type;
                if(type==1){
                    if(message.buttonId==0){
                        //OKの場合、設定を反映する
                        int index=SpiritManager.findSpiritToolIndex(player);
                        if(index!=-1){
                            ItemStack itemStack= player.inventory.getStackInSlot(index);
                            SpiritStatus ss=SpiritStatus.readFromNBT(itemStack.getTagCompound());
                            ss.getConfiguration().readFromNBT(message.nbt);

                            NBTTagCompound nbt1=new NBTTagCompound();
                            SpiritStatus.writeToNBT(ss, nbt1);
                            itemStack.setTagCompound(nbt1);
                        }
                    }
                    player.openGui(FBS.instance, FBS.GUI_SPIRIT_MAIN_ID, player.worldObj, x,y,z);
                }
            }

            return null;
        }
    }
}
