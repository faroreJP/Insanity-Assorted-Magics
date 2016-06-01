package jp.plusplus.fbs.storage;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockBase;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.particle.EntityGlowFX;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class BlockMealInlet extends BlockBase implements ITileEntityProvider {
    public BlockMealInlet() {
        super(Material.rock);
        setBlockName("mealInlet");
        setBlockTextureName("mealCrystal");
        setStepSound(soundTypeGlass);

        infoName="mealInlet";
        infoRow=1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMealInlet();
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
        //world.setBlockMetadataWithNotify(x,y,z,side,2);
        return side^1;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        IMealDevice te=(IMealDevice)world.getTileEntity(x,y,z);
        if(te==null) return false;

        ItemStack hav=player.getCurrentEquippedItem();
        if(te.hasFragment()){
            //ミールを取り出す
            if(!world.isRemote){
                player.entityDropItem(te.getFragment().copy(), 0);
            }
            te.setFragment(null);

            ((TileEntity)te).markDirty();
            player.inventory.markDirty();
            world.markBlockForUpdate(x, y, z);
            return true;
        }
        else if(hav!=null && hav.getItem()==ItemCore.mealFragment){
            //ミールを設定する
            ItemStack f=hav.copy();
            f.stackSize=1;
            te.setFragment(f);

            if(!world.isRemote){
                hav.stackSize--;
                if(hav.stackSize<=0){
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }
            }
            ((TileEntity)te).markDirty();
            player.inventory.markDirty();
            world.markBlockForUpdate(x, y, z);
            return true;
        }

        return false;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        //ミールの欠片をドロップさせる
        IMealDevice te = (IMealDevice)par1World.getTileEntity(x, y, z);
        if(te!=null && te.hasFragment()){
            float f = this.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
            ItemStack fragment=te.getFragment();
            EntityItem entityitem = new EntityItem(par1World, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(fragment.getItem(), fragment.stackSize, fragment.getItemDamage()));
            if (fragment.hasTagCompound()){
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)fragment.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
            entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
            entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
            par1World.spawnEntityInWorld(entityitem);
        }
        super.breakBlock(par1World, x, y, z, block, par6);
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
        return FBS.renderMealInletId;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess w, int x, int y, int z) {
        int meta=w.getBlockMetadata(x,y,z);
        switch (meta){
            case 0: setBlockBounds(0,0,0,1,0.5f,1); break;
            case 1: setBlockBounds(0,0.5f,0,1,1,1); break;
            case 2: setBlockBounds(0,0,0,1,1,0.5f); break;
            case 3: setBlockBounds(0,0,0.5f,1,1,1); break;
            case 4: setBlockBounds(0,0,0,0.5f,1,1); break;
            case 5: setBlockBounds(0.5f,0,0,1,1,1); break;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {
        setBlockBoundsBasedOnState(w,x,y,z);
        return super.getCollisionBoundingBoxFromPool(w,x,y,z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {
        setBlockBoundsBasedOnState(w,x,y,z);
        return super.getSelectedBoundingBoxFromPool(w, x, y, z);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntity te=world.getTileEntity(x,y,z);
        if(te instanceof IMealDevice){
            return ((IMealDevice) te).hasFragment()?15:0;
        }
        return 15;
    }



    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        EntityPlayer ep = FBS.proxy.getEntityPlayerInstance();
        if (ep == null) return;

        IMealDevice tem=(IMealDevice)world.getTileEntity(x,y,z);
        if(tem==null || !tem.hasFragment()) return;

        float f = (float) x + 0.5F;
        float f1 = (float) y + 0.5f;
        float f2 = (float) z + 0.5F;
        for(int i=0;i<2;i++){
            float f3 = 0.3f*rand.nextFloat()-0.15f;
            float f4 = rand.nextFloat() * 0.4F - 0.2F;
            float f5 = 0.3f*rand.nextFloat()-0.15f;
            spawnParticle(world, (double) (f+f3), (double) (f1+f4), (double) (f2 + f5));
        }
    }

    @SideOnly(Side.CLIENT)
    protected void spawnParticle(World world, double x, double y, double z){
        EntityGlowFX e=new EntityGlowFX(world, x,y,z);
        e.setRBGColorF(0.8f+0.2f*rand.nextFloat(), 0.8f+0.2f*rand.nextFloat(), 0);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(e);
    }
}
