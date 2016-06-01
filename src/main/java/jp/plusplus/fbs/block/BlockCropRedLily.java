package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

import static net.minecraftforge.common.util.ForgeDirection.UP;

/**
 * Createdby pluslus_Fon 2015/088/13.
 */
public class BlockCropRedLily extends BlockCrops {
    private IIcon[] icons = new IIcon[4];

    public BlockCropRedLily() {
        setBlockName("fbs.redLily");
        setBlockTextureName(FBS.MODID + ":redLily");
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        for (int i = 0; i < icons.length; ++i) {
            icons[i] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 7) {
            meta = 7;
        }

        if(meta==7) return icons[3];

        int i = 3 * meta / 7;
        if (i > 2) i = 2;
        return icons[i];
    }

    protected boolean canPlaceBlockOn(Block p_149854_1_) {
        return p_149854_1_ == Blocks.grass || p_149854_1_ == Blocks.dirt || p_149854_1_ == Blocks.farmland || p_149854_1_ == BlockCore.fallenLeaves;
    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        //ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        ArrayList<ItemStack> ret=new ArrayList<ItemStack>();
        Item drop=null;

        if(metadata<7){
            //ヒガンバナの種のドロップ
            drop=ItemCore.seedRedLily;
        }
        else{
            //ヒガンバナのドロップ
            BiomeGenBase bgb= world.getBiomeGenForCoords(x,z);
            if(bgb== Registry.biomeAutumn){
                drop=ItemCore.redLilyDirty;
            }
            else{
                drop=ItemCore.redLily;
            }
        }

        ret.add(new ItemStack(drop));
        return ret;
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return ItemCore.seedRedLily;
    }
    protected Item func_149866_i()
    {
        return ItemCore.seedRedLily;
    }

    protected Item func_149865_P()
    {
        return ItemCore.redLily;
    }
}
