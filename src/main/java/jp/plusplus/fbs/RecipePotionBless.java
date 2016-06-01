package jp.plusplus.fbs;

import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.ItemAlchemyIntermediateMaterial;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicQuality;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class RecipePotionBless implements IRecipe {
    public static final int LV=2;

    private ItemStack result;
    @Override
    public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
        result = null;
        int size = p_77569_1_.getSizeInventory();

        //装備と祝福ポーションを探す
        ItemStack tool = null;
        ItemStack pot=null;
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = p_77569_1_.getStackInSlot(i);
            if (itemStack == null) continue;
            Item item = itemStack.getItem();

            if (item.isItemTool(itemStack) && item.getItemEnchantability(itemStack)>0) {
                if (tool != null) return false;
                tool=itemStack;
                if(tool.isItemEnchanted()){
                    Map m= EnchantmentHelper.getEnchantments(tool);
                    if(m.containsKey(Enchantment.unbreaking.effectId)){
                        return false;
                    }
                }
            } else if (item== ItemCore.potionBless) {
                if(pot==null) pot=itemStack;
                else return false;
            } else {
                return false;
            }
        }
        if (tool == null || pot==null) return false;

        result = new ItemStack(tool.getItem(), tool.stackSize, tool.getItemDamage());
        if(tool.hasTagCompound()) result.setTagCompound((NBTTagCompound)tool.getTagCompound().copy());
        else result.setTagCompound(new NBTTagCompound());

        Map m=null;
        if(result.isItemEnchanted()) m=EnchantmentHelper.getEnchantments(result);
        else m=new LinkedHashMap();

        m.put(Enchantment.unbreaking.effectId, LV);
        EnchantmentHelper.setEnchantments(m, result);
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return result;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }
}
