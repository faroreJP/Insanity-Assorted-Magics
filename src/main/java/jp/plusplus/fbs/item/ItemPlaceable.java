package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.entity.EntityTableware;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plusplus_F on 2015/11/06.
 */
public class ItemPlaceable extends ItemBase {
    public static final String[] NAMES={"Spoon", "Knife", "Fork"};
    protected IIcon[] icons;

    public ItemPlaceable(){
        setUnlocalizedName("tableware");
        setTextureName("tableware");
        setMaxDamage(0);
        setHasSubtypes(true);
    }


    @Override
    public int getMetadata(int par1) {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int meta=p_77667_1_.getItemDamage();
        if(meta<0 ||meta>=NAMES.length) meta=0;

        return super.getUnlocalizedName() + NAMES[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for(int i=0;i<NAMES.length;i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        icons=new IIcon[NAMES.length];
        for(int i=0;i<NAMES.length;i++){
            icons[i]=register.registerIcon(FBS.MODID+":tableware"+NAMES[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        if(p_77617_1_<0 || p_77617_1_>=icons.length) p_77617_1_=0;
        return icons[p_77617_1_];
    }


    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        //カラスさんありがとー
        if(player != null && player.isSneaking()) {
            this.onItemRightClick(item, world, player);
            return false;
        } else {
            Block block = world.getBlock(x, y, z);
            if(block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1) {
                side = 1;
            } else if(block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z)) {
                if(side == 0) {
                    --y;
                }

                if(side == 1) {
                    ++y;
                }

                if(side == 2) {
                    --z;
                }

                if(side == 3) {
                    ++z;
                }

                if(side == 4) {
                    --x;
                }

                if(side == 5) {
                    ++x;
                }
            }

            if(item.stackSize == 0) {
                return false;
            } else if(!player.canPlayerEdit(x, y, z, side, item)) {
                return false;
            } else if(y >= 255) {
                return false;
            } else{
                int m = this.getMetadata(item.getItemDamage());
                if(!world.isRemote && this.spawnEntity(world, player, new ItemStack(this, 1, m), (double) ((float) x + 0.5F), (double) ((float) y + 0.0F), (double) ((float) z + 0.5F))) {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                    --item.stackSize;
                }

                return true;
            }
        }
    }

    protected boolean spawnEntity(World world, EntityPlayer player, ItemStack item, double x, double y, double z) {
        EntityTableware entity=new EntityTableware(world, item, x,y,z);
        world.spawnEntityInWorld(entity);
        return true;
    }

}
