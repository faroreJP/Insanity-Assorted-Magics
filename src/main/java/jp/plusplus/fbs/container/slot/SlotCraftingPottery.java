package jp.plusplus.fbs.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

/**
 * Createdby pluslus_Fon 2015/08/29.
 */
public class SlotCraftingPottery extends Slot {
    private EntityPlayer thePlayer;
    private int amountCrafted;

    public SlotCraftingPottery(EntityPlayer player, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
        super(p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.thePlayer = player;
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_)
    {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int p_75209_1_) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(p_75209_1_, this.getStack().stackSize);
        }
        return super.decrStackSize(p_75209_1_);
    }

    @Override
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.amountCrafted += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    @Override
    protected void onCrafting(ItemStack p_75208_1_) {
        p_75208_1_.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        this.amountCrafted = 0;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
        //FMLCommonHandler.instance().firePlayerCraftingEvent(p_82870_1_, p_82870_2_, craftMatrix);
        this.onCrafting(p_82870_2_);

        for (int i = 0; i < 25; ++i) {
            ItemStack itemstack1 = inventory.getStackInSlot(i);

            if (itemstack1 != null) {
                inventory.decrStackSize(i, 1);

                if (itemstack1.getItem().hasContainerItem(itemstack1)) {
                    ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

                    if (itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
                        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
                        continue;
                    }

                    if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !this.thePlayer.inventory.addItemStackToInventory(itemstack2)) {
                        if (inventory.getStackInSlot(i) == null) {
                            inventory.setInventorySlotContents(i, itemstack2);
                        } else {
                            this.thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
                        }
                    }
                }
            }
        }
    }
}
