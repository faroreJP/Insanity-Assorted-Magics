package jp.plusplus.fbs.container.slot;

import cpw.mods.fml.common.FMLCommonHandler;
import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.api.IPottery;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class SlotTakeOnly extends Slot{
    protected int achievement;

    public SlotTakeOnly(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        this(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_, 0);
    }
    public SlotTakeOnly(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, int achievement){
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.achievement=achievement;
    }

    @Override
    public boolean isItemValid(ItemStack item){
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        Item item=stack.getItem();
        if(achievement==1){
            if(item instanceof ItemBlock){
                Block b=((ItemBlock) item).field_150939_a;
                if(b instanceof IPottery){
                    player.triggerAchievement(AchievementRegistry.grade);
                    if(((IPottery) b).getGrade(stack.getTagCompound())== IPottery.PotteryGrade.SOULFUL){
                        player.triggerAchievement(AchievementRegistry.soulful);
                    }
                }
            }
        }
        else if(achievement==2){
            FMLCommonHandler.instance().firePlayerCraftingEvent(player, stack, null);
        }

        super.onPickupFromSlot(player, stack);
    }
}
