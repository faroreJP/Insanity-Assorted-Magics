package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class ItemFortuneCookie extends ItemFood {
    public ItemFortuneCookie() {
        super(2, 0.1f, false);
        setUnlocalizedName("fbs.cookieFortune");
        setTextureName(FBS.MODID + ":cookieFortune");
        setCreativeTab(FBS.tab);
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        super.onEaten(itemStack, world, player);

        //メッセージを出す
        if(!world.isRemote){
            if(world.rand.nextInt(256)==1){
                //願い判定
                player.openGui(FBS.instance, FBS.GUI_WISH_ID, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
            }
            else{
                //ランダムにメッセージ
                String m= Registry.GetRandomMessage();
                int v=Registry.GetRandomMessageVariant(m);
                player.addChatComponentMessage(new ChatComponentText("<"+itemStack.getDisplayName()+">"+Registry.GetLocalizedFortuneCookieMessage(m, v)));
            }
        }

        return itemStack;
    }

    @Override
    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer player) {
        super.onFoodEaten(itemStack, world, player);
    }
}
