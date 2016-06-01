package jp.plusplus.fbs.pottery.usable.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class SlotPotteryUsable extends Slot {
    protected InventoryPotteryUsable inventoryPottery;
    public SlotPotteryUsable(InventoryPotteryUsable iInventory, int index, int x, int y) {
        super(iInventory, index, x, y);
        inventoryPottery=iInventory;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return inventoryPottery.potteryEffect.isItemValid(inventoryPottery.player, inventoryPottery.potteryStack, getSlotIndex(), itemStack);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return inventoryPottery.potteryEffect.canTakeStack(player, inventoryPottery.potteryStack, getSlotIndex(), getStack());
    }
}
