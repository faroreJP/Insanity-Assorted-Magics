package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/24.
 */
public class CharacteristicExp extends CharacteristicBase {
    protected  boolean reverse;
    public CharacteristicExp(boolean minus){
        reverse=minus;
        setUnlocalizedName("fbs.exp."+(reverse?"lose":"gain"));
    }

    @Override
    public Type getType() {
        return Type.SCALE;
    }

    @Override
    public void affectEntity(World world, EntityLivingBase entity){
        if(!(entity instanceof EntityPlayer)) return;
        EntityPlayer ep=(EntityPlayer)entity;

        int d=6+6*getType().getCorrectedValue(value);

        if(reverse) {
            ep.experience -= (float) d / (float) ep.xpBarCap();
            for (ep.experienceTotal -= d; ep.experience < 0.0F; ep.experience /= (float) ep.xpBarCap()) {
                ep.experience = (1.0f - ep.experience) * (float) ep.xpBarCap();
                ep.addExperienceLevel(-1);
            }
        }
        else ((EntityPlayer) entity).addExperience(d);
    }

    @Override
    public ChatFormatting getNameColor(){
        return reverse?ChatFormatting.RED:ChatFormatting.DARK_GREEN;
    }

    public static class Gain extends CharacteristicExp {
        public Gain(){ super(false); }
    }
    public static class Lose extends CharacteristicExp {
        public Lose(){ super(true); }
    }
}
