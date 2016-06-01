package jp.plusplus.fbs.command;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.command.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/01.
 */
public class CommandGetSpirit extends CommandBase {
    @Override
    public String getCommandName() {
        return "givespirit";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.fbs.getspirit.usage";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length >= 3) {
            World w = p_71515_1_.getEntityWorld();
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[0]);
            if (ep == null) new CommandException("commands.generic.player.notFound", new Object[1]);

            Item item=(Item)Item.itemRegistry.getObject(p_71515_2_[1]);
            if(item==null){
                throw new CommandException("commands.fbs.getspirit.notFound", new Object[1]);
            }

            SpiritManager.ToolEntry te=SpiritManager.getTool(item);
            if(te==null){
                throw new CommandException("commands.fbs.getspirit.notFound2", new Object[1]);
            }

            boolean male=ep.worldObj.rand.nextBoolean();
            if(p_71515_2_.length>=4){
                if(p_71515_2_[3].equals("male")) male=true;
                else if(p_71515_2_[3].equals("female")) male=false;
                else throw new WrongUsageException("commands.fbs.getspirit.usage", new Object[0]);
            }

            ItemStack itemStack=te.getSpiritToolStack(male, SpiritManager.getRandomCharacter(male), p_71515_2_[2], ep, new ItemStack(item));
            if(itemStack!=null){
                EntityItem entityitem = ep.dropPlayerItemWithRandomChoice(itemStack, false);
                entityitem.delayBeforeCanPickup = 0;
                entityitem.func_145797_a(ep.getCommandSenderName());
                func_152373_a(p_71515_1_, this, "commands.give.success", new Object[] {itemStack.func_151000_E(), 1, ep.getCommandSenderName()});

                SpiritStatus status=SpiritStatus.readFromNBT(itemStack.getTagCompound());
                SpiritManager.talk(ep, status.getCharacter(), "first", itemStack);
            }
        } else {
            throw new WrongUsageException("commands.fbs.getspirit.usage", new Object[0]);
        }
    }
}
