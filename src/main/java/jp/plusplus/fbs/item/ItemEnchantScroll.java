package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/10/19.
 */
public class ItemEnchantScroll extends ItemBase {
    public ItemEnchantScroll(){
        setMaxStackSize(1);
        setUnlocalizedName("enchantScroll");
        setTextureName("enchantScroll");
        setMaxDamage(100);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if(!p_77659_2_.isRemote){
            p_77659_3_.openGui(FBS.instance, FBS.GUI_ENCHANTMENT_ID, p_77659_2_, MathHelper.floor_double(p_77659_3_.posX), MathHelper.floor_double(p_77659_3_.posY), MathHelper.floor_double(p_77659_3_.posZ));
        }
        return p_77659_1_;
    }
    /*
    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {

        if(!p_77648_3_.isRemote){
            p_77648_2_.openGui(FBS.instance, FBS.GUI_ENCHANTMENT_ID, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
        }

        return true;
    }
    */
}
