package jp.plusplus.fbs.pottery.usable;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shift.sextiarysector.api.SextiarySectorAPI;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public abstract class PotteryUsableLimitted extends PotteryBase {
    public static final String USE_COUNT="UseCount";
    public Random rand=new Random();

    /**
     * 壺そのものの効果
     * @param player 所有者
     * @param pottery 壺のアイテムスタック
     */
    public abstract void effect(EntityPlayer player, ItemStack pottery);

    @Override
    public String getNameModifier(ItemStack pottery){
        return "["+pottery.getTagCompound().getInteger(USE_COUNT)+"]";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 0.8f+0.1f*pottery.getTagCompound().getInteger(USE_COUNT);
    }

    @Override
    public void onBaked(ItemStack pottery){
        IPottery ip=(IPottery)((ItemBlock) pottery.getItem()).field_150939_a;

        int c;
        switch (ip.getSize(pottery.getTagCompound())){
            case SMALL: c=2; break;
            case LARGE: c=8; break;
            default: c=5; break;
        }
        c+=rand.nextInt(c/2);

        switch (ip.getGrade(pottery.getTagCompound())){
            case BAD: c=(int)(c*0.8); break;
            case GOOD: c=(int)(c*1.5); break;
            case GREAT: c=(int)(c*2); break;
            case SOULFUL: c=(int)(c*3); break;
            default: break;
        }

        pottery.getTagCompound().setInteger(USE_COUNT, c);
    }

    @Override
    public ItemStack onUse(EntityPlayer player, ItemStack pottery) {
        int c=pottery.getTagCompound().getInteger(USE_COUNT);
        if(c>0){
            effect(player, pottery);
            if(!player.capabilities.isCreativeMode) pottery.getTagCompound().setInteger(USE_COUNT, c-1);
            player.swingItem();
        }
        return pottery;
    }
}
