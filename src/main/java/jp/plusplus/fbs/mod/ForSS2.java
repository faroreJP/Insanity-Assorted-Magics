package jp.plusplus.fbs.mod;

import net.minecraft.item.ItemStack;
import shift.sextiarysector.api.recipe.RecipeAPI;

/**
 * Created by plusplus_F on 2016/02/28.
 */
public class ForSS2 {
    public static void setup(){

    }

    public static boolean canTimeTrace(ItemStack itemStack){
        return RecipeAPI.timeMachine.getResult(itemStack)!=null;
    }

    public static ItemStack getTimeTraced(ItemStack itemStack){
        return RecipeAPI.timeMachine.getResult(itemStack);
    }
}
