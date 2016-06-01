package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemCharm;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by pluslus_F on 2015/06/17.
 */
public class BlockCharm extends BlockBase {
    public BlockCharm() {
        super(Material.circuits);
        setBlockName("charm");
        setBlockTextureName("charm");
        setCreativeTab(null);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        //BlockRedstoneWire
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return FBS.renderCharmId;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
        int meta = p_149720_1_.getBlockMetadata(p_149720_2_, p_149720_3_, p_149720_4_);
        return ItemCharm.COLOR_VALUE[meta ^ 15];
    }

    @Override
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_) {
        return p_149742_1_.isBlockNormalCubeDefault(p_149742_2_, p_149742_3_ - 1, p_149742_4_, true);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return ItemCore.charm;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return ItemCore.charm;
    }

    @Override
    public int damageDropped(int p_149692_1_) {
        return p_149692_1_ ^ 15;
    }

    @Override
    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
        if (!p_149695_1_.isRemote) {
            boolean flag = this.canPlaceBlockAt(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);

            if (!flag){
                this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
                p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }

            super.onNeighborBlockChange(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
        }
    }

}
