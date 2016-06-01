package jp.plusplus.fbs.pottery.usable;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shift.sextiarysector.api.SextiarySectorAPI;

import javax.annotation.Nullable;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotterySenaka extends PotteryUsableLimitted {
    public static final String USE_COUNT="UseCount";

    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.senaka";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 2.f*super.getPriceScale(pottery);
    }

    @Override
    public void effect(EntityPlayer player, ItemStack pottery) {
        player.heal(player.getMaxHealth());
        player.getFoodStats().addStats(20, 1);
        if(FBS.cooperatesSS2) forSS2(player);
    }

    protected void forSS2(EntityPlayer player){
        SextiarySectorAPI.addMoistureStats(player, 20, 1);
        SextiarySectorAPI.addStaminaStats(player, 100, 1);
    }
}
