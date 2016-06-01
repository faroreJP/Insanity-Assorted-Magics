package jp.plusplus.fbs;

import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.ItemAlchemyIntermediateMaterial;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicQuality;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class RecipeBladeSpice implements IRecipe {
    public static final String AMOUNT="fbs.bladeSpiceAmount";
    public static final String EFFECTS="fbs.bladeSpiceEffect";

    private ItemStack result;
    @Override
    public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
        result = null;
        int size = p_77569_1_.getSizeInventory();

        //剣と刃薬を探す
        ItemStack sword = null;
        NBTTagCompound now=null;
        ArrayList<CharacteristicBase> cbList = null;
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = p_77569_1_.getStackInSlot(i);
            if (itemStack == null) continue;
            Item item = itemStack.getItem();

            if (item instanceof ItemSword) {
                if (sword != null) return false;

                now = itemStack.getTagCompound();
                if (now == null) {
                    sword = itemStack;
                    now = new NBTTagCompound();
                } else if (!now.hasKey(AMOUNT)) {
                    sword = itemStack;
                }
            } else if (item instanceof ItemAlchemyIntermediateMaterial && itemStack.getItemDamage() == 5) {
                if (cbList != null) return false;
                cbList = AlchemyRegistry.ReadCharacteristicFromNBT(itemStack.getTagCompound());
            } else {
                //剣でもなく刃薬以外なら弾く
                return false;
            }
        }
        if (sword == null || now==null || cbList == null || cbList.isEmpty()) return false;

        result = new ItemStack(sword.getItem(), sword.stackSize, sword.getItemDamage());
        NBTTagCompound nbt = (NBTTagCompound)now.copy();
        int amount=16;

        //書き込む特性と、特性が持続する回数を決定する
        ArrayList<CharacteristicBase> cbs=new ArrayList<CharacteristicBase>();
        for(CharacteristicBase cb : cbList){
            if(cb instanceof CharacteristicQuality){
                amount=(int)(amount*cb.getMPScale());
            }
            else{
                cbs.add(cb);
            }
        }

        //書き込む
        NBTTagCompound tagCB=new NBTTagCompound();
        nbt.setInteger(AMOUNT, amount);
        AlchemyRegistry.WriteCharacteristicToNBT(tagCB, cbs);
        nbt.setTag(EFFECTS, tagCB);
        result.setTagCompound(nbt);

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

    public static ArrayList<CharacteristicBase> getCharacteristics(ItemStack sword){
        if(sword.getItem() instanceof ItemSword && sword.getTagCompound()!=null && sword.getTagCompound().hasKey(RecipeBladeSpice.AMOUNT)){
            return AlchemyRegistry.ReadCharacteristicFromNBT(sword.getTagCompound().getCompoundTag(RecipeBladeSpice.EFFECTS));
        }
        return new ArrayList<CharacteristicBase>();
    }

    public static void consumeBladeSpiceAmount(ItemStack sword){
        if(sword.getItem() instanceof ItemSword){
            NBTTagCompound nbt=sword.getTagCompound();
            if(nbt==null || !nbt.hasKey(AMOUNT)) return;

            int t=nbt.getInteger(AMOUNT)-1;
            if(t>0){
                nbt.removeTag(AMOUNT);
                nbt.setInteger(AMOUNT, t);
            }
            else{
                nbt.removeTag(AMOUNT);
                nbt.removeTag(EFFECTS);
            }
        }
    }
}
