package jp.plusplus.fbs.packet;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.storage.MessageMealTerminal;
import jp.plusplus.fbs.storage.MessageMealTerminalScroll;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(FBS.MODID);

    public static void init() {
        INSTANCE.registerMessage(MessagePlayerPropertiesHandler.class, MessagePlayerProperties.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MessagePlayerJoinInAnoucementHandler.class, MessagePlayerJoinInAnnouncement.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessageMagicFlexibleHandler.class, MessageMagicFlexible.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(MessageMagicVortex.Handler.class, MessageMagicVortex.class, 3, Side.CLIENT);
        INSTANCE.registerMessage(MessageGuiButton.Handler.class, MessageGuiButton.class, 4, Side.SERVER);
        INSTANCE.registerMessage(MessageGuiButtonWithString.Handler.class, MessageGuiButtonWithString.class, 5, Side.SERVER);
        INSTANCE.registerMessage(MessageGuiButtonDecide.Handler.class, MessageGuiButtonDecide.class, 6, Side.SERVER);
        INSTANCE.registerMessage(MessageGuiButtonWithNBT.Handler.class, MessageGuiButtonWithNBT.class, 7, Side.SERVER);
        INSTANCE.registerMessage(MessageMealTerminal.Handler.class, MessageMealTerminal.class, 8, Side.SERVER);
        INSTANCE.registerMessage(MessageMealTerminal.HandlerClient.class, MessageMealTerminal.class, 9, Side.CLIENT);
        INSTANCE.registerMessage(MessageMealTerminalScroll.Handler.class, MessageMealTerminalScroll.class, 10, Side.SERVER);
        INSTANCE.registerMessage(MessageWish.Handler.class, MessageWish.class, 11, Side.SERVER);
        INSTANCE.registerMessage(MessagePacketThrower.Handler.class, MessagePacketThrower.class, 12, Side.CLIENT);

    }
}
