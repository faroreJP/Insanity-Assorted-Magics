package jp.plusplus.fbs.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.tileentity.TileEntityAlchemyCauldron;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/09/24.
 */
public class BlockAlchemyCauldron extends BlockBase implements ITileEntityProvider {
    public static final int X_SHIFT[]={0,1,0,1};
    public static final int Z_SHIFT[]={0,0,1,1};

    private static boolean breakFlag=false;

    public BlockAlchemyCauldron() {
        super(Material.rock);
        setBlockName("alchemyCauldron");
        setHarvestLevel("pickaxe", 2);
        setResistance(80.0f);
        setHardness(3.0f);
        setStepSound(soundTypeStone);
        setBlockTextureName("alchemyCauldron");
        setCreativeTab(FBS.tabAlchemy);
    }

    public ArrayList<BlockPos> getBlocks(World w, int x, int y, int z){
        ArrayList<BlockPos> pos=new ArrayList<BlockPos>();
        for(int i=0;i<4;i++) pos.add(new BlockPos(x+X_SHIFT[i], y, z+Z_SHIFT[i], i));
        return pos;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        if((p_149915_2_&7)==0) return new TileEntityAlchemyCauldron();
        return null;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        int meta=par1World.getBlockMetadata(par2, par3, par4)&7;

        if(meta==0){
            TileEntity e=par1World.getTileEntity(par2, par3, par4);
            if(!par1World.isRemote && e instanceof TileEntityAlchemyCauldron){
                par5EntityPlayer.openGui(FBS.instance, -1, par1World, par2, par3, par4);
            }
            return true;
        }
        else{
            return par1World.getBlock(par2-X_SHIFT[meta], par3, par4-Z_SHIFT[meta]).onBlockActivated(par1World, par2-X_SHIFT[meta], par3, par4-Z_SHIFT[meta], par5EntityPlayer, par6, par7, par8, par9);
        }
    }
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int pSide=0;

        /*
        //プレイヤーの向きの決定
        switch (l){
            case 0: pSide=2; break;
            case 1: pSide=5; break;
            case 2: pSide=3; break;
            case 3: pSide=4; break;
        }
        */
        pSide=(l==0||l==2)?0:8;

        //ほかのブロックの設置
        ArrayList<BlockPos> pos=getBlocks(world, x,y,z);
        for(BlockPos bp : pos){
            if(bp.getBlock(world)==this) world.setBlockMetadataWithNotify(bp.x, bp.y, bp.z, bp.meta|pSide, 2);
            else world.setBlock(bp.x, bp.y, bp.z, this, bp.meta|pSide, 2);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        /*
        int meta=world.getBlockMetadata(x,y,z);

        ArrayList<BlockPos> pos=getBlocks(world, x-X_SHIFT[meta], y, z-Z_SHIFT[meta]);
        for(BlockPos bp : pos){
            if(!bp.getBlock(world).isReplaceable(world, bp.x, bp.y, bp.z)) return false;
        }
        */

        return true;
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        TileEntity tileentity = par1World.getTileEntity(x, y, z);

        if(!breakFlag){
            breakFlag=true;

            par6=(par6&7);
            ArrayList<BlockPos> pos=getBlocks(par1World, x-X_SHIFT[par6], y, z-Z_SHIFT[par6]);
            for(BlockPos bp : pos){
                if(bp.getBlock(par1World)==this) par1World.func_147480_a(bp.x, bp.y, bp.z, false);
            }
            breakFlag=false;
            return;
        }

        if(tileentity==null){
            super.breakBlock(par1World, x, y, z, block, par6);
            return;
        }

        if(tileentity instanceof TileEntityAlchemyCauldron){
            TileEntityAlchemyCauldron inv=(TileEntityAlchemyCauldron)tileentity;
            for (ItemStack itemstack : inv.inputMaterial){

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
            //par1World.func_96440_m(x, y, z, block);
        }

        super.breakBlock(par1World, x, y, z, block, par6);
    }

    @Override
    public int getRenderType(){
        return FBS.renderAlchemyCauldronId;
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

    public static class BlockPos{
        public int x,y,z;
        public int meta;
        public BlockPos(int x, int y, int z, int meta){
            this.x=x;
            this.y=y;
            this.z=z;
            this.meta=meta;
        }
        public Block getBlock(World w){
            return w.getBlock(x,y,z);
        }
        public TileEntity getTE(World w){
            return w.getTileEntity(x, y, z);
        }
    }
}
