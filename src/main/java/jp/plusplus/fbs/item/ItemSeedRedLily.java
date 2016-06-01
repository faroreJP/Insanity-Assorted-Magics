package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/08/24.
 */
public class ItemSeedRedLily extends ItemSeeds {
    protected Block fucking_private_field_150925_a;

    public ItemSeedRedLily() {
        super(BlockCore.cropRedLily, Blocks.dirt);
        setUnlocalizedName("fbs.seedRedLily");
        setTextureName(FBS.MODID+":seedRedLily");
        setCreativeTab(FBS.tab);
        fucking_private_field_150925_a=BlockCore.cropRedLily;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if (p_77648_7_ != 1) {
            return false;
        } else if (p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_) && p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_ + 1, p_77648_6_, p_77648_7_, p_77648_1_)) {
            Block b=p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

            if(b==Blocks.dirt || b==Blocks.grass || b==Blocks.farmland || b==BlockCore.fallenLeaves){
                if (p_77648_3_.isAirBlock(p_77648_4_, p_77648_5_ + 1, p_77648_6_)) {
                    p_77648_3_.setBlock(p_77648_4_, p_77648_5_ + 1, p_77648_6_, fucking_private_field_150925_a);
                    --p_77648_1_.stackSize;
                    return true;
                } else {
                    return false;
                }
            }
            else return false;


        } else {
            return false;
        }
    }
}
