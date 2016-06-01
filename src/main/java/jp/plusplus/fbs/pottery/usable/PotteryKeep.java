package jp.plusplus.fbs.pottery.usable;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotteryKeep extends PotteryBase {
    public static final String CHANGED_INDEXES="ChangedIndex";

    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.keep";
    }

    @Override
    public ItemStack onInventoryOpening(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        pottery.getTagCompound().setBoolean(CHANGED_INDEXES+index, itemStack!=null);
        return itemStack;
    }

    @Override
    public ItemStack onUse(EntityPlayer player, ItemStack pottery) {
        player.openGui(FBS.instance, FBS.GUI_MAGIC_POT_ID, player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
        return pottery;
    }
}
