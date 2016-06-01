package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class ItemBlockBase extends ItemBlock {
    public ItemBlockBase(Block p_i45328_1_) {
        super(p_i45328_1_);
        setCreativeTab(FBS.tab);
    }

    public ItemBlock setUnlocalizedName(String s){
        super.setUnlocalizedName("fbs." + s);
        return this;
    }
    public Item setTextureName(String s){
        super.setTextureName(FBS.MODID + ":" + s);
        return this;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean flag){
        if(field_150939_a instanceof BlockBase) ((BlockBase)(field_150939_a)).addBlockInformation(item, p_77624_2_, list, flag);
    }
}
