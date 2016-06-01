package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class ItemGem extends ItemBase {
    public static final String[] NAMES={"gemRuby","gemSapphire","gemAmethyst", "gemInfinity"};
    public static final int[] COLOR_VALUE={0xff3333, 0x3333ff, 0xcc00cc, 0xc8e6ff};
    private IIcon overlay;

    public ItemGem(){
        setHasSubtypes(true);
        setTextureName("gem");
        setUnlocalizedName("gem");
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        for(int i=0;i<NAMES.length;i++) itemList.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack item){
        return "item.fbs."+NAMES[item.getItemDamage()];
    }


    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_){
        super.registerIcons(p_94581_1_);
        overlay=p_94581_1_.registerIcon(FBS.MODID+":gemOverlay");
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return par1ItemStack.getItemDamage()==3;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int pass){
        return pass == 0 ? itemIcon : overlay;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return par2==0?COLOR_VALUE[par1ItemStack.getItemDamage()]:0xffffff;
    }
}
