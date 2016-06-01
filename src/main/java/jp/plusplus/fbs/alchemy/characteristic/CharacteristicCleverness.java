package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import jp.plusplus.fbs.Registry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/20.
 */
public class CharacteristicCleverness extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicCleverness(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.cleverness."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.LENGTH;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        int lv=getType().getCorrectedValue(value);
        int d=60+60*lv;

        if(reverse) entity.removePotionEffect(Potion.poison.getId());
        else entity.addPotionEffect(new PotionEffect(Registry.potionCleverness.getId(), 20*d, lv+1));
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicCleverness {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicCleverness {
        public Lose(){ super(true); }
    }
}
