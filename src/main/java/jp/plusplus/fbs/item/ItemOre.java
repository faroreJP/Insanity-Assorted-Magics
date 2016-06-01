package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by pluslus_F on 2015/06/23.
 */
public class ItemOre extends ItemBlock {
    public ItemOre(Block p_i45328_1_) {
        super(p_i45328_1_);
        setCreativeTab(FBS.tab);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        if(field_150939_a instanceof BlockBlock) {
            return super.getUnlocalizedName() + BlockBlock.NAMES[par1ItemStack.getItemDamage()];
        }
        return super.getUnlocalizedName();
    }
}
