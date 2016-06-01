package jp.plusplus.fbs.container.inventory;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.IAlchemyMaterial;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import shift.mceconomy2.MCEconomy2;
import shift.mceconomy2.api.MCEconomyAPI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/10/19.
 */
public class InventoryBonfire implements IInventory {
    public ItemStack[] itemStacks=new ItemStack[4];
    public Random rand=new Random();
    public EntityPlayer player;

    private int x,y,z;

    public InventoryBonfire(int x, int y, int z, EntityPlayer player){
        this.x=x;
        this.y=y;
        this.z=z;
        this.player=player;
    }

    public float getExtractProb(EnchantmentPair ench, int mLv){
        float p=0.5f;
        p-=0.15f*(ench.lv-1);
        p+=0.025f*mLv;
        return p;
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return itemStacks[p_70301_1_];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.itemStacks[p_70298_1_] != null) {
            ItemStack itemstack;

            if (this.itemStacks[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.itemStacks[p_70298_1_];
                this.itemStacks[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.itemStacks[p_70298_1_].splitStack(p_70298_2_);

                if (this.itemStacks[p_70298_1_].stackSize == 0) {
                    this.itemStacks[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.itemStacks[p_70304_1_] != null) {
            ItemStack itemstack = this.itemStacks[p_70304_1_];
            this.itemStacks[p_70304_1_] = null;
            return itemstack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.itemStacks[p_70299_1_] = p_70299_2_;
        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return BlockCore.bonfire.getLocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        //焼却スロットにアイテムがある場合
        if(itemStacks[0]!=null){
            if(!player.worldObj.isRemote) {
                //--------------------------------焼却経験値の生成------------------------------------------
                float rate;
                int size = itemStacks[0].stackSize;
                int tmp;

                //レートの決定
                float value;
                if(itemStacks[0].isItemStackDamageable()){
                    ItemStack is=itemStacks[0].copy();
                    is.setItemDamage(0);
                    value=MCEconomyAPI.getPurchase(is)*(1.f-(float)itemStacks[0].getItemDamage()/(float)itemStacks[0].getMaxDamage());
                }
                else{
                    value=MCEconomyAPI.getPurchase(itemStacks[0]);
                }
                rate=0.001f*value;
                if(rate<0.02f) rate=0.02f;
                if(rate>1.f) rate=1.f;

                //経験値の決定
                if(rate<1.f){
                    tmp = MathHelper.floor_float(size * rate);
                    if (tmp < MathHelper.ceiling_float_int(size * rate) && (float) Math.random() < size * rate - (float) tmp) {
                        tmp++;
                    }
                    size = tmp;
                }

                while (size > 0) {
                    tmp = EntityXPOrb.getXPSplit(size);
                    size -= tmp;
                    player.worldObj.spawnEntityInWorld(new EntityXPOrb(player.worldObj, player.posX, player.posY + 0.5D, player.posZ + 0.5D, tmp));
                }

                //焼却アイテムがエンチャントされてあり、かつハーブスロットにハーブが存在する場合
                //--------------------------------ESの生成------------------------------------------
                if (itemStacks[0].isItemEnchanted() || itemStacks[0].getItem()==Items.enchanted_book) {
                    int herb = 0;
                    for (int i = 1; i < 4; i++) {
                        if (itemStacks[i] == null) continue;
                        if (!(itemStacks[i].getItem() instanceof IAlchemyMaterial)) continue;

                        if (AlchemyRegistry.IsMatching("herb", itemStacks[i])) {
                            herb++;
                        }
                    }

                    if (herb > 0) {
                        //エンチャントの抽出
                        ArrayList<EnchantmentPair> list = new ArrayList<EnchantmentPair>();
                        Map enchantments = EnchantmentHelper.getEnchantments(itemStacks[0]);
                        for (Object obj : enchantments.entrySet()) {
                            Map.Entry e = (Map.Entry) obj;
                            list.add(new EnchantmentPair((Integer) e.getKey(), (Integer) e.getValue()));
                        }

                        int mLv=FBSEntityPropertiesAPI.GetMagicLevel(player);

                        //ハーブの数だけ繰り返す
                        for (; herb > 0 && !list.isEmpty(); herb--) {
                            int index = rand.nextInt(list.size());
                            EnchantmentPair ep = list.get(index);
                            if (!player.capabilities.isCreativeMode && rand.nextFloat() >= getExtractProb(ep, mLv)){
                                String str=String.format(StatCollector.translateToLocal("info.fbs.enchant.extFailure"), Enchantment.enchantmentsList[ep.id].getTranslatedName(ep.lv));
                                player.addChatComponentMessage(new ChatComponentText(str));
                                continue; //確率で失敗
                            }

                            //経験値
                            double exp=100 * ep.lv;
                            FBSEntityPropertiesAPI.AddExp(player, exp, true);

                            String str=String.format(StatCollector.translateToLocal("info.fbs.enchant.extSuccess"), Enchantment.enchantmentsList[ep.id].getTranslatedName(ep.lv));
                            str=str+String.format("(EXP+%.2f)", exp);
                            player.addChatComponentMessage(new ChatComponentText(str));

                            //ESの生成
                            ItemStack es = new ItemStack(ItemCore.enchantScroll);
                            LinkedHashMap ench = new LinkedHashMap();
                            ench.put(ep.id, ep.lv);
                            EnchantmentHelper.setEnchantments(ench, es);

                            player.entityDropItem(es, 0);
                            list.remove(index);

                            player.triggerAchievement(AchievementRegistry.extract);
                        }
                    }
                }
            }

            for(int i=0;i<itemStacks.length;i++){
                itemStacks[i]=null;
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return p_70300_1_.worldObj.getBlockMetadata(x,y,z)>0;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {
        if(player.worldObj.isRemote) return;

        for(int i=0;i<itemStacks.length;i++){
            ItemStack itemstack = this.getStackInSlot(i);

            if (itemstack != null){
                float f = this.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0){
                    int k1 = this.rand.nextInt(21) + 10;

                    if (k1 > itemstack.stackSize){
                        k1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= k1;
                    double x=player.posX+f;
                    double y=player.posY+f1+player.getEyeHeight();
                    double z=player.posZ+f2;
                    EntityItem entityitem = new EntityItem(player.worldObj, x, y, z, new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));

                    if (itemstack.hasTagCompound()){
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                    player.worldObj.spawnEntityInWorld(entityitem);
                }
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }

    public static class EnchantmentPair{
        int id, lv;
        public EnchantmentPair(int id, int lv){
            this.id=id;
            this.lv=lv;
        }
    }
}
