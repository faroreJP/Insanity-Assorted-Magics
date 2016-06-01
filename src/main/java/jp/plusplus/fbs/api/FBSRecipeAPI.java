package jp.plusplus.fbs.api;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * Created by plusplus_F on 2015/09/05.
 * 外部からレシピをどうにかしたいときに使ってください。
 * 別にRegistryを直接叩いてもいいけどね。
 */
public class FBSRecipeAPI {
    /**
     * アイテムに対し抽出炉で抽出できる魔力量を登録する
     * @param item
     * @param amount 正の数
     */
    public static void AddManaContainer(ItemStack item, int amount){
        if(amount<=0){
            FBS.logger.error("Error:fault adding mana container. " + item.getDisplayName() + "(" + amount + ")");
            return;
        }
        Registry.RegisterManaContainer(item, amount);
    }

    /**
     * 魔術師の作業台でのクラフトレシピを登録する
     * @param recipe 登録レシピ
     * @param amount 消費魔力量(正の数)
     */
    public static void AddCrafting(IRecipe recipe, int amount){
        if(amount<=0){
            FBS.logger.error("Error:fault adding crafting. " + recipe.getRecipeOutput().getDisplayName() + "(" + amount + ")");
            return;
        }
        Registry.RegisterRecipe(recipe, amount);
    }

    /**
     * 古書を登録する
     * @param title 書物のUnlocalizedタイトル
     * @param lv 適性魔術レベル(0<lv)
     * @param prob 解読成功確率(0<prob<=1)
     * @param exp 基本経験値(0<=exp)
     * @param sanTrial 正気度ロールXdYのX(0<=)
     * @param sanMax 正気度ロールXdYのY(0<=)
     * @param weight 出現時の重み(基本値10)
     */
    public static boolean AddBook(String title, int lv, float prob, double exp, int sanTrial, int sanMax, int weight){
        if(title==null || lv<=0 || prob<=0 || exp<0 || sanTrial<0 || sanMax<0 || weight<0){
            FBS.logger.error("Error:fault adding book. " + title + "(lv" + lv + ",prob" + prob + ",exp" + exp + "," + sanTrial + "d" + sanMax + ",weight" + weight);
            return false;
        }
        Registry.RegisterBook(title, lv, false, prob, exp, sanTrial, sanMax, weight);
        return true;
    }

    /**
     * 魔法を登録する。書物も一緒に登録される
     * @param title 書物のUnlocalizedタイトル
     * @param lv 適性魔術レベル(0<lv)
     * @param prob 解読成功確率(0<prob<=1)
     * @param exp 基本経験値(0<=exp)
     * @param sanTrial 正気度ロールXdYのX(0<=)
     * @param sanMax 正気度ロールXdYのY(0<=)
     * @param weight 出現時の重み(基本値10)
     * @param type 魔法の種類
     * @param aria 詠唱tick数
     * @param exp2 魔法行使の基本経験値
     * @param minUse 解読時設定される使用回数の最小
     * @param maxUse 解読時設定される使用回数の最大
     * @param magic 魔法のクラス
     */
    public static void AddMagic(String title, int lv, float prob, double exp, int sanTrial, int sanMax, int weight, String type, int aria, double exp2, int minUse, int maxUse, Class<? extends MagicBase> magic){
        if(title==null || lv<=0 || prob<=0 || exp<0 || sanTrial<0 || sanMax<0 || weight<0){
            FBS.logger.error("Error:fault adding book. " + title + "(lv" + lv + ",prob" + prob + ",exp" + exp + "," + sanTrial + "d" + sanMax + ",weight" + weight);
            return;
        }
        Registry.RegisterBook(title, lv, true, prob, exp, sanTrial, sanMax, weight);

        if(type==null || aria<=0 || exp2<=0 || minUse<0 || maxUse<minUse || magic==null){
            FBS.logger.error("Error:fault adding magic. " + title + "(aria" + aria + ",exp2" + exp2 + ",use" + minUse + "..." + maxUse + "," + magic.toString());
            return;
        }
        Registry.RegisterMagic(title, type, aria, exp2, minUse, maxUse, magic);
    }

    /**
     * TFKの構造物のチェスト内容を登録する
     * @param id 封印された図書館:0
     * @param item
     * @param weight 出現の重み(0<)
     */
    public static void AddChestContent(int id, ItemStack item, int weight){
        if(id<0 || item==null || weight<=0){
            FBS.logger.error("Error:fault adding chest content. id" + id + "," + item.getDisplayName() + ",weight" + weight);
            return;
        }
        Registry.RegisterChestContent(id, item, weight);
    }

    /**
     * 魔法陣を登録する。
     * 魔法陣は文字列の配列で表され、n*nのサイズである必要がある(nは奇数)。
     * 使用できる文字は0~9,a-fであり、これは各チャームのメタ値に対応している。
     * また、空白' 'も使用でき、これはそこにはチャームが無いことを示す。
     * 魔法陣の中央は常に核が置かれるため、中央の値は無視される。
     * @param name 登録名
     * @param charms 魔法陣のチャーム配置
     */
    public static void AddMagicCircle(String name, String ... charms){
        if(name==null || charms==null){
            FBS.logger.error("Error:fault adding chest content. " + name + "," + charms);
            return;
        }
        Registry.RegisterMagicCircle(name, charms);
    }

    /**
     * アイテム使用時のプレイヤーの正気度の変化量を登録する。
     * @param item
     * @param trial  正気度ロールXdYのX(0<)
     * @param max 正気度ロールXdYのY(!=0)
     */
    public static void AddItemSanity(ItemStack item, int trial, int max){
        if(item==null || trial<=0 || max==0){
            FBS.logger.error("Error:fault registering item. " + item.getDisplayName()+","+trial+"d"+max);
            return;
        }
        Registry.RegisterItemSanity(item, trial, max);
    }

    /**
     * Mob攻撃時のプレイヤーの正気度の変化量を登録する。
     * @param type Mobのクラス
     * @param trial  正気度ロールXdYのX(0<)
     * @param max 正気度ロールXdYのY(!=0)
     */
    public static void AddMobSanity(Class<? extends IMob> type, int trial, int max){
        if(type==null || trial<=0 || max==0){
            FBS.logger.error("Error:fault registering mob. " + type.toString()+","+trial+"d"+max);
            return;
        }
        Registry.RegisterMobSanity(type, trial, max);
    }
}
