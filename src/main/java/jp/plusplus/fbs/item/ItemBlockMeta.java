package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockBlock;
import jp.plusplus.fbs.block.IMeta;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by plusplus_F on 2015/08/25.
 */
public class ItemBlockMeta extends ItemBlockBase {
    public ItemBlockMeta(Block p_i45328_1_) {
        super(p_i45328_1_);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150939_a.getIcon(2, p_77617_1_);
    }

    @Override
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        if(field_150939_a instanceof IMeta) {
            return ((IMeta) field_150939_a).getUnlocalizedName(par1ItemStack.getItemDamage());
        }
        return super.getUnlocalizedName();
    }
}
