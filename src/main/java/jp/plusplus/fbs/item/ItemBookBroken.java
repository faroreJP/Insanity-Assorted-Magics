package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by plusplus_F on 2015/08/24.
 */
public class ItemBookBroken extends ItemBase {
    private IIcon iconOverlay;

    public ItemBookBroken(){
        setCreativeTab(FBS.tabBook);
        setUnlocalizedName("bookBroken");
        setTextureName("bookNoDecoded");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1){
        return super.getIconFromDamage(par1);
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }
    @Override
    public int getSpriteNumber(){
        return 1;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        iconOverlay=par1IconRegister.registerIcon(FBS.MODID+":bookBroken");
    }
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses(){
        return true;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2){
        return par2 > 0 ? iconOverlay : itemIcon;
    }
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        if(par2==0) return 0xffffff;
        int d=par1ItemStack.getItemDamage();
        int r=(d>>8)&0xf, g=(d>>4)&0xf, b=d&0xf;
        return ~(((r*17)<<16)+((g*17)<<8)+(b*17));
    }
}
