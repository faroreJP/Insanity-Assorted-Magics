package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

/**
 * Created by plusplus_F on 2015/08/21.
 */
public class ItemShovel extends ItemSword{
    public ItemShovel() {
        super(EnumHelper.addToolMaterial("fbs.shovel", 0, 250, 6.0f, 2.0f, 0));
        setCreativeTab(FBS.tab);
        setUnlocalizedName("fbs.shovel");
        setTextureName(FBS.MODID + ":shovel");
    }

    public static ItemStack GetItemStack(){
        ItemStack it=new ItemStack(ItemCore.shovel, 1, 0);
        it.addEnchantment(Enchantment.smite, 2);

        return it;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(GetItemStack());
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        list.add(StatCollector.translateToLocal("info.fbs.shovel.0"));
    }
}
