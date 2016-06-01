package jp.plusplus.fbs.storage;

import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class BlockMealOutletSingle extends BlockMealInlet {
    public BlockMealOutletSingle() {
        super();
        setBlockName("mealOutletSingle");
        infoName="mealOutlet";
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityMealOutletSingle();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntityMealOutletSingle temos=(TileEntityMealOutletSingle)world.getTileEntity(x,y,z);
        if(temos==null) return false;

        //リクエストを取り出す
        if(temos.hasRequirement()){
            if(!world.isRemote){
                player.entityDropItem(temos.getRequirement().copy(), player.getEyeHeight());
                temos.setRequirement(null);
            }
            temos.markDirty();
            world.markBlockForUpdate(x,y,z);
            return true;
        }

        //リクエストを設定する
        ItemStack hav=player.getCurrentEquippedItem();
        if(hav!=null && temos.hasFragment()){
            if(!world.isRemote){
                ItemStack f=hav.copy();
                f.stackSize=1;
                temos.setRequirement(f);

                hav.stackSize--;
                if(hav.stackSize<=0){
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }

                player.addChatComponentMessage(new ChatComponentText("Set requirement : "+f.getDisplayName()));
            }
            player.inventory.markDirty();
            world.markBlockForUpdate(x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, Block block, int par6){
        TileEntityMealOutletSingle te = (TileEntityMealOutletSingle)par1World.getTileEntity(x, y, z);
        if(te!=null && te.hasRequirement()){
            float f = this.rand.nextFloat() * 0.8F + 0.1F;
            float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
            float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
            ItemStack fragment=te.getRequirement();
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

}
