package jp.plusplus.fbs.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.ProxyClient;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class ContainerMealTerminal extends Container {
    public InventoryTerminal inv;
    public TileEntityMeal meal;
    private int state;
    private int fuck1=-1;
    private Set fuckSet1=new HashSet();
    public EntityPlayer player;

    public ContainerMealTerminal(EntityPlayer player, TileEntityMealTerminal meal){
        this.player=player;
        this.meal=ItemMealFragment.getTileEntity(meal.getFragment());
        inv=new InventoryTerminal(this.meal);
        inv.openInventory();

        SlotTerminal.dontUpdate=inv.dontUpdate=true;
        for(int i=0;i<54;i++) addSlotToContainer(new SlotTerminal(inv, i, 8+18*(i%9), 18+18*(i/9)));

        for(int i=0;i<27;i++) addSlotToContainer(new Slot(player.inventory, 9+i, 8+18*(i%9), 140+18*(i/9)));
        for(int i=0;i<9;i++) addSlotToContainer(new Slot(player.inventory, i, 8+18*(i%9), 198+18*(i/9)));
        SlotTerminal.dontUpdate=inv.dontUpdate=false;
    }

    public void scrollTo(float f){
        int preShift=(int)(((inv.allItem.length-54)/9+1)*inv.scroll);

        inv.scroll=f;
        inv.markDirty();

        if(player.worldObj.isRemote && preShift!=(int)(((inv.allItem.length-54)/9+1)*inv.scroll)){
            PacketHandler.INSTANCE.sendToServer(new MessageMealTerminalScroll(player.worldObj.provider.dimensionId, player, inv.scroll));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return meal.getWorldObj().getBlock(meal.xCoord, meal.yCoord, meal.zCoord)==BlockCore.mealCrystal;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        inv.closeInventory();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void putStacksInSlots(ItemStack[] p_75131_1_) {
        SlotTerminal.dontUpdate=true;
        super.putStacksInSlots(p_75131_1_);
        SlotTerminal.dontUpdate=false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        Slot slot=(Slot)inventorySlots.get(p_82846_2_);
        if(slot instanceof SlotTerminal || slot.getStack()==null) return null;

        ItemStack itemStack=slot.getStack();
        slot.putStack(null);

        if(!p_82846_1_.worldObj.isRemote){
            ItemStack tmp = itemStack.copy();
            meal.insertItemStack(tmp);
            inv.markDirty();
        }


        return null;
    }

    /*
        @Mojang
        　　　　fﾆヽ
        　　　　|_||
        　　　　|= |
        　　　　|　|
        　　　　|= |
        　　 i⌒|　|⌒i_
        　 ／|　|　|　| ヽ
        　｜ (　(　(　(　|
        　｜/　　　　　　|
        　｜　　　　　　 |
        　 ＼　　　　　 ノ
        　　 ＼　　　　/
        　　　｜　　　｜
    */
    @Override
    public ItemStack slotClick(int slotIndex, int button, int type, EntityPlayer player) {
        boolean isClient=(player.worldObj.isRemote);

        //FBS.logger.info("slot:"+slotIndex+","+button+","+type+","+player.toString());

        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = player.inventory;
        int i1;
        //ItemStack itemstack3;
        //FBS.logger.info("item:"+inventoryplayer.getItemStack());

        //通常のスロットであれば普通に処理する
        if (slotIndex!=-999 && (slotIndex<0 || !(inventorySlots.get(slotIndex) instanceof SlotTerminal))){
            return super.slotClick(slotIndex, button, type, player);
        }

        try{
            Class c=Container.class;
            Field field=c.getDeclaredField("field_94536_g");
            field.setAccessible(true);
            if(field.getInt(this)!=0){
                func_94533_d();
                return null;
            }
        }catch (Exception e){
            FBS.logger.error(e);
        }

        if(state!=0){
            this.func_94533_d();
            return null;
        }

        Slot slot2;
        int size;
        ItemStack itemstack5;

        if(slotIndex==-999 && button==0 && type==0){
            return inventoryplayer.getItemStack();
        }
        if(type==1){
            return inventoryplayer.getItemStack();
        }

        if(!isClient) return inventoryplayer.getItemStack();

        ///////////////////////////////////////////////////////////////////////////////////////
        //
        // シフトを押さない左または右クリック
        //
        ///////////////////////////////////////////////////////////////////////////////////////
        if (type==0 && (button == 0 || button == 1)) {
            if (slotIndex == -999) {
                ///////////////////////////////////////////////////////////////////////////////////////
                //
                // 持っているアイテムを捨てる処理
                //
                ///////////////////////////////////////////////////////////////////////////////////////

                if (inventoryplayer.getItemStack() != null && slotIndex == -999) {
                    if (button == 0) {
                        player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack((ItemStack) null);
                    }

                    if (button == 1) {
                        player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), true);

                        if (inventoryplayer.getItemStack().stackSize == 0) {
                            inventoryplayer.setItemStack((ItemStack) null);
                        }
                    }
                }
            } else {
                if (slotIndex < 0) {
                    return null;
                }

                slot2 = (Slot) this.inventorySlots.get(slotIndex);
                if (slot2 == null) return null;

                ItemStack slotItem = slot2.getStack();
                ItemStack playerCurrentItem = inventoryplayer.getItemStack();
                if (slotItem != null) itemstack = slotItem.copy();

                if (slotItem == null) {
                    ////////////////////////////////////////////////////////////////////////////////////////
                    //
                    // スロットが空の場合
                    //
                    ////////////////////////////////////////////////////////////////////////////////////////
                    if (playerCurrentItem != null && slot2.isItemValid(playerCurrentItem)) {
                        ////////////////////////////////////////////////////////////////////////////////////////
                        //
                        // 空のスロットに持っているアイテムを置く処理
                        //
                        ////////////////////////////////////////////////////////////////////////////////////////
                        size = button == 0 ? playerCurrentItem.stackSize : 1;
                        playerCurrentItem.splitStack(size);

                        ItemStack tmp = playerCurrentItem.copy();
                        tmp.stackSize = size;
                        //meal.insertItemStack(tmp);
                        //inv.markDirty();

                        if (isClient) {
                            PacketHandler.INSTANCE.sendToServer(new MessageMealTerminal(player, meal, 0, tmp));
                        }

                        if (playerCurrentItem.stackSize == 0) {
                            inventoryplayer.setItemStack((ItemStack) null);
                        }

                    }
                } else if (slot2.canTakeStack(player)) {
                    if (playerCurrentItem == null) {
                        ////////////////////////////////////////////////////////////////////////////////////////
                        //
                        // 何も持っていないときにスロットからアイテムを取り出す処理
                        //
                        ////////////////////////////////////////////////////////////////////////////////////////
                        size = (button == 0 ? slotItem.stackSize : (slotItem.stackSize + 1) / 2);
                        if (size > slotItem.getMaxStackSize()) size = slotItem.getMaxStackSize();

                        //itemstack5 = slot2.decrStackSize(size);
                        itemstack5=slotItem.copy();
                        itemstack5.stackSize=size;
                        inventoryplayer.setItemStack(itemstack5);

                        FBS.logger.info("picked up:"+itemstack5);

                        ItemStack tmp = itemstack5.copy();
                        //meal.exportItemStack(tmp, tmp.stackSize);
                        //inv.markDirty();

                        if(isClient){
                            //PacketHandler.INSTANCE.sendTo(new MessageMealTerminal(player, meal, 1, tmp), (EntityPlayerMP) player);
                            PacketHandler.INSTANCE.sendToServer(new MessageMealTerminal(player, meal, 1, tmp));
                        }

                        //slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
                    } else if (slot2.isItemValid(playerCurrentItem)) {
                        ////////////////////////////////////////////////////////////////////////////////////////
                        //
                        // アイテムをミールへ送る処理
                        //
                        ////////////////////////////////////////////////////////////////////////////////////////
                        size = button == 0 ? playerCurrentItem.stackSize : 1;

                        playerCurrentItem.splitStack(size);
                        if (playerCurrentItem.stackSize == 0) {
                            inventoryplayer.setItemStack((ItemStack) null);
                        }

                        ItemStack tmp = playerCurrentItem.copy();
                        tmp.stackSize = size;
                        //meal.insertItemStack(tmp);
                        //inv.markDirty();

                        if(isClient){
                            PacketHandler.INSTANCE.sendToServer(new MessageMealTerminal(player, meal, 0, tmp));
                        }
                    }
                }
            }
        }
        return itemstack;
    }


    @Override
    protected void func_94533_d() {
        super.func_94533_d();
        state = 0;
        fuckSet1.clear();
    }

    public static class SlotTerminal extends Slot{
        public static boolean dontUpdate;

        public SlotTerminal(InventoryTerminal inv, int index, int x, int y) {
            super(inv, index, x, y);
        }

        @Override
        public int getSlotStackLimit() {
            return Integer.MAX_VALUE;
        }
    }
}
