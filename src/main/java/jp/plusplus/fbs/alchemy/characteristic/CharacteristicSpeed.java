package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/02/23.
 */
public class CharacteristicSpeed extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicSpeed(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.speed."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.LENGTH;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity) {
        int d = 10 + 10 * getType().getCorrectedValue(value);

        if (reverse) {
            entity.removePotionEffect(Potion.moveSlowdown.getId());
            entity.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 20 * d, 1));
        } else {
            entity.removePotionEffect(Potion.moveSpeed.getId());
            entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 20 * d, 1));
        }
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicSpeed {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicSpeed {
        public Lose(){ super(true); }
    }
}
