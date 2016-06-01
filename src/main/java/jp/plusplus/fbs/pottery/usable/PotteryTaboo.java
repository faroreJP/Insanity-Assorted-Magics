package jp.plusplus.fbs.pottery.usable;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public class PotteryTaboo extends PotteryUsableLimitted {

    @Override
    public float getPriceScale(ItemStack pottery){
        return 3.5f*super.getPriceScale(pottery);
    }

    @Override
    public void effect(EntityPlayer player, ItemStack pottery) {
        if(player.worldObj.isRemote) return;

        FBSEntityProperties properties=FBSEntityProperties.get(player);

        int now=properties.getSanity();
        properties.setSanity(now/2+1);
        int san=now-properties.getSanity();
        if(san>0){
            player.addChatComponentMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("info.fbs.san.1"), san)));
            player.triggerAchievement(AchievementRegistry.insanity);

            if(san>=2 && 100*san/now>=20){
                //発狂判定
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("info.fbs.san.2")));
                player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 15, 2));
                player.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 20*15, 1));
                player.triggerAchievement(AchievementRegistry.madness);
            }
        }
        SanityManager.sendPacket(player);

        if(!player.isDead){
            IPottery ip=(IPottery)((ItemBlock)pottery.getItem()).field_150939_a;

            int dur=20*30*(ip.getGrade(pottery.getTagCompound()).getValue()+1);

            player.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), dur, 2));
            player.addPotionEffect(new PotionEffect(Potion.resistance.getId(), dur, 2));
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), dur, 2));
        }
    }

    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.taboo";
    }

    public void onCrash(EntityPlayer player, ItemStack pottery){
        if(player.worldObj.isRemote) return;
        SanityManager.loseSanity(player, 10, 100, true);
    }
}
