package jp.plusplus.fbs.command;

import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/01.
 */
public class CommandMagicExp extends CommandBase {
    @Override
    public String getCommandName() {
        return "mexp";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.fbs.mexp.usage";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length >= 3) {
            World w = p_71515_1_.getEntityWorld();
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[0]);
            if (ep == null) new CommandException("commands.generic.player.notFound", new Object[1]);

            double t = 0;

            try {
                t = Double.parseDouble(p_71515_2_[1]);
            } catch (Exception e) {
                throw new CommandException("commands.generic.num.invalid", new Object[]{t});
            }
            if (t < 1) throw new CommandException("commands.generic.double.tooSmall", new Object[]{t, 1});

            FBSEntityPropertiesAPI.AddExp(ep, t, true);

        } else {
            throw new WrongUsageException("commands.fbs.mexp.usage", new Object[0]);
        }
    }
}
