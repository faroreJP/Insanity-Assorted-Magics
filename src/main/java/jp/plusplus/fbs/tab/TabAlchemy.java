package jp.plusplus.fbs.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;

/**
 * Created by plusplus_F on 2015/08/26.
 */
public class TabAlchemy extends CreativeTabs{
    public TabAlchemy(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return ItemCore.lavender;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return FBS.NAME+":Secret Art of Alchemy";
    }
}
