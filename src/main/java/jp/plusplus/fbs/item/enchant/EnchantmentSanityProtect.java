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
public class EnchantmentSanityProtect extends Enchantment {
    public EnchantmentSanityProtect(int id, int weight) {
        super(id, weight, EnumEnchantmentType.armor);
        this.setName("fbs.sanity");
    }

    @Override
    public boolean canApply(ItemStack p_92089_1_) {
        if(p_92089_1_.getItem() instanceof ItemStaff) return true;
        return this.type.canEnchantItem(p_92089_1_.getItem());
    }

    @Override
    public int getMinEnchantability(int p_77321_1_) {
        return 3 + (p_77321_1_ - 1) * 6;
    }

    @Override
    public int getMaxEnchantability(int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 6;
    }

    @Override
    public int getMaxLevel()
    {
        return 4;
    }

    public static int getSum(EntityPlayer ep){
        int sum=0;

        for(ItemStack armor : ep.inventory.armorInventory){
            if(armor==null) continue;
            sum+=EnchantmentHelper.getEnchantmentLevel(ItemCore.eIdSanity, armor);
        }
        if(ep.getCurrentEquippedItem()!=null){
            sum+=EnchantmentHelper.getEnchantmentLevel(ItemCore.eIdSanity, ep.getCurrentEquippedItem());
        }

        return sum;
    }
}
