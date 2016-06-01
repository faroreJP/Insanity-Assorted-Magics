package jp.plusplus.fbs.command;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/01.
 */
public class CommandGetBook extends CommandBase {
    @Override
    public String getCommandName() {
        return "givebook";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.fbs.getbook.usage";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length >= 2) {
            World w = p_71515_1_.getEntityWorld();
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[0]);
            if (ep == null) new CommandException("commands.generic.player.notFound", new Object[1]);

            String name=p_71515_2_[1];
            ItemStack itemStack= Registry.GetBookItemStack(name);
            if(itemStack==null){
                new CommandException("commands.fbs.getbook.notFound", new Object[1]);
            }

            EntityItem entityitem = ep.dropPlayerItemWithRandomChoice(itemStack, false);
            entityitem.delayBeforeCanPickup = 0;
            entityitem.func_145797_a(ep.getCommandSenderName());
            func_152373_a(p_71515_1_, this, "commands.give.success", new Object[] {itemStack.func_151000_E(), 1, ep.getCommandSenderName()});
        } else {
            throw new WrongUsageException("commands.fbs.getbook.usage", new Object[0]);
        }
    }
}
