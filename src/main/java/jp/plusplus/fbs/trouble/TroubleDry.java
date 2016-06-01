package jp.plusplus.fbs.trouble;

import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import shift.sextiarysector.api.SextiarySectorAPI;
import shift.sextiarysector.packet.PacketPlayerData;
import shift.sextiarysector.packet.SSPacketHandler;
import shift.sextiarysector.player.CustomPlayerData;
import shift.sextiarysector.player.EntityPlayerManager;
import shift.sextiarysector.player.MoistureStats;

/**
 * Created by plusplus_F on 2015/09/17.
 */
public class TroubleDry extends TroubleBase {
    public TroubleDry() {
        super(1);
    }

    @Override
    public void done(World world, EntityPlayer player, Registry.BookData bd) {
        float amount = Math.min(4 + 36.f * bd.lv / 25.f, 40.f);
        SextiarySectorAPI.addMoistureExhaustion(player, amount);
        if(player instanceof EntityPlayerMP) SSPacketHandler.INSTANCE.sendTo(new PacketPlayerData(EntityPlayerManager.getCustomPlayerData(player)), (EntityPlayerMP)player);
    }

    @Override
    public String getMessage(){ return super.getMessage()+".dry"; }
}
