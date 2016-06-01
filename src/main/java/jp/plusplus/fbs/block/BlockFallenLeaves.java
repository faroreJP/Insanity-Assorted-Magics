package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/24.
 * 落ち葉
 */
public class BlockFallenLeaves extends BlockBase {
    IIcon icon;

    public BlockFallenLeaves() {
        super(Material.ground);
        setBlockTextureName("fallenLeaves");
        setBlockName("fallenLeaves");
        setHardness(0.2F);
        setLightOpacity(1);
        setStepSound(Block.soundTypeGrass);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon(getTextureName()+"Top");
        icon=register.registerIcon(getTextureName()+"Side");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if(side==0) return Blocks.dirt.getIcon(0,0);
        if(side==1) return blockIcon;
        return icon;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(Blocks.dirt);
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        return plantable.getPlantType(world, x,y+1,z)== EnumPlantType.Plains;
    }
}
