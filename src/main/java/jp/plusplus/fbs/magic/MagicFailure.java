package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class MagicFailure extends MagicBase {
    @Override
    public boolean checkSuccess() {
        return true;
    }

    @Override
    public void success() {
        //player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.magic.resona.failure"));
    }

    @Override
    public void failure() {
        //player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.magic.resona.failure"));
    }
}
