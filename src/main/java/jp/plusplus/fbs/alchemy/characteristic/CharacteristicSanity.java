package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import jp.plusplus.fbs.exprop.SanityManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class CharacteristicSanity extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicSanity(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.sanity."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.SCALE;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        if(!(entity instanceof EntityPlayer)) return;

        int t=getType().getCorrectedValue(value);
        if(reverse) SanityManager.loseSanity((EntityPlayer)entity, 1+t, 6, true);
        else SanityManager.addSanity((EntityPlayer) entity, 1 + t, 6, true);
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicSanity {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicSanity {
        public Lose(){ super(true); }
    }
}
