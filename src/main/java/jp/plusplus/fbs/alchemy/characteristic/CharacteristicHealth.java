package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class CharacteristicHealth extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicHealth(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.life."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.SCALE;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        float d=6+6*getType().getCorrectedValue(value);

        if(reverse) entity.attackEntityFrom(new DamageSource(""), d);
        else entity.heal(d);
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicHealth{
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicHealth{
        public Lose(){ super(true); }
    }
}
