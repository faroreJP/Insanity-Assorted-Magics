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
public class CommandMagicLevel extends CommandBase {
    @Override
    public String getCommandName() {
        return "mlevel";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.fbs.mlevel.usage";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length >= 3) {
            World w = p_71515_1_.getEntityWorld();
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[1]);
            if (ep == null) new CommandException("commands.generic.player.notFound", new Object[1]);

            String mode = p_71515_2_[0];
            int t = 0;

            try {
                t = Integer.parseInt(p_71515_2_[2]);
            } catch (Exception e) {
                throw new CommandException("commands.generic.num.invalid", new Object[]{t});
            }
            if (t < 1) throw new CommandException("commands.generic.num.tooSmall", new Object[]{t, 1});

            if (mode.equals("set")) FBSEntityPropertiesAPI.SetMagicLevel(ep, t);
            else if (mode.equals("add")) FBSEntityPropertiesAPI.SetMagicLevel(ep, FBSEntityPropertiesAPI.GetMagicLevelRaw(ep)+t);
            else new WrongUsageException("commands.fbs.mlevel.usage", new Object[0]);

        } else {
            throw new WrongUsageException("commands.fbs.mlevel.usage", new Object[0]);
        }
    }
}
