package jp.plusplus.fbs.block;

import codechicken.lib.render.BlockRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.ItemAlchemyMaterial;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import jp.plusplus.fbs.tileentity.TileEntityHavestableGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class BlockGrass extends BlockHerb {
    protected IIcon[] icons;

    public BlockGrass() {
        setBlockName("grass");
        setBlockTextureName("grass");
        setHardness(0.5f);
        setResistance(0.1f);
        setCreativeTab(FBS.tabAlchemy);
        setStepSound(soundTypeGrass);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityHavestableGrass(60, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        icons=new IIcon[2];
        for(int i=0;i<2;i++){
            icons[i]=p_149651_1_.registerIcon(FBS.MODID+":harvestableGrass"+i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return icons[getTE(world,x,y,z).canHarvest()?1:0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int a, int b) {
        return icons[1];
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
    }

    @Override
    public ArrayList<ItemStack> getHarvestItems(World world, int x, int y, int z) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        for(int i=9;i<18;i++) ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, i));
        for(int i=30;i<36;i++) ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, 1, i));
        for(int i=4;i<7;i++) ret.add(AlchemyRegistry.getItemStack(ItemCore.alchemyMaterialEatable, 1, i));

        return ret;
    }

    @Override
    public ArrayList<ItemStack> harvest(World world, int x, int y, int z, Random rand) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ArrayList<ItemStack> list = getHarvestItems(world, x, y, z);
        int num = 2 + rand.nextInt(2);

        for (int i = 0; i < num; i++) {
            ret.add(list.get(rand.nextInt(list.size()-1)).copy());
        }

        getTE(world, x, y, z).onHarvest();
        return ret;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return "tile.fbs.grass";
    }
}
