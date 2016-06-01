package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.entity.EntityMagicArrowFlexible;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/10/17.
 */
public class MessageMagicFlexibleHandler implements IMessageHandler<MessageMagicFlexible, IMessage> {
    @Override
    public IMessage onMessage(MessageMagicFlexible message, MessageContext ctx) {
        World w=FBS.proxy.getClientWorld();

        Entity e=w.getEntityByID(message.entityId);
        if(e instanceof EntityMagicArrowFlexible){
            ((EntityMagicArrowFlexible) e).shootingEntity=w.getEntityByID(message.shooterId);
            ((EntityMagicArrowFlexible) e).readMagicsFromNBT(message.data);
        }
        return null;
    }
}
