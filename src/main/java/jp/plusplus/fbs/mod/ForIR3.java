package jp.plusplus.fbs.mod;

import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.BlockHerb;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.ir2.Recipes;
import jp.plusplus.ir2.api.IR3RecipeAPI;
import jp.plusplus.ir2.api.ItemCrystalUnit;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class ForIR3 {
    public static void setup(){
        for(int i=0;i<16;i++) IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.charm, 1, i), 1, 30);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.stoneInactive), 20, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.stoneActive), 20, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.stoneActiveMale), 20, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.stoneActiveFemale), 20, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.membership), 5, 30);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.monocle), 15, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.cloak), 80, 8);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.bookBroken), 1, 30);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.bookWhite), 10, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.bookmark), 3, 30);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.magicCore), 5, 30);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.mirror), 20, 5);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.plank), 1, 30);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.workbench), 3, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.extractingFurnace), 3, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.fillingTable), 3, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.enchantScroll), 3, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff1_1), 10, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff1_2), 12, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff1_3), 14, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff1_4), 16, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff1_5), 18, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff2_1), 14, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff2_2), 18, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff2_3), 22, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staff2_4), 26, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staffHead1), 6, 25);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staffHead2), 8, 25);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staffHead3), 10, 25);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staffHead4), 12, 25);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(ItemCore.staffHead5), 14, 25);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.portal2), 5, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.alchemyCauldron), 20, 1);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_MAGIC, new ItemStack(BlockCore.tableAlchemist), 12, 1);

        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.lavender), 3, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.seedLavender), 3, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.redLily), 3, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(ItemCore.seedRedLily), 3, 20);
        for(int i=0;i<9;i++) IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(BlockCore.harvestableHerb, 1, i), 20, 20);
        for(int i=0;i<3;i++) IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(BlockCore.harvestableMushroom, 1, i), 20, 20);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_PLANT, new ItemStack(BlockCore.harvestableGrass), 20, 20);

        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(ItemCore.shovel), 18, 50);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(ItemCore.foldingFan), 22, 50);
        IR3RecipeAPI.AddComposition(IR3RecipeAPI.COMPOSITION_TOOL, new ItemStack(ItemCore.luckyDagger), 64, 30);
    }

    public static boolean isCrystalUnit(ItemStack itemStack){
        return itemStack.getItem() instanceof ItemCrystalUnit;
    }
    public static void repairCrystalUnit(ItemStack itemStack, float percent){
        ItemCrystalUnit icu=(ItemCrystalUnit)itemStack.getItem();
        double d=icu.getDamageNBT(itemStack);
        if(d==0) return;
        if(percent>1) percent=1;
        if(percent<0) percent=0;

        d=Math.max(0, d-icu.maxDamageNBT*percent);
        icu.setDamageNBT(itemStack, d);
    }
}
