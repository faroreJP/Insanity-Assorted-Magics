package jp.plusplus.fbs.spirit;

import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/11/02.
 * 精霊武器であることを示すインターフェース
 */
public interface ISpiritTool {
    /**
     * 攻撃力を計算する
     * @param ss
     * @return
     */
    public float calcDamage(SpiritStatus ss);

    /**
     * 採掘レベルを計算する
     * @param ss
     * @return
     */
    public int calcDigLv(SpiritStatus ss);

    /**
     * 耐久度を計算する
     * @param ss
     * @return
     */
    public int calcDurable(SpiritStatus ss);

    /**
     * 使用した素材アイテムによって、初期値を決定する
     * @param ss
     * @param material
     */
    public void calcInitialValue(SpiritStatus ss, ItemStack material);
}
