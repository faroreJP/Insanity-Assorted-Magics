package jp.plusplus.fbs.storage;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.block.BlockBase;
import jp.plusplus.fbs.item.ItemMonocle;
import jp.plusplus.fbs.particle.EntityGlowFX;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class BlockMealCrystal extends BlockBase implements ITileEntityProvider,ChunkLoadManager.IChunkLoader {
    private static boolean breaking=false;

    public BlockMealCrystal() {
        super(Material.rock);
        setStepSound(soundTypeGlass);
        setBlockName("mealCrystal");
        setBlockTextureName("mealCrystal");
        setResistance(10000);
        setHardness(0.5f);
        setHarvestLevel("pickaxe", Item.ToolMaterial.EMERALD.getHarvestLevel());

        infoName="mealCrystal";
        infoRow=1;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return p_149915_2_==0?new TileEntityMeal():null;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }
    @Override
    public int quantityDropped(Random p_149745_1_) {
        return 0;
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        int m=world.getBlockMetadata(x,y,z);
        TileEntityMeal te=null;
        if(m==0) te=(TileEntityMeal)world.getTileEntity(x,y,z);
        else te=(TileEntityMeal)world.getTileEntity(x,y-1,z);
        if(te==null || !te.hasFragment()) return false;

        ItemStack ret=te.getFragment(true);
        if(!world.isRemote){
            if(ret!=null){
                player.entityDropItem(ret, player.getEyeHeight());
                if(!player.capabilities.isCreativeMode) FBSEntityPropertiesAPI.LoseSanity(player, 1, 6, true);
            }
        }
        te.markDirty();
        world.markBlockForUpdate(x,y,z);

        return true;
    }

    @Override
    public boolean canLoad(World w, int x, int y, int z) {
        return true;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        TileEntityMeal te = (TileEntityMeal)par1World.getTileEntity(x, y, z);
        if(te!=null){
            ItemStack[] list=te.getAllItemStacks();
            for(ItemStack itemstack : list){
                if (itemstack != null){
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0){
                        int k1 = itemstack.getMaxStackSize();
                        if (k1 > itemstack.stackSize){
                            k1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= k1;

                        EntityItem entityitem = new EntityItem(par1World, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        if (itemstack.hasTagCompound()){
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                        par1World.spawnEntityInWorld(entityitem);
                    }
                }
            }
            if(par6==0) ChunkLoadManager.removeChunkLoader(par1World, x,y,z);
        }

        if(!breaking){
            breaking=true;
            switch (par6){
                case 0:
                    if(par1World.getBlock(x,y+1,z)==this) par1World.func_147480_a(x,y+1,z, false);
                    break;
                case 1:
                    if(par1World.getBlock(x,y-1,z)==this) par1World.func_147480_a(x,y-1,z, false);
                    break;
            }
            breaking=false;
        }

        super.breakBlock(par1World, x, y, z, block, par6);
    }

    @Override
    public boolean canPlaceBlockAt(World w, int x, int y, int z) {
        for(int i=0;i<2;i++){
            if(!w.getBlock(x,y+i,z).isReplaceable(w,x,y+i,z)) return false;
        }
        return true;
    }

    @Override
    public void onPostBlockPlaced(World w, int x, int y, int z, int meta) {
    }

    @Override
    public int onBlockPlaced(World w, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int meta) {
        w.setBlock(x,y+1,z,this,1,2);
        return meta;
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
        return FBS.renderMealId;
    }

    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 15;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        EntityPlayer ep = FBS.proxy.getEntityPlayerInstance();
        if (ep == null) return;

        TileEntityMeal tem=null;
        if (world.getBlockMetadata(x, y, z) == 0) tem=(TileEntityMeal)world.getTileEntity(x,y,z);
        else if(world.getBlock(x,y-1,z)==this) tem=(TileEntityMeal)world.getTileEntity(x,y-1,z);
        if(tem==null || !tem.hasFragment()) return;

        float f = (float) x + 0.5F;
        float f1 = (float) y + 0.5f;
        float f2 = (float) z + 0.5F;
        for(int i=0;i<2;i++){
            float f3 = (0.2f+0.3f*rand.nextFloat())*(rand.nextBoolean()?-1:1);
            float f4 = rand.nextFloat() * 0.8F - 0.4F;
            float f5 = (0.2f+0.3f*rand.nextFloat())*(rand.nextBoolean()?-1:1);
            spawnParticle(world, (double) (f+f3), (double) (f1+f4), (double) (f2 + f5));
        }
        /*
        f4 = rand.nextFloat() * 0.6F - 0.3F;
        f5 =rand.nextFloat()*0.75f;
        spawnParticle(world, (double) (f + f4), (double) (f1+f5), (double) (f2 - f3));
        f4 = rand.nextFloat() * 0.6F - 0.3F;
        f5 =rand.nextFloat()*0.75f;
        spawnParticle(world, (double) (f + f4), (double) (f1+f5), (double) (f2 + f3));
        */
    }

    @SideOnly(Side.CLIENT)
    protected void spawnParticle(World world, double x, double y, double z){
        EntityGlowFX e=new EntityGlowFX(world, x,y,z);
        e.setRBGColorF(0.8f+0.2f*rand.nextFloat(), 0.8f+0.2f*rand.nextFloat(), 0);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(e);
    }
}
