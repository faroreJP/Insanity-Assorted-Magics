package jp.plusplus.fbs.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by plusplus_F on 2015/11/08.
 */
public class TabSpirit extends CreativeTabs{
    public TabSpirit(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return ItemCore.stoneActiveMale;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return FBS.NAME+":Contract with Spirit";
    }
}
