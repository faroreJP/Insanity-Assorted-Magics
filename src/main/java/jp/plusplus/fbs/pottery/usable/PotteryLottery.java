package jp.plusplus.fbs.pottery.usable;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/04/02.
 */
public class PotteryLottery extends PotteryUsableLimitted {
    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.lottery";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 0.8f*super.getPriceScale(pottery);
    }

    @Override
    public void effect(EntityPlayer player, ItemStack pottery) {
        World world=player.worldObj;

        if(!world.isRemote){
            if(world.rand.nextInt(256)==1){
                //願い判定
                player.openGui(FBS.instance, FBS.GUI_WISH_ID, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
            }
            else{
                //ランダムにメッセージ
                String m= Registry.GetRandomMessage();
                int v=Registry.GetRandomMessageVariant(m);
                player.addChatComponentMessage(new ChatComponentText("<"+ StatCollector.translateToLocal(getUnlocalizedName())+">"+Registry.GetLocalizedFortuneCookieMessage(m, v)));
            }
        }
    }

}
