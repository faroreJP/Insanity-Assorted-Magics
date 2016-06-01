package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by pluslus_F on 2015/06/23.
 */
public class BlockBlock extends BlockBase {
    public static String[] NAMES={"Ruby","Sapphire","Amethyst"};
    public IIcon[] icons;

    public BlockBlock(String name) {
        super(Material.rock);
        setHarvestLevel("pickaxe", 2);
        setBlockName(name);
        setBlockTextureName(name);
    }

    @Override
    public int damageDropped(int par1){
        return par1;
    }
    @Override
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < NAMES.length; i++) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons=new IIcon[NAMES.length];
        for(int i=0;i<NAMES.length;i++){
            icons[i]=iconRegister.registerIcon(getTextureName()+NAMES[i]);
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[meta];
    }

}
