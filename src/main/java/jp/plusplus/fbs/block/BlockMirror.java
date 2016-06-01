package jp.plusplus.fbs.block;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.particle.EntityGlowFX;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import jp.plusplus.fbs.tileentity.TileEntityMirror;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.sextiarysector.block.BlockMonitor;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.UP;

/**
 * Createdby pluslus_Fon 2015/06/24.
 */
public class BlockMirror extends BlockBase implements ITileEntityProvider{

    public BlockMirror() {
        super(Material.rock);
        setBlockName("mirror");
        setBlockTextureName("mirror");
        setHardness(3.5F);
        setStepSound(soundTypeGlass);
        infoName="mirror";
        infoRow=1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMirror();
    }
    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        MCEconomyAPI.openShopGui(Registry.shopMCE2Id, par5EntityPlayer, par1World, par2, par3, par4);
        return true;
    }
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
            world.setBlock(x, y + 1, z, BlockCore.mirror);
            world.setBlockMetadataWithNotify(x, y + 1, z, 2 | 8, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
            world.setBlock(x,y+1,z, BlockCore.mirror);
            world.setBlockMetadataWithNotify(x, y+1, z, 5|8, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
            world.setBlock(x,y+1,z, BlockCore.mirror);
            world.setBlockMetadataWithNotify(x, y+1, z, 3|8, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
            world.setBlock(x,y+1,z, BlockCore.mirror);
            world.setBlockMetadataWithNotify(x, y+1, z, 4|8, 2);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x,y,z);

    }
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int meta) {
        world.removeTileEntity(x,y,z);
        if((meta&8)!=0 && world.getBlock(x,y-1,z)==BlockCore.mirror) world.func_147480_a(x,y-1,z, true);
        else if(world.getBlock(x,y+1,z)==BlockCore.mirror) world.func_147480_a(x,y+1,z, false);
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
        if((metadata&8)!=0) return new ArrayList<ItemStack>();
        return super.getDrops(world, x,y,z,metadata,fortune);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).isReplaceable(world, x, y, z) && world.getBlock(x, y+1, z).isReplaceable(world, x, y+1, z);
    }

    @Override
    public int getRenderType(){
        return FBS.renderMirrorId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }
}