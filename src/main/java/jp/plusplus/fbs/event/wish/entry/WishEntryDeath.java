package jp.plusplus.fbs.event.wish.entry;

import jp.plusplus.fbs.event.wish.WishHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

import java.util.regex.Pattern;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class WishEntryDeath implements IWishEntry {
    @Override
    public float priority() {
        return 1.f;
    }

    @Override
    public boolean matches(String string) {
        return Pattern.compile(StatCollector.translateToLocal("wish.fbs.death.regex"), Pattern.CASE_INSENSITIVE).matcher(string).matches();
    }

    @Override
    public ItemStack get(EntityPlayer player, String string) {
        player.addChatComponentMessage(new ChatComponentText(WishHandler.getGetMessage("death")));
        player.attackEntityFrom(DamageSource.generic, 99999);
        player.setDead();
        return null;
    }
}
