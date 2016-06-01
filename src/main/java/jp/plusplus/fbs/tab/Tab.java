package jp.plusplus.fbs.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class Tab  extends CreativeTabs{
    public Tab(String label) {
        super(label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return ItemCore.butterfly;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        //return FBS.NAME+":Crucible of Sorcery";
        return FBS.NAME+":Assorted Magics";
    }
}
