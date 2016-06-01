package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shift.sextiarysector.api.SextiarySectorAPI;
import shift.sextiarysector.packet.PacketPlayerData;
import shift.sextiarysector.packet.SSPacketHandler;
import shift.sextiarysector.player.CustomPlayerData;
import shift.sextiarysector.player.EntityPlayerManager;
import shift.sextiarysector.player.MoistureStats;

/**
 * Created by plusplus_F on 2015/09/24.
 */
public class CharacteristicWater extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicWater(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.water."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.SCALE;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        if(!(entity instanceof EntityPlayer)) return;
        int d=6+6*getType().getCorrectedValue(value);

        CustomPlayerData cpd= EntityPlayerManager.getCustomPlayerData((EntityPlayer)entity);
        if(cpd==null) return;
        MoistureStats ms=cpd.getMoisture();

        EntityPlayer ep=(EntityPlayer) entity;

        if(reverse) SextiarySectorAPI.addMoistureExhaustion(ep, d*2);
        else SextiarySectorAPI.addMoistureStats(ep, d, 0.1f);

        if(ep instanceof EntityPlayerMP) SSPacketHandler.INSTANCE.sendTo(new PacketPlayerData(EntityPlayerManager.getCustomPlayerData(ep)), (EntityPlayerMP) ep);
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicWater {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicWater {
        public Lose(){ super(true); }
    }
}
