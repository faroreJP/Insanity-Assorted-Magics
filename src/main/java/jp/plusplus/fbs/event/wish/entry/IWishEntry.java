package jp.plusplus.fbs.event.wish.entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public interface IWishEntry {
    /**
     * 0.0f-1.0fの範囲で優先度を返す<br>
     * 数値が大きいほど判定が優先される
     * @return
     */
    public float priority();

    /**
     * プレイヤーの願いと一致するか判定する
     * @param string 願い事
     * @return 一致すればtrue
     */
    public boolean matches(String string);

    /**
     * 願いごとを叶える処理
     * @param player 願ったプレイヤー
     * @param string 願い事
     * @return プレイヤーに与えるItemStack (Nullable)
     */
    public ItemStack get(EntityPlayer player, String string);
}
