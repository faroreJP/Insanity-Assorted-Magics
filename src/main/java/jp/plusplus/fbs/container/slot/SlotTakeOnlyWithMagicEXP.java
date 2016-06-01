package jp.plusplus.fbs.container.slot;

import cpw.mods.fml.common.FMLCommonHandler;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/10/31.
 */
public class SlotTakeOnlyWithMagicEXP extends Slot {
    protected EntityPlayer thePlayer;
    protected int achievement;

    public SlotTakeOnlyWithMagicEXP(EntityPlayer p_i1813_1_, IInventory p_i1813_2_, int p_i1813_3_, int p_i1813_4_, int p_i1813_5_) {
        this(p_i1813_1_, p_i1813_2_, p_i1813_3_, p_i1813_4_, p_i1813_5_, 0);
    }
    public SlotTakeOnlyWithMagicEXP(EntityPlayer p_i1813_1_, IInventory p_i1813_2_, int p_i1813_3_, int p_i1813_4_, int p_i1813_5_, int achievement) {
        super(p_i1813_2_, p_i1813_3_, p_i1813_4_, p_i1813_5_);
        this.thePlayer = p_i1813_1_;
        this.achievement=achievement;
    }

    public boolean isItemValid(ItemStack p_75214_1_) {
        return false;
    }

    public ItemStack decrStackSize(int p_75209_1_) {
        return super.decrStackSize(p_75209_1_);
    }

    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
        this.onCrafting(p_82870_2_);
        if(achievement==1){
            p_82870_1_.triggerAchievement(AchievementRegistry.alchemy);
        }
        super.onPickupFromSlot(p_82870_1_, p_82870_2_);
    }

    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.onCrafting(p_75210_1_);
    }

    protected void onCrafting(ItemStack p_75208_1_) {
        p_75208_1_.onCrafting(this.thePlayer.worldObj, this.thePlayer, 1);

        if (!this.thePlayer.worldObj.isRemote) {
            double baseEXP = AlchemyRegistry.GetProductExp(p_75208_1_);

            if(baseEXP>0){
                FBSEntityPropertiesAPI.AddExp(thePlayer, baseEXP, true);
            }
        }
    }
}
