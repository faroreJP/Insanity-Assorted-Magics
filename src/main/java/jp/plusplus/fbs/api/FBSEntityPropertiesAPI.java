package jp.plusplus.fbs.api;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.enchant.EnchantmentCleverness;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import javax.swing.text.html.parser.Entity;

/**
 * Created by plusplus_F on 2015/09/05.
 * FBS,TFKの独自ステータスに関してはここで弄るといいと思います。
 */
public class FBSEntityPropertiesAPI {

    /**
     * playerの現在の正気度を得る
     * @param player 対象プレイヤー
     * @return 現在の正気度
     */
    public static int GetSanityPoint(EntityPlayer player){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        return prop.getSanity();
    }

    /**
     * playerの最大正気度を得る
     * @param player 対象プレイヤー
     * @return 最大正気度
     */
    public static int GetMaxSanityPoint(EntityPlayer player){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        return prop.getMaxSanity();
    }

    /**
     * プレイヤー1人を対象に発動する。そのプレイヤーに正気度を与える
     * @param player 対象プレイヤー
     * @param trial ダイスを振る回数 (0<)
     * @param max 何面ダイス？ (0<)
     * @param sim 実際に結果を反映させるか否か
     * @return 変化量
     */
    public static int AddSanity(EntityPlayer player, int trial, int max, boolean sim){
        return SanityManager.addSanity(player, trial, max, sim);
    }

    /**
     * プレイヤー1人を対象に発動する。そのプレイヤーの正気度を失わせる
     * @param player 対象プレイヤー
     * @param trial ダイスを振る回数 (0<)
     * @param max 何面ダイス？ (0<)
     * @param sim 実際に結果を反映させるか否か
     * @return 変化量
     */
    public static int LoseSanity(EntityPlayer player, int trial, int max, boolean sim){
        return SanityManager.loseSanity(player, trial, max, sim);
    }

    /**
     * playerの補正無しの魔術レベルを得る
     * @param player 対象プレイヤー
     * @return 魔術レベル
     */
    public static int GetMagicLevelRaw(EntityPlayer player){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        return prop.getMagicLevel();
    }

    /**
     * playerの補正済みの魔術レベルを得る
     * @param player 対象プレイヤー
     * @return 魔術レベル
     */
    public static int GetMagicLevel(EntityPlayer player){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        int l=prop.getMagicLevel();

        //ポーション効果
        int peff;
        if(player.isPotionActive(Registry.potionCleverness)){
            peff=4*(1+player.getActivePotionEffect(Registry.potionCleverness).getAmplifier());
        }
        else{
            peff=0;
        }

        //エンチャント補正
        int eSum= EnchantmentCleverness.getSum(player);
        int eeff;
        if(eSum<=5) eeff=5;
        else if(eSum<=11) eeff=5+MathHelper.floor_float(0.5f*(eSum-5));
        else eeff=8+MathHelper.floor_float(2.f/14.f*(eSum-11));

        //
        return l+peff+eeff;
    }


    /**
     * playerの魔術レベルを設定する
     * @param player 対象プレイヤー
     * @param lv 魔術レベル
     */
    public static void SetMagicLevel(EntityPlayer player, int lv){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        prop.setMagicLevel(lv);
        SanityManager.sendPacket(player);
    }

    /**
     * playerの魔術経験値を得る
     * @param player 対象プレイヤー
     * @return 魔術経験値
     */
    public static double GetMagicEXP(EntityPlayer player){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        return prop.getEXP();
    }

    /**
     * playerのLvUPに必要な魔術経験値を得る
     * @param player 対象プレイヤー
     * @return 必要な魔術経験値
     */
    public static double GetNextMagicEXP(EntityPlayer player){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        return prop.getNext();
    }

    /**
     * 魔術経験値を与える。レベルアップの判定と処理もされる
     * @param player 対象プレイヤー
     * @param exp  (0<)
     * @param sim 実際に結果を反映させるか否か
     * @return 変化量
     */
    public static double AddExp(EntityPlayer player, double exp, boolean sim){
        return SanityManager.addExp(player, exp, sim);
    }

    /**
     * その本が解読したことがあるかどうかを返す
     * @param player 対象プレイヤー
     * @param name 対象の書物
     * @return trueなら解読したことがある
     */
    public static boolean IsBookDecoded(EntityPlayer player, String name){
        return FBSEntityProperties.get(player).isDecoded(name);
    }
}
