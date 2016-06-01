package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerAlchemyCauldron;
import jp.plusplus.fbs.container.ContainerTFKEnchantment;
import jp.plusplus.fbs.container.ContainerWarp;
import jp.plusplus.fbs.container.spirit.ContainerSpiritLearn;
import jp.plusplus.fbs.container.spirit.ContainerSpiritMain;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/10/21.
 */
public class MessageGuiButton implements IMessage {
    protected int buttonId;

    public MessageGuiButton(){}
    public MessageGuiButton(int id){
        buttonId=id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        buttonId= ByteBufUtils.readVarInt(buf, 4);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, buttonId, 4);
    }

    public static class Handler implements IMessageHandler<MessageGuiButton, IMessage>{

        @Override
        public IMessage onMessage(MessageGuiButton message, MessageContext ctx) {
            Container con=ctx.getServerHandler().playerEntity.openContainer;

            if(con instanceof ContainerTFKEnchantment){
                //エンチャントできそうならエンチャントする
                ContainerTFKEnchantment c=(ContainerTFKEnchantment)con;
                if(c.canEnchant()){
                    c.tryEnchant();
                }
            }
            else if(con instanceof ContainerAlchemyCauldron){
                ContainerAlchemyCauldron c=(ContainerAlchemyCauldron)con;
                if(c.entity.canCompounding()){
                    c.entity.tryCompounding(ctx.getServerHandler().playerEntity);
                }
            }
            else if(con instanceof ContainerSpiritMain){
                ContainerSpiritMain c=(ContainerSpiritMain)con;
                EntityPlayer player=ctx.getServerHandler().playerEntity;
                int x= MathHelper.floor_double(player.posX);
                int y=MathHelper.floor_double(player.posY);
                int z=MathHelper.floor_double(player.posZ);

                if(c.type==0){
                    switch (message.buttonId){
                        case 0:
                            player.openGui(FBS.instance, FBS.GUI_SPIRIT_SKILL_ID, player.worldObj, x,y,z);
                            break;

                        case 1:
                            player.openGui(FBS.instance, FBS.GUI_SPIRIT_LEARN_ID, player.worldObj, x,y,z);
                            break;

                        case 2:
                            SpiritManager.bless(player, SpiritManager.findSpiritTool(player));
                            break;

                        case 3:
                            SpiritManager.repair(player, SpiritManager.findSpiritTool(player), -1);
                            break;

                        case 5:
                            player.openGui(FBS.instance, FBS.GUI_SPIRIT_CONFIG_ID, player.worldObj, x,y,z);
                            break;

                        default:
                            break;
                    }
                }
            }
            else if(con instanceof ContainerSpiritLearn){
                EntityPlayer player=ctx.getServerHandler().playerEntity;
                int x= MathHelper.floor_double(player.posX);
                int y=MathHelper.floor_double(player.posY);
                int z=MathHelper.floor_double(player.posZ);
                player.openGui(FBS.instance, FBS.GUI_SPIRIT_MAIN_ID, player.worldObj, x,y,z);
            }

            return null;
        }
    }
}
