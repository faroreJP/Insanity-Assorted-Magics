package jp.plusplus.fbs.alchemy;

import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.world.biome.WorldGenDirtyOak;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/09/08.
 * 素材アイテムが実装すべきインターフェース
 */
public interface IAlchemyMaterial {
    /**
     * 鑑定時に特性を付与するための特性リストをランダムで生成する。
     * 戻り値の内容が全てItemStackに付与される
     * @param itemStack
     * @param rand
     * @return アイテムスタックに付与される特性(Nullable)
     */
    ArrayList<CharacteristicBase> addCharacteristics(ItemStack itemStack, Random rand);
}
