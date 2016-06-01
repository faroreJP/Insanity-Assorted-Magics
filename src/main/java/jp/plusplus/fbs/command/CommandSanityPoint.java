package jp.plusplus.fbs.command;

import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/01.
 */
public class CommandSanityPoint extends CommandBase {
    @Override
    public String getCommandName() {
        return "sanity";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "commands.fbs.sanity.usage";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length >= 4) {
            World w = p_71515_1_.getEntityWorld();
            EntityPlayer ep = w.getPlayerEntityByName(p_71515_2_[1]);
            if(ep==null) new CommandException("commands.generic.player.notFound", new Object[0]);

            String mode = p_71515_2_[0];
            int t = 0, m = 0;

            try {
                t = Integer.parseInt(p_71515_2_[2]);
            }catch (Exception e) {
                throw new CommandException("commands.generic.num.invalid", new Object[]{t});
            }
            try {
                m = Integer.parseInt(p_71515_2_[3]);
            } catch (Exception e) {
                throw new CommandException("commands.generic.num.invalid", new Object[]{m});
            }

            if (t <= 0) throw new CommandException("commands.generic.num.tooSmall", new Object[]{t, 1});
            if (m <= 0) throw new CommandException("commands.generic.num.tooSmall", new Object[]{m, 1});

            if (mode.equals("lose")) FBSEntityPropertiesAPI.LoseSanity(ep, t, m, true);
            else if (mode.equals("add")) FBSEntityPropertiesAPI.LoseSanity(ep, t, m, true);
            else new WrongUsageException("commands.fbs.sanity.usage", new Object[0]);

        } else {
            throw new WrongUsageException("commands.fbs.sanity.usage", new Object[0]);
        }
    }
}
