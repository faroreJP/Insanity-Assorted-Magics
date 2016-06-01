package jp.plusplus.fbs.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.container.slot.SlotTakeOnlyWithMagicEXP;
import jp.plusplus.fbs.tileentity.TileEntityAlchemyCauldron;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class ContainerAlchemyCauldron extends Container {
    public TileEntityAlchemyCauldron entity;
    public EntityPlayer player;

    public ContainerAlchemyCauldron(EntityPlayer player, TileEntityAlchemyCauldron tileEntity) {
        this.entity = tileEntity;
        this.player=player;

        //inventory's inventory
        this.addSlotToContainer(new Slot(this.entity, 0, 26, 19));
        this.addSlotToContainer(new SlotTakeOnlyWithMagicEXP(player, this.entity, 1, 134, 39, 1));
        this.addSlotToContainer(new Slot(this.entity, 2, 8, 19));

        //player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 119 + i * 18));
            }
        }

        //player slots
        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 177));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
    }
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return entity.isUseableByPlayer(entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {}

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        return itemStack;
    }
}
