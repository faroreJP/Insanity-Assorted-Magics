package jp.plusplus.fbs.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class ItemLavender extends ItemFood{
    public ItemLavender() {
        super(2, 0.2f, false);
        setCreativeTab(FBS.tab);
        setUnlocalizedName("fbs.lavender");
        setTextureName(FBS.MODID+":lavender");
        setAlwaysEdible();
    }
    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
        return super.onEaten(p_77654_1_, p_77654_2_, p_77654_3_);
    }
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer player) {
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        if(prop!=null && prop.getSanity()<prop.getMaxSanity()) {
            player.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        }

        return p_77659_1_;
    }
}
