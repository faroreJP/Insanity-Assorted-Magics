package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/03/09.
 */
public class ItemBasket extends ItemBase {
    public Random rand = new Random();

    public ItemBasket() {
        setUnlocalizedName("basket");
        setTextureName("basket");
        setMaxStackSize(1);
        setNoRepair();
        setMaxDamage(0);
        setCreativeTab(FBS.tabAlchemy);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        list.add(StatCollector.translateToLocal("info.fbs.basket.0"));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.openGui(FBS.instance, FBS.GUI_BASKET_ID, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY),  MathHelper.floor_double(player.posZ));
        return itemStack;
    }
}
