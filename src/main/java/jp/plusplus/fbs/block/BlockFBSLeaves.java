package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/08/20.
 */
public class BlockFBSLeaves extends BlockLeaves {
    public static final String[][] field_150130_N = new String[][]{{"leaves_oak", "leaves_birch"}, {"leaves_oak_opaque", "leaves_birch_opaque"}};
    public static final String[] field_150131_O = new String[]{"oak", "birch"};

    public BlockFBSLeaves() {
        setBlockName("leaves");
        setBlockTextureName("leaves");
        setCreativeTab(FBS.tab);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        meta = (meta & 3);

        if (meta == 0) return 0xff0000;
        else return 0xffff00;
        //return (meta & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((meta & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : super.getRenderColor(meta));
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess w, int x, int y, int z) {
        int meta = (w.getBlockMetadata(x, y, z) & 3);

        if (meta == 0) return 0xff0000;
        else return 0xffff00;
        //return (meta & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((meta & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : super.colorMultiplier(w, x, y, z));
    }

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {
        if ((p_150124_5_ & 3) == 0 && p_150124_1_.rand.nextInt(p_150124_6_) == 0) {
            this.dropBlockAsItem(p_150124_1_, p_150124_2_, p_150124_3_, p_150124_4_, new ItemStack(Items.apple, 1, 0));
        }
    }

    public int damageDropped(int p_149692_1_) {
        return (p_149692_1_&3)==0?0:2;
    }
    /*
    protected int func_150123_b(int p_150123_1_) {
        int j = super.func_150123_b(p_150123_1_);

        if ((p_150123_1_ & 3) == 3) {
            j = 40;
        }

        return j;
    }
    */

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        meta = (meta & 3);
        if (meta >= field_150129_M.length) meta = 0;
        return field_150129_M[field_150127_b][meta];
        //return (meta & 3) == 1 ? this.field_150129_M[this.field_150127_b][1] : ((meta & 3) == 3 ? this.field_150129_M[this.field_150127_b][3] : ((meta & 3) == 2 ? this.field_150129_M[this.field_150127_b][2] : this.field_150129_M[this.field_150127_b][0]));
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        for (int i = 0; i < field_150131_O.length; i++) p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        for (int i = 0; i < field_150130_N.length; ++i) {
            this.field_150129_M[i] = new IIcon[field_150130_N[i].length];

            for (int j = 0; j < field_150130_N[i].length; ++j) {
                this.field_150129_M[i][j] = p_149651_1_.registerIcon(field_150130_N[i][j]);
            }
        }
    }

    public String[] func_150125_e() {
        return field_150131_O;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }
}
