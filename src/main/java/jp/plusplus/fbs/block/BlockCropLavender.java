package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

/**
 * Createdby pluslus_Fon 2015/06/13.
 */
public class BlockCropLavender extends BlockCrops {
    private IIcon[] icons = new IIcon[6];

    public BlockCropLavender() {
        setBlockName("fbs.lavender");
        setBlockTextureName(FBS.MODID + ":lavender");
    }

    protected Item func_149866_i()
    {
        return ItemCore.seedLavender;
    }

    protected Item func_149865_P()
    {
        return ItemCore.lavender;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        for (int i = 0; i < icons.length; ++i) {
            icons[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        if (p_149691_2_ < 0 || p_149691_2_ > 7) {
            p_149691_2_ = 7;
        }

        int i = 5 * p_149691_2_ / 7;
        if (i > 5) i = 5;
        return icons[i];
    }
}
