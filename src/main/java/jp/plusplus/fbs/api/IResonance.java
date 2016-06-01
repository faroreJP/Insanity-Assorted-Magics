package jp.plusplus.fbs.api;

import jp.plusplus.fbs.Registry;

/**
 * Created by pluslus_F on 2015/06/23.
 * 魔導書の共鳴の判定と情報提供用のインターフェース
 * 独自共鳴はこれを実装してRegistryに登録するといいよ
 */
public interface IResonance {

    /**
     * 同じクラスのインスタンスを返す
     * @return 自身のインスタンス
     */
    public IResonance copy();

    /**
     * 共鳴できるかどうかの判定
     * @param magics スタッフにセットされた魔法
     * @return true:共鳴可
     */
    public boolean isMatch(Registry.MagicData[] magics);

    /**
     * 共鳴後の魔法の登録名を返す
     * @return 共鳴後の魔法の登録名
     */
    public String getResonanceMagicName();

    /**
     * 共鳴後の魔法のクライアント側に表示される名前を返す
     * @param titles 共鳴に使用されている魔法の登録名
     * @return ローカライズ後の表示名
     */
    public String getDisplayMagicName(String[] titles);

    /**
     * 共鳴判定時の優先度を返す
     * @return 優先度(0が最低値)
     */
    public int priority();
}
