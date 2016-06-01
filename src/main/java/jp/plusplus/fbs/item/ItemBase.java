package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/13.
 */
public class ItemBase extends Item {
    public ItemBase(){
        setCreativeTab(FBS.tab);
    }

    public Item setUnlocalizedName(String s){
        super.setUnlocalizedName("fbs." + s);
        return this;
    }
    public Item setTextureName(String s){
        super.setTextureName(FBS.MODID + ":" + s);
        return this;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(p_77624_1_.getItem()==ItemCore.potionBless){
            p_77624_3_.add(StatCollector.translateToLocal("info.fbs.potionBless.0"));
        }
    }

}
