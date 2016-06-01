package jp.plusplus.fbs;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

/**
 * Created by plusplus_F on 2015/11/30.
 */
public class AchievementInsanity extends Achievement {
    public AchievementInsanity(String name, int x, int y, ItemStack icon, Achievement parent) {
        super(FBS.MODID+":"+name, "fbs."+name, x, y+3, icon, parent);
        registerStat();
    }
}
