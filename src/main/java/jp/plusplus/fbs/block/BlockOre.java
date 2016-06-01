package jp.plusplus.fbs.block;

import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by pluslus_F on 2015/06/23.
 */
public class BlockOre extends BlockBlock {
    public BlockOre() {
        super("ore");
    }

    @Override
    public Item getItemDropped(int meta, Random p_149650_2_, int p_149650_3_){
        return ItemCore.gem;
    }

    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 1;
    }
    @Override
    public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
        int j = p_149679_2_.nextInt(p_149679_1_ + 2) - 1;
        if (j < 0) {
            j = 0;
        }

        return this.quantityDropped(p_149679_2_) * (j + 1);
    }

    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
        super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_, p_149690_7_);
    }

    @Override
    public int getExpDrop(IBlockAccess p_149690_1_, int p_149690_5_, int p_149690_7_) {
        return MathHelper.getRandomIntegerInRange(this.rand, 3, 4);
    }
}
