package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class CharacteristicConfusion extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicConfusion(boolean minus){
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

        if(reverse) entity.removePotionEffect(Potion.confusion.getId());
        else entity.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20*d, 1));
    }

    @Override
    public ChatFormatting getNameColor(){
        return !reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicConfusion {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicConfusion {
        public Lose(){ super(true); }
    }
}
