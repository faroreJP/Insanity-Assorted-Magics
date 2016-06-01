package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by pluslus_F on 2015/06/17.
 */
public class ItemMagicCore extends ItemBlock {
    public ItemMagicCore(Block p_i45328_1_) {
        super(p_i45328_1_);
        setUnlocalizedName("fbs.magicCore");
        setTextureName(FBS.MODID+":magicCore");
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.magicCore.0"));
    }
}
