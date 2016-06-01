package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;

import java.util.List;

/**
 * Created by plusplus_F on 2015/10/20.
 */
public class ItemMPCoin extends ItemBase {
    protected IIcon[] icons;

    public ItemMPCoin() {
        setMaxStackSize(1);
        setMaxDamage(0);
        setHasSubtypes(true);
        setUnlocalizedName("mpCoin");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        int v = p_77659_1_.getItemDamage();
        if (!p_77659_2_.isRemote && v > 0) {
            MCEconomyAPI.addPlayerMP(p_77659_3_, v, false);
        }
        p_77659_1_.stackSize=0;
        return p_77659_1_;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        icons=new IIcon[2];
        icons[0]=p_94581_1_.registerIcon(FBS.MODID+":coin0");
        icons[1]=p_94581_1_.registerIcon(FBS.MODID+":coin1");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        return p_77617_1_>=1000?icons[1]:icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 1));
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 500));
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 1000));
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 2000));
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return super.getItemStackDisplayName(p_77653_1_)+"("+p_77653_1_.getItemDamage()+"MP)";
    }
}
