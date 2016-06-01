package jp.plusplus.fbs.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.item.ItemBase;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.List;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class ItemMealFragment extends ItemBase{
    public static final String DIM="MEAL_DIM";
    public static final String X="MEAL_X";
    public static final String Y="MEAL_Y";
    public static final String Z="MEAL_Z";

    public ItemMealFragment(){
        setUnlocalizedName("mealFragment");
        setTextureName("mealFragment");
        setCreativeTab(null);
    }

    public static ItemStack getItemStack(World w, int x, int y, int z){
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setInteger(DIM, w.provider.dimensionId);
        nbt.setInteger(X, x);
        nbt.setInteger(Y, y);
        nbt.setInteger(Z, z);

        ItemStack ret=new ItemStack(ItemCore.mealFragment);
        ret.setTagCompound(nbt);
        return ret;
    }

    public static TileEntityMeal getTileEntity(ItemStack fragment){
        if(fragment==null) {
            FBS.logger.info("fragment is null");
            return null;
        }
        NBTTagCompound nbt=fragment.getTagCompound();
        if(nbt==null){
            FBS.logger.info("nbt is null");
            return null;
        }

        int id=nbt.getInteger(DIM);
        int x=nbt.getInteger(X);
        int y=nbt.getInteger(Y);
        int z=nbt.getInteger(Z);

        World world=ChunkLoadManager.getWorld(id);
        if(world==null){
            FBS.logger.info("world is null");
            return null;
        }
        if(!world.getChunkFromBlockCoords(x,z).isChunkLoaded){
            FBS.logger.info("chunk is not loaded");
            return null;
        }

        TileEntity te=world.getTileEntity(x,y,z);
        return (te instanceof TileEntityMeal)?(TileEntityMeal)te:null;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        String name = "???";

        NBTTagCompound nbt = p_77624_1_.getTagCompound();
        if(nbt==null) return;

        World w=DimensionManager.getWorld(nbt.getInteger(DIM));
        if(w!=null && w.provider!=null) {
            name = w.provider.getDimensionName();
        }
        p_77624_3_.add(name + "(" + nbt.getInteger(X) + "," + nbt.getInteger(Y) + "," + nbt.getInteger(Z) + ")");

    }
}
