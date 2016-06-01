package jp.plusplus.fbs.trouble;

import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import shift.sextiarysector.api.SextiarySectorAPI;
import shift.sextiarysector.packet.PacketPlayerData;
import shift.sextiarysector.packet.SSPacketHandler;
import shift.sextiarysector.player.CustomPlayerData;
import shift.sextiarysector.player.EntityPlayerManager;
import shift.sextiarysector.player.MoistureStats;
import shift.sextiarysector.player.StaminaStats;

/**
 * Created by plusplus_F on 2015/09/17.
 */
public class TroubleTiredness extends TroubleBase {
    public TroubleTiredness() {
        super(1);
    }

    @Override
    public void done(World world, EntityPlayer player, Registry.BookData bd) {
        float amount = Math.min(10 + 90.f * bd.lv / 25.f, 100.f)*10;
        SextiarySectorAPI.addStaminaExhaustion(player, amount);
        if(player instanceof EntityPlayerMP) SSPacketHandler.INSTANCE.sendTo(new PacketPlayerData(EntityPlayerManager.getCustomPlayerData(player)), (EntityPlayerMP)player);
    }

    @Override
    public String getMessage(){ return super.getMessage()+".tiredness"; }
}
