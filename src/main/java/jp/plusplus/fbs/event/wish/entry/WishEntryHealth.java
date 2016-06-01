package jp.plusplus.fbs.event.wish.entry;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.event.wish.WishHandler;
import jp.plusplus.fbs.mod.ForSS2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.regex.Pattern;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class WishEntryHealth implements IWishEntry {
    @Override
    public float priority() {
        return 1.f;
    }

    @Override
    public boolean matches(String string) {
        return Pattern.compile(StatCollector.translateToLocal("wish.fbs.health.regex"), Pattern.CASE_INSENSITIVE).matcher(string).matches();
    }

    @Override
    public ItemStack get(EntityPlayer player, String string) {
        player.addChatComponentMessage(new ChatComponentText(WishHandler.getGetMessage("health")));
        player.heal(100);
        player.getFoodStats().addStats(20,1);
        return null;
    }
}
