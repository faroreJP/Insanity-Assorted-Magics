package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class ItemCharm extends ItemBase {
    public static final int[] COLOR_VALUE = {0x333333, 0xff0000, 0x009113, 0x552700, 0x2b00ff, 0xff00f7, 0x00afaf, 0xcdcdcd,
            0x787878, 0xffaaaa, 0x48ff37, 0xffea00, 0x00ffff, 0xd7008b, 0xff9500, 0xffffff};

    public ItemCharm() {
        setHasSubtypes(true);
        setUnlocalizedName("charm");
        setTextureName("charm");
        getContainerItem(null);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        for (int i = 0; i < 16; i++) itemList.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getItemStackDisplayName(ItemStack item) {
        return StatCollector.translateToLocal("item.fbs.charm." + ItemDye.field_150923_a[item.getItemDamage()] + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return COLOR_VALUE[par1ItemStack.getItemDamage()];
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) != Blocks.snow_layer) {
            if (p_77648_7_ == 0) {
                --p_77648_5_;
            }

            if (p_77648_7_ == 1) {
                ++p_77648_5_;
            }

            if (p_77648_7_ == 2) {
                --p_77648_6_;
            }

            if (p_77648_7_ == 3) {
                ++p_77648_6_;
            }

            if (p_77648_7_ == 4) {
                --p_77648_4_;
            }

            if (p_77648_7_ == 5) {
                ++p_77648_4_;
            }

            if (!p_77648_3_.isAirBlock(p_77648_4_, p_77648_5_, p_77648_6_)) {
                return false;
            }
        }

        if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_)) {
            return false;
        } else {
            if (BlockCore.charm.canPlaceBlockAt(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) {
                --p_77648_1_.stackSize;
                p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, BlockCore.charm);
                p_77648_3_.setBlockMetadataWithNotify(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_1_.getItemDamage()^15, 2);
            }

            return true;
        }
    }
}
