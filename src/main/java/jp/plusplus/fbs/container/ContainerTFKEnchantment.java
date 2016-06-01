package jp.plusplus.fbs.container;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.container.inventory.InventoryEnchantment;
import jp.plusplus.fbs.container.slot.SlotInventory;
import jp.plusplus.fbs.container.slot.SlotTakeOnly;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.item.ItemEnchantScroll;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.util.Map;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/21.
 */
public class ContainerTFKEnchantment extends Container {
    public boolean enchanted;
    public InventoryEnchantment inventory;
    public EntityPlayer player;

    public ContainerTFKEnchantment(EntityPlayer player){
        this.player=player;
        inventory=new InventoryEnchantment(player);

        addSlotToContainer(new Slot(inventory, 0, 44, 31));
        addSlotToContainer(new Slot(inventory, 1, 62, 31));
        addSlotToContainer(new SlotTakeOnly(inventory, 2, 120, 31));

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

    public double getEnchantExp(int enchLv){
        return 100 * enchLv;
    }
    public float getEnchantProbability(){
        ItemStack es=inventory.getStackInSlot(1);
        if(es==null) return 0;
        Map ench=EnchantmentHelper.getEnchantments(es);
        Map.Entry e=(Map.Entry)ench.entrySet().iterator().next();
        return getEnchantProbability((Integer)e.getValue());
    }
    public float getEnchantProbability(int enchLv){
        float p=0.5f;
        p-=0.1f*(enchLv-1);
        p+=0.01f*FBSEntityPropertiesAPI.GetMagicLevel(player);
        return p;
    }
    public boolean canEnchant(){
        ItemStack itemStack=inventory.getStackInSlot(0);
        if(itemStack==null || !itemStack.getItem().isItemTool(itemStack)){
            return false;
        }

        ItemStack es=inventory.getStackInSlot(1);
        if(es==null || !(es.getItem() instanceof ItemEnchantScroll) || !es.isItemEnchanted()){
            return false;
        }

        return true;
    }
    public void tryEnchant(){
        if(!canEnchant()) return;

        Random rand=new Random();
        ItemStack itemStack=inventory.getStackInSlot(0);
        ItemStack es=inventory.getStackInSlot(1);

        Map ench=EnchantmentHelper.getEnchantments(es);
        Map.Entry e=(Map.Entry)ench.entrySet().iterator().next();
        int eLv=(Integer)e.getValue();

        if(rand.nextFloat()<getEnchantProbability(eLv)){
            //成功
            player.triggerAchievement(AchievementRegistry.enchant);

            //経験値
            FBSEntityPropertiesAPI.AddExp(player, getEnchantExp(eLv), true);

            //エンチャント重複判定，重複している場合は新しいもので上書きする
            Map itemEnchantmentList=EnchantmentHelper.getEnchantments(itemStack);
            if(itemEnchantmentList.containsKey(e.getKey())) {
                //重複してる場合は削除
                itemEnchantmentList.remove(e.getKey());
            }

            //エンチャント付与
            itemEnchantmentList.put(e.getKey(), e.getValue());
            EnchantmentHelper.setEnchantments(itemEnchantmentList, itemStack);

            //スロット操作
            inventory.setInventorySlotContents(0, null);
            inventory.setInventorySlotContents(1, null);
            inventory.setInventorySlotContents(2, itemStack);
        }
        else{
            //失敗e

            //経験値
            FBSEntityPropertiesAPI.AddExp(player, 0.2*getEnchantExp(eLv), true);

            //アイテムの耐久減らす
            float d=(0.1f+(0.1f+0.1f*(eLv-1))*rand.nextFloat())*itemStack.getMaxDamage();
            itemStack.setItemDamage(itemStack.getItemDamage() + MathHelper.floor_float(d));
            inventory.setInventorySlotContents(0, null);
            if(itemStack.getItemDamage()<itemStack.getMaxDamage()) inventory.setInventorySlotContents(2, itemStack);

            //ESの耐久減らす
            int mLv=FBSEntityPropertiesAPI.GetMagicLevel(player);
            float dMin=0.1f+0.4f*(1.0f-(float)mLv/FBSEntityProperties.MAGIC_LEVEL_MAX);
            d=dMin*es.getMaxDamage();
            es.setItemDamage(es.getItemDamage()+ MathHelper.floor_float(d));
            if(es.getItemDamage()>=es.getMaxDamage()) inventory.setInventorySlotContents(1, null);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        return itemstack;
    }
    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        this.inventory.closeInventory();
    }

}
