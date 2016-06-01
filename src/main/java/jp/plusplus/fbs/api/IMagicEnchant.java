package jp.plusplus.fbs.api;

import net.minecraft.entity.EntityLivingBase;

/**
 * Created by plusplus_F on 2015/06/22.
 * 付与魔法とするMagicBaseのサブクラスが実装すべきインターフェース
 * このインターフェースを実装していると付与魔法として扱われる
 */
public interface IMagicEnchant {
    /**
     * 対象Entityに何かしらの効果を与える。
     * これは主に共鳴魔法から呼び出される。
     * @param entity 付与対象Entity
     * @param success 魔法の成功可否(true:成功)
     */
    public void enchant(EntityLivingBase entity, boolean success);

    /**
     * ダメージ計算時に実ダメージに乗算される値を返す
     * @param entity 付与対象となるEntity
     * @return 実ダメージ値に乗算される補正値(0以上)
     */
    public float damageScale(EntityLivingBase entity);

    /**
     * ダメージ計算時に実ダメージに加算される値を返す
     * @param entity 付与対象となるEntity
     * @return 実ダメージ値に加算される補正値(0以上)
     */
    public float damageValue(EntityLivingBase entity);

    /**
     * パーティクルの色を設定する
     */
    public ParticleColor setParticleColor();

    public static class ParticleColor{
        public float red,green,blue;
        public ParticleColor(){
            red=green=blue=1;
        }
    }
}
