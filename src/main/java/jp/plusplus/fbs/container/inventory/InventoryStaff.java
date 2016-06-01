package jp.plusplus.fbs.container.inventory;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemStaff;
import jp.plusplus.fbs.api.IResonance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class InventoryStaff implements IInventory {
    public ItemStack[] itemStacks;


    protected InventoryPlayer inventoryPlayer;
    protected ItemStack currentItem;
    protected ItemStack[] items;
    public int gemNum;
    public int bookNum;

    public InventoryStaff(InventoryPlayer inventory) {
        inventoryPlayer = inventory;
        currentItem = inventoryPlayer.getCurrentItem();

        if (currentItem != null && currentItem.getItem() instanceof ItemStaff) {
            gemNum = ((ItemStaff) currentItem.getItem()).gemNum;
        }

        //InventorySize
        bookNum=((ItemStaff)currentItem.getItem()).bookNum;
        items = new ItemStack[gemNum+bookNum];
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.items[p_70298_1_] != null) {
            ItemStack itemstack;

            if (this.items[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.items[p_70298_1_];
                this.items[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.items[p_70298_1_].splitStack(p_70298_2_);

                if (this.items[p_70298_1_].stackSize == 0) {
                    this.items[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.items[p_70304_1_] != null) {
            ItemStack itemstack = this.items[p_70304_1_];
            this.items[p_70304_1_] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.items[p_70299_1_] = p_70299_2_;
        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return currentItem.getDisplayName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    /*
        Containerが開かれたタイミングでItemStackの持っているNBTからアイテムを読み込んでいる
     */
    @Override
    public void openInventory() {
        items=ItemStaff.loadInventory(currentItem);
    }

    /*
        Containerを閉じるときに保存
     */
    @Override
    public void closeInventory() {
        ItemStack result = new ItemStack(currentItem.getItem(), 1, currentItem.getItemDamage());
        ItemStaff.saveInventory(result, items);

        //ItemStackをセットする。NBTは右クリック等のタイミングでしか保存されないため再セットで保存している。
        inventoryPlayer.mainInventory[inventoryPlayer.currentItem] = result;

        EntityPlayer p=inventoryPlayer.player;
        if(!p.worldObj.isRemote) {
            String n = result.getTagCompound().getString("MagicName");
            if (n.equals("fbs.failure")){
                p.addChatComponentMessage(new ChatComponentTranslation("info.fbs.magic.resona.failure"));
            }
            else if (Registry.IsResonance(n)){
                p.addChatComponentMessage(new ChatComponentTranslation("info.fbs.magic.resona.success"));
                p.triggerAchievement(AchievementRegistry.resonance);
            }


            if(bookNum>1) {
                IResonance r=Registry.GetResonance(n);
                if(r==null) return;

                String[] books=new String[bookNum];
                for(int i=0;i<bookNum;i++){
                    if(items[i]==null) continue;
                    books[i]=Registry.GetUnlocalizedBookTitleFromItemStack(items[i]);
                }

                String s = "";
                int i = 0;
                boolean find = false, find1=false;
                for (; i < bookNum; i++) {
                    if (items[i] != null && items[i].getItem() == ItemCore.bookSorcery) {
                        s = ItemCore.bookNoDecoded.getLocalizedBookTitle(items[i]);
                        find = true;
                        break;
                    }
                }
                for (i++; i < bookNum; i++) {
                    if (items[i] != null && items[i].getItem() == ItemCore.bookSorcery) {
                        s += " + " + ItemCore.bookNoDecoded.getLocalizedBookTitle(items[i]);
                        find1 = true;
                        break;
                    }
                }
                if (find && find1) {
                    s += " -> " + r.getDisplayMagicName(books);
                    p.addChatComponentMessage(new ChatComponentText(s));
                }
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack item) {
        if(item==null) return false;
        return item.getItem()==ItemCore.bookSorcery;
    }
}
