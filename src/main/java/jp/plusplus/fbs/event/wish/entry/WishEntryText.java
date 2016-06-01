package jp.plusplus.fbs.event.wish.entry;

import jp.plusplus.fbs.event.wish.WishHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class WishEntryText implements IWishEntry {
    protected String key;
    public WishEntryText(String key){
        this.key=key;
    }

    @Override
    public float priority() {
        return 1.f;
    }

    @Override
    public boolean matches(String string) {
        return Pattern.compile(StatCollector.translateToLocal("wish.fbs."+key+".regex"), Pattern.CASE_INSENSITIVE).matcher(string).matches();
    }

    @Override
    public ItemStack get(EntityPlayer player, String string) {
        player.addChatComponentMessage(new ChatComponentText(WishHandler.getGetMessage(key)));
        return null;
    }
}
