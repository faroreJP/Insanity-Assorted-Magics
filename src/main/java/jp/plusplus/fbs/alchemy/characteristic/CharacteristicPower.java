package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class CharacteristicPower extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicPower(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.power."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.LENGTH;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        int lv=getType().getCorrectedValue(value);
        int d=30+30*lv;

        if(reverse){
            entity.removePotionEffect(Potion.damageBoost.getId());
            entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 20*d, lv+1));
        }
        else{
            entity.removePotionEffect(Potion.weakness.getId());
            entity.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 20*d, lv+1));
        }
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicPower {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicPower {
        public Lose(){ super(true); }
    }
}
