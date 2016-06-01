package jp.plusplus.fbs.pottery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class BlockPottersWheel extends BlockBase implements ITileEntityProvider {
    private IIcon iconFTop;
    private IIcon iconFBottom;

    public BlockPottersWheel() {
        super(Material.rock);
        setBlockName("pottersWheel");
        setBlockTextureName("pottersWheel");
        setHardness(3.5F);
        setStepSound(soundTypeWood);
        setCreativeTab(FBS.tabPottery);
        infoName = "pottersWheel";
        infoRow = 1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityPottersWheel();
    }

    @Override
    public boolean canPlaceTorchOnTop(World par1World, int par2, int par3, int par4) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        TileEntity e = par1World.getTileEntity(par2, par3, par4);
        if (!par1World.isRemote) {
            par5EntityPlayer.openGui(FBS.instance, -1, par1World, par2, par3, par4);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        blockIcon = p_149651_1_.registerIcon(FBS.MODID + ":pottersWheelSide");
        iconFTop = p_149651_1_.registerIcon("stonebrick_carved");
        iconFBottom = p_149651_1_.registerIcon("furnace_top");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 0) return iconFBottom;
        if (side == 1) return iconFTop;
        return blockIcon;
    }

    @Override
    public int getRenderType(){
        return FBS.renderPottersWheelId;
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

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        setBlockBounds(0,0,0,1,12.0f/16.0f,1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x,y,z, x+1, y+12.0/16.0, z+1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        TileEntity tileentity = world.getTileEntity(x, y, z);

        if (tileentity == null) {
            super.breakBlock(world, x, y, z, block, par6);
            return;
        }

        if (tileentity instanceof IInventory) {
            IInventory inv = (IInventory) tileentity;

            for (int j1 = 0; j1 < inv.getSizeInventory(); j1++) {
                if(j1==25) continue;
                ItemStack itemstack = inv.getStackInSlot(j1);

                if (itemstack != null) {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int k1 = this.rand.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize) {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
            //par1World.func_96440_m(x, y, z, block);
        }

        if (hasTileEntity(par6)){
            world.removeTileEntity(x, y, z);
        }
    }
}