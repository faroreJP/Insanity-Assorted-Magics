package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
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
 * Created by plusplus_F on 2015/09/24.
 */
public class CharacteristicStamina extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicStamina(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.stamina."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.SCALE;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        if(!(entity instanceof EntityPlayer)) return;
        int d=30+30*getType().getCorrectedValue(value);

        EntityPlayer ep=(EntityPlayer) entity;

        if(reverse) SextiarySectorAPI.addStaminaExhaustion(ep, d * 10);
        else SextiarySectorAPI.addStaminaStats(ep, d, 0.1f);

        if(ep instanceof EntityPlayerMP) SSPacketHandler.INSTANCE.sendTo(new PacketPlayerData(EntityPlayerManager.getCustomPlayerData(ep)), (EntityPlayerMP)ep);
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicStamina {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicStamina {
        public Lose(){ super(true); }
    }
}
