package jp.plusplus.fbs.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class ItemPotionOblivion extends ItemPotion {
    public ItemPotionOblivion() {
        setCreativeTab(FBS.tab);
        setUnlocalizedName("fbs.potOblivion");
        setTextureName("potion");
        setMaxStackSize(64);

    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
        return p_82790_2_==0?0xaaaaff:0xffffff;
    }

    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
        if (!p_77654_3_.capabilities.isCreativeMode) {
            --p_77654_1_.stackSize;
        }

        if (!p_77654_2_.isRemote) {
            FBSEntityProperties prop = FBSEntityProperties.get(p_77654_3_);
            if (prop != null) {
                prop.setMagicLevel(prop.getMagicLevel() - 5);

                String str=String.format(StatCollector.translateToLocal("info.fbs.lv.0"), prop.getMagicLevel());
                p_77654_3_.addChatComponentMessage(new ChatComponentText(str));

                SanityManager.sendPacket(p_77654_3_);
            }
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
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        itemList.add(new ItemStack(this));
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return StatCollector.translateToLocal(getUnlocalizedName()+".name");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_){
        return super.getIconFromDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.potObl.0"));
    }
}
