package jp.plusplus.fbs.block;

import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/13.
 */
public class BlockBase extends Block {
    protected String infoName="";
    protected int infoRow=0;
    protected Random rand=new Random();

    public BlockBase(Material mat) {
        super(mat);
        setCreativeTab(FBS.tab);
    }

    public Block setBlockName(String s){
        super.setBlockName("fbs."+s);
        return this;
    }
    public Block setBlockTextureName(String s){
        super.setBlockTextureName(FBS.MODID+":"+s);
        return this;
    }

    public void addBlockInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean flag){
        if(infoName.length()>0 && FBS.enableDescription){
            for(int i=0;i<infoRow;i++){
                list.add(I18n.format("info.fbs."+infoName+"."+i));
            }
        }
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        TileEntity tileentity = par1World.getTileEntity(x, y, z);

        if(tileentity==null){
            super.breakBlock(par1World, x, y, z, block, par6);
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

}
