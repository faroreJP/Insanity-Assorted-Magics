package jp.plusplus.fbs.pottery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockBase;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by pluslus_F on 2015/08/29.
 */
public class BlockKiln extends BlockBase implements ITileEntityProvider{
    private IIcon iconFTop;
    private IIcon iconFBottom;
    private IIcon iconFSide;
    private boolean isActive;
    private static boolean dontDrop;

    public BlockKiln(boolean flag) {
        super(Material.rock);
        isActive=flag;
        setBlockName("kiln");
        setBlockTextureName("kiln" + (isActive ? "Active" : ""));
        setHardness(3.5F);
        setStepSound(soundTypePiston);
        setCreativeTab(FBS.tabPottery);
        infoName="kiln";
        infoRow=1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityKiln();
    }
    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4){
        return false;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        TileEntity e=par1World.getTileEntity(par2, par3, par4);
        if(!par1World.isRemote && e instanceof TileEntityKiln){
            par5EntityPlayer.openGui(FBS.instance, -1, par1World, par2, par3, par4);
        }
        return true;
    }
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        if (!dontDrop) {
            TileEntity tileentity = world.getTileEntity(x, y, z);

            if(tileentity==null){
                super.breakBlock(world, x, y, z, block, par6);
                return;
            }

            if(tileentity instanceof IInventory){
                IInventory inv=(IInventory)tileentity;

                for (int j1 = 0; j1 < inv.getSizeInventory(); j1++){
                    ItemStack itemstack = inv.getStackInSlot(j1);

                    if (itemstack != null){
                        float f = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0){
                            int k1 = this.rand.nextInt(21) + 10;

                            if (k1 > itemstack.stackSize){
                                k1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= k1;
                            EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound()){
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                            entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                            world.spawnEntityInWorld(entityitem);
                        }
                    }
                }
                //par1World.func_96440_m(x, y, z, block);
            }
        }

        if (hasTileEntity(par6)){
            world.removeTileEntity(x,y,z);
        }
    }

    public static void updateFurnaceBlockState(boolean s, World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        dontDrop = true;

        if (s) {
            world.setBlock(x, y, z, BlockCore.kilnActive);
        } else {
            world.setBlock(x, y, z, BlockCore.kiln);
        }

        dontDrop = false;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null) {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random p_149734_5_) {
        if (isActive) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + p_149734_5_.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = p_149734_5_.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        iconFTop = p_149651_1_.registerIcon("furnace_top");
        iconFBottom = p_149651_1_.registerIcon("furnace_top");
        iconFSide = p_149651_1_.registerIcon("furnace_side");
    }
    @Override
    public IIcon getIcon(int side, int meta){
        if(side==0) return iconFBottom;
        if(side==1) return iconFTop;
        if(side==meta) return blockIcon;
        return iconFSide;
    }

    @Override
    public int getRenderType(){
        return FBS.renderDirectionalId;
    }
    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_){
        return Item.getItemFromBlock(BlockCore.kiln);
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return Item.getItemFromBlock(BlockCore.kiln);
    }
}