package jp.plusplus.fbs.event.wish.entry;

import jp.plusplus.fbs.event.wish.WishHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import shift.mceconomy2.api.MCEconomyAPI;

import java.util.regex.Pattern;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class WishEntryMoney implements IWishEntry {
    @Override
    public float priority() {
        return 1.f;
    }

    @Override
    public boolean matches(String string) {
        return Pattern.compile(StatCollector.translateToLocal("wish.fbs.money.regex"), Pattern.CASE_INSENSITIVE).matcher(string).matches();
    }

    @Override
    public ItemStack get(EntityPlayer player, String string) {
        MCEconomyAPI.addPlayerMP(player, (player.experienceLevel/3+1)*8000, false);
        player.addChatComponentMessage(new ChatComponentText(WishHandler.getGetMessage("money")));
        return null;
    }
}
