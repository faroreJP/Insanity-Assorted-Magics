package jp.plusplus.fbs.command;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.event.wish.WishHandler;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class CommandWish extends CommandBase {
    @Override
    public String getCommandName() {
        return "wish";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.fbs.wish.usage";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        World w = p_71515_1_.getEntityWorld();

        if (p_71515_2_.length >= 2) {
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[0]);
            if (ep == null) new CommandException("commands.generic.player.notFound", new Object[1]);
            WishHandler.handleWish(ep, p_71515_2_[1]);
        }
        else if (p_71515_2_.length == 1){
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[0]);
            if (ep == null) new CommandException("commands.generic.player.notFound", new Object[1]);
            ep.openGui(FBS.instance, FBS.GUI_WISH_ID, w, MathHelper.floor_double(ep.posX), MathHelper.floor_double(ep.posY), MathHelper.floor_double(ep.posZ));
        }
        else {
            throw new WrongUsageException("commands.fbs.wish.usage", new Object[0]);
        }
    }
}
