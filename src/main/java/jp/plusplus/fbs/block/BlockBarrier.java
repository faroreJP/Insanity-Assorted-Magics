package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/09/27.
 */
public class BlockBarrier extends BlockBase {
    public BlockBarrier() {
        super(Material.glass);
        setBlockName("barrier");
        setBlockTextureName("barrier");
        setHardness(5.0f);
        setResistance(1000.0f);
        setTickRandomly(true);
        setStepSound(soundTypeGlass);
        setCreativeTab(FBS.tabBook);
    }

    @Override
    public int tickRate(World w) {
        return 5;
    }

    @Override
    public void updateTick(World w, int x, int y, int z, Random rand) {
        if (!w.isRemote) {
            int meta = w.getBlockMetadata(x, y, z)-2;

            if(meta>0) w.setBlockMetadataWithNotify(x, y, z, meta, 2);
            else w.func_147480_a(x, y, z, false);
        }
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        int meta = p_149726_1_.getBlockMetadata(p_149726_2_, p_149726_3_, p_149726_4_);
        if (meta == 0) {
            meta = AlchemyRegistry.getRandom().nextInt(4);
            p_149726_1_.setBlockMetadataWithNotify(p_149726_2_, p_149726_3_, p_149726_4_, meta, 2);
        }
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    protected boolean canSilkHarvest() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        /*
        ForgeDirection dir=ForgeDirection.getOrientation(p_149646_5_);
        Block b=p_149646_1_.getBlock(p_149646_2_+dir.offsetX, p_149646_3_+dir.offsetY, p_149646_4_+dir.offsetZ);

        if(b==null || b.getMaterial()==Material.air) return true;
        return b!=this;
        */
        return p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_)!=this;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

}
