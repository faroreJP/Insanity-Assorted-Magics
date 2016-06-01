package jp.plusplus.fbs.item.enchant;

import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemStaff;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by nori on 2016/03/17.
 */
public class EnchantmentWealth extends Enchantment {
    public EnchantmentWealth(int id, int weight) {
        super(id, weight, EnumEnchantmentType.weapon);
        this.setName("fbs.wealth");
    }

    @Override
    public int getMinEnchantability(int p_77321_1_)
    {
        return 15 + (p_77321_1_ - 1) * 9;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_)
    {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }

    @Override
    public int getMaxLevel()
    {
        return 3;
    }

    public static int getSum(EntityPlayer ep){
        int sum=0;

        if(ep.getCurrentEquippedItem()!=null){
            sum+=EnchantmentHelper.getEnchantmentLevel(ItemCore.eIdWealth, ep.getCurrentEquippedItem());
        }

        return sum;
    }
}
