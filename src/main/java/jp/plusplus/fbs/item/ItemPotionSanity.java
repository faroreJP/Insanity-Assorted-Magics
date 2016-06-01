package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by pluslus_F on 2015/06/16.
 */
public class ItemPotionSanity extends ItemPotionOblivion {
    public ItemPotionSanity(){
        setUnlocalizedName("fbs.potSan");
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
        return p_82790_2_==0?0x99ff99:0xffffff;
    }

    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
        if (!p_77654_3_.capabilities.isCreativeMode) {
            --p_77654_1_.stackSize;
        }

        if (!p_77654_3_.capabilities.isCreativeMode) {
            if (p_77654_1_.stackSize <= 0) {
                return new ItemStack(Items.glass_bottle);
            }

            p_77654_3_.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }

        return p_77654_1_;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {}

}
