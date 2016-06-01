package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class CharacteristicPoison extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicPoison(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.poison."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.LENGTH;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        int d=10+10*getType().getCorrectedValue(value);

        if(reverse) entity.removePotionEffect(Potion.poison.getId());
        else entity.addPotionEffect(new PotionEffect(Potion.poison.getId(), 20*d, 1));
    }

    @Override
    public ChatFormatting getNameColor(){
        return !reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicPoison {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicPoison {
        public Lose(){ super(true); }
    }
}
