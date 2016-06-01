package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.ItemAlchemyMaterial;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import jp.plusplus.fbs.tileentity.TileEntityHavestableMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class BlockMushroom extends BlockHerb {
    public BlockMushroom() {
        setBlockName("mushroom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        for (int i = 0; i < 3; i++) p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityHavestableMushroom(60, p_149915_2_);
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return "tile.fbs.mushroom"+meta;
    }

    @Override
    public ArrayList<ItemStack> getHarvestItems(World world, int x, int y, int z) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        int meta = world.getBlockMetadata(x, y, z);

        switch (meta){
            case 0:
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 25));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 26));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 27));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 29));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 41));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, 0));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, 2));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, 3));
                break;

            case 1:
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 25));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 26));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 27));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 28));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 29));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 41));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, 1));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, 2));
                break;

            case 2:
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 42));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 43));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 44));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, 45));
                ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, 1));
                break;

        }

        return ret;
    }

    @Override
    public ArrayList<ItemStack> harvest(World world, int x, int y, int z, Random rand) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ArrayList<ItemStack> list = getHarvestItems(world, x, y, z);
        int num = 1 + rand.nextInt(2);
        int index=rand.nextInt(list.size()-1);

        for (int i = 0; i < num; i++) {
            ret.add(list.get(index).copy());
        }

        getTE(world, x, y, z).onHarvest();
        return ret;
    }
}
