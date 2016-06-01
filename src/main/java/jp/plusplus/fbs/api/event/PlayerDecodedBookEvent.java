package jp.plusplus.fbs.api.event;

import jp.plusplus.fbs.Registry;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Createdby pluslus_Fon 2015/06/14.
 * プレイヤーが魔導書を使用した際に呼ばれるイベント
 */
public class PlayerDecodedBookEvent extends PlayerEvent {
    private Registry.BookData bookData;
    private boolean success;

    public PlayerDecodedBookEvent(EntityPlayer player, Registry.BookData bd, boolean success) {
        super(player);
        bookData=bd.copy();
        this.success=success;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    /**
     * 使用した書物
     * @return
     */
    public Registry.BookData getBook(){ return bookData; }

    /**
     * 魔法の行使に成功したか
     * @return
     */
    public boolean isSuccess(){ return success; }
}
