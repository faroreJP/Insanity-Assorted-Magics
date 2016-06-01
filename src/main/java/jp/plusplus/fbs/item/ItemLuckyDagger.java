package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.EnumHelper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by plusplus_F on 2016/04/01.
 */
public class ItemLuckyDagger extends ItemSword {
    public ItemLuckyDagger() {
        super(EnumHelper.addToolMaterial("fbs.luckyDagger", 0, 1024, 5.0f, 1.5f, 0));
        setUnlocalizedName("fbs.luckyDagger");
        setTextureName(FBS.MODID + ":luckyDagger");
        setCreativeTab(FBS.tab);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(getItemStack());
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.valuable"));
    }

    public static ItemStack getItemStack(){
        ItemStack itemStack=new ItemStack(ItemCore.luckyDagger);
        LinkedHashMap linkedhashmap = new LinkedHashMap();
        linkedhashmap.put(ItemCore.enchantmentWealth.effectId, ItemCore.enchantmentWealth.getMaxLevel());
        linkedhashmap.put(Enchantment.looting.effectId, Enchantment.looting.getMaxLevel());
        linkedhashmap.put(Enchantment.field_151369_A.effectId, Enchantment.field_151369_A.getMaxLevel());
        EnchantmentHelper.setEnchantments(linkedhashmap, itemStack);
        return itemStack;
    }
}
