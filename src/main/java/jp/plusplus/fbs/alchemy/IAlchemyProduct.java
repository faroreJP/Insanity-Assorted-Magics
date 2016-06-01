package jp.plusplus.fbs.alchemy;

import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/09/09.
 * 大釜によって作成できるアイテムに実装すべきインターフェース
 */
public interface IAlchemyProduct {
    /**
     * その特性を引き継げるか判定する
     * @param cb 判定したい特性
     * @return true:引継ぎ可能
     */
    boolean canInherit(ItemStack itemStack, CharacteristicBase cb);

    /**
     * 特性引継ぎ時に、引継ぎ可能な特性の数を返す
     * @param itemStack
     * @return
     */
    int getMaxInheritAmount(ItemStack itemStack);

    /**
     * 調合した際に、最初から付与されている特性リストを返す
     * (このメソッドでは特性を付与しない！)
     * @param itemStack
     * @return
     */
    ArrayList<CharacteristicBase> getDefaultCharacteristics(ItemStack itemStack, Random rand);
}
