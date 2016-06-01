package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.container.ContainerWarp;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.tileentity.TileEntityPortalWarp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/22.
 */
public class MessageGuiButtonDecide implements IMessage {
    public FBSEntityProperties.WarpPosition destination;
    public EntityPlayer player;

    public MessageGuiButtonDecide(){}
    public MessageGuiButtonDecide(EntityPlayer ep, FBSEntityProperties.WarpPosition dest){
        player=ep;
        destination=dest;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        destination=new FBSEntityProperties.WarpPosition(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound nbt=new NBTTagCompound();
        destination.writeToNBT(nbt);
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class Handler implements IMessageHandler<MessageGuiButtonDecide, IMessage> {
        protected Random rand=new Random();
        @Override
        public IMessage onMessage(MessageGuiButtonDecide message, MessageContext ctx) {
            EntityPlayer ep=ctx.getServerHandler().playerEntity;
            World w=ctx.getServerHandler().playerEntity.worldObj;

            if(ep.openContainer instanceof ContainerWarp){
                ContainerWarp con=(ContainerWarp)ep.openContainer;

                //ポータルの生成
                if(!w.isRemote){
                    FBSEntityProperties.WarpPosition wp=message.destination;

                    int x=MathHelper.floor_double(ep.posX);
                    int y=MathHelper.floor_double(ep.posY);
                    int z=MathHelper.floor_double(ep.posZ);
                    int rx=(rand.nextBoolean()?1:-1)*(1+rand.nextInt(2));
                    int ry=0;
                    int rz=(rand.nextBoolean()?1:-1)*(1+rand.nextInt(2));
                    int meta=0;

                    if(MathHelper.abs_int(rx)>MathHelper.abs_int(rz)){
                        meta=(meta|8);
                    }
                    if(wp.dimId==FBS.dimensionCrackId && wp.x==-1 && wp.y==-1 && wp.z==-1){
                        //狭間生成用
                        meta=(meta|4);
                    }

                    rx+=x;
                    ry+=y;
                    rz+=z;
                    for(int i=0;i<2;i++){
                        w.setBlock(rx, ry+i, rz, BlockCore.portal1, meta+i, 2);
                        TileEntity te=w.getTileEntity(rx,ry+i,rz);
                        if(te instanceof TileEntityPortalWarp){
                            ((TileEntityPortalWarp) te).destination=message.destination;
                            te.markDirty();
                        }
                    }
                }

                //魔導書の使用回数を減らす
                if(!ep.capabilities.isCreativeMode){
                    ItemStack item=ep.getCurrentEquippedItem();
                    if(item != null && item.getItem() instanceof ItemBookSorcery){
                        ItemBookSorcery.reduceMagicMaxUse(item);
                    }
                }

                //GUI閉じる
                con.close=true;
            }

            //

            return null;
        }
    }
}
