package jp.plusplus.fbs.container;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.container.inventory.InventoryContract;
import jp.plusplus.fbs.container.slot.SlotBonfire;
import jp.plusplus.fbs.container.slot.SlotContract;
import jp.plusplus.fbs.container.slot.SlotInventory;
import jp.plusplus.fbs.container.slot.SlotTakeOnly;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.item.ItemStoneSpirit;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class ContainerContract extends Container {
    public EntityPlayer player;
    public InventoryContract inventory;

    public ContainerContract(EntityPlayer player){
        inventory=new InventoryContract(player);
        inventory.openInventory();

        for(int i=0;i<2;i++){
            addSlotToContainer(new SlotContract(inventory, i, 26+18*i, 46, i));
        }
        addSlotToContainer(new SlotContract(inventory, 2, 98, 23, 2));
        addSlotToContainer(new SlotTakeOnly(inventory, 3, 133, 40));

        //player's inv
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new SlotInventory(player.inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18, 2));
            }
        }

        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new SlotInventory(player.inventory, j, 8 + j * 18, 142, 2));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.inventory.closeInventory();
    }

    public boolean canContract(){
        if(inventory.itemStacks[0]==null || !SpiritManager.isTool(inventory.itemStacks[0].getItem())) return false;
        if(inventory.itemStacks[1]==null || !(inventory.itemStacks[1].getItem() instanceof ItemStoneSpirit)) return false;
        if(inventory.itemStacks[2]==null) return false;
        Registry.BookData bd=Registry.GetBookDataFromItemStack(inventory.itemStacks[2]);
        return bd!=null && bd.title.equals("fbs.contract") && ItemBookSorcery.getMagicMaxUse(inventory.itemStacks[2])>0 && inventory.itemStacks[3]==null;
    }

    public String contract(String name, EntityPlayer player){
        if(!canContract()) return null;
        SpiritManager.ToolEntry te=SpiritManager.getTool(inventory.itemStacks[0].getItem());
        if(te!=null){
            boolean m=((ItemStoneSpirit)inventory.itemStacks[1].getItem()).getSex();
            inventory.setInventorySlotContents(3, te.getSpiritToolStack(m, SpiritManager.getRandomCharacter(m), name, player, inventory.itemStacks[0]));
            inventory.setInventorySlotContents(0, null);
            inventory.setInventorySlotContents(1, null);
            ItemBookSorcery.reduceMagicMaxUse(inventory.itemStacks[2]);

            if(!player.worldObj.isRemote) player.triggerAchievement(AchievementRegistry.contract);

            SpiritStatus status=SpiritStatus.readFromNBT(inventory.itemStacks[3].getTagCompound());
            SpiritManager.talk(player, status.getCharacter(), "first", inventory.itemStacks[3]);
            return status.getCharacter();
        }
        return null;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack itemStack = null;
        return itemStack;
    }
}
