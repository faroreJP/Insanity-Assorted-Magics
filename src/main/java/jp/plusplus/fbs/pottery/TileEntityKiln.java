package jp.plusplus.fbs.pottery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.pottery.BlockKiln;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.pottery.ItemBlockPottery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/27.
 * 焼き物窯
 */
public class TileEntityKiln extends TileEntity implements ISidedInventory {
    public static final int[] slotsMaterial = new int[]{0,1,2,3,4,5,6,7,8};
    public static final int[] slotsProduct = new int[]{10,11,12,13,14,15,16,17,18, 9};
    public static final int[] slotsFuel = new int[]{9};
    public static final int PROGRESS_MAX=20*80*4;
    //public static final int PROGRESS_MAX=20*5;

    public ItemStack[] itemStacks = new ItemStack[19];
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int[] progress=new int[9];
    public Random rand=new Random();

    public void readFromNBT(NBTTagCompound p_145839_1_) {
        super.readFromNBT(p_145839_1_);

        for(int i=0;i<progress.length;i++){
            progress[i]=p_145839_1_.getShort("Progress"+i);
        }

        NBTTagList nbttaglist = p_145839_1_.getTagList("Items", 10);
        this.itemStacks = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.itemStacks.length) {
                this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = p_145839_1_.getShort("BurnTime");
        this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.itemStacks[slotsFuel[0]]);
    }

    public void writeToNBT(NBTTagCompound p_145841_1_) {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setShort("BurnTime", (short) this.furnaceBurnTime);

        for(int i=0;i<progress.length;i++){
            p_145841_1_.setShort("Progress"+i, (short)progress[i]);
        }

        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (this.itemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        p_145841_1_.setTag("Items", nbttaglist);
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int i, int ext) {
        if(i<0 || i>=progress.length) return 0;
        return progress[i] * ext / PROGRESS_MAX;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_) {
        if (this.currentItemBurnTime == 0) {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * p_145955_1_ / this.currentItemBurnTime;
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public void updateEntity() {
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        //燃料燃やすカウンタ
        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote) {
            boolean fuelNotNull=(itemStacks[slotsFuel[0]]!=null);
            boolean materialNotNull=false;
            for(int i=0;i<slotsMaterial.length;i++){
                if(itemStacks[slotsMaterial[i]]!=null){
                    materialNotNull=true;
                    break;
                }
            }

            if (this.furnaceBurnTime != 0 || (fuelNotNull && materialNotNull)) {
                boolean smelt=false;
                boolean[] smelt2=new boolean[slotsMaterial.length];
                for(int i=0;i<slotsMaterial.length;i++){
                    smelt2[i]=false;
                    if(itemStacks[slotsMaterial[i]]==null) continue;
                    if(canSmelt(i)){
                        smelt2[i]=smelt=true;
                    }
                }

                //FBS.logger.info("furnace:"+furnaceBurnTime+","+smelt);

                if (this.furnaceBurnTime == 0 && smelt) {
                    //1つのスロットでも焼ける場合は燃料の更新
                    this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.itemStacks[slotsFuel[0]]);

                    if (this.furnaceBurnTime > 0) {
                        flag1 = true;
                        if (this.itemStacks[slotsFuel[0]] != null) {
                            --this.itemStacks[slotsFuel[0]].stackSize;
                            if (this.itemStacks[slotsFuel[0]].stackSize == 0) {
                                this.itemStacks[slotsFuel[0]] = itemStacks[slotsFuel[0]].getItem().getContainerItem(itemStacks[slotsFuel[0]]);
                            }
                        }
                    }
                }

                if (this.isBurning() && smelt) {
                    for(int i=0;i<slotsMaterial.length;i++){
                        if(!smelt2[i]){
                            progress[i]=0;
                            continue;
                        }
                        progress[i]++;
                        if(progress[i]>=PROGRESS_MAX){
                            progress[i]=0;
                            smeltItem(i);
                            flag1=true;
                        }
                    }
                } else {
                    for(int i=0;i<slotsMaterial.length;i++){
                        progress[i]=0;
                    }
                }
            }

            if (flag != this.furnaceBurnTime > 0) {
                flag1 = true;
                BlockKiln.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private boolean canSmelt(int i) {
        ItemStack stack=itemStacks[slotsMaterial[i]];
        if(stack==null) return false;

        //Pottery以外は弾く
        Item item=stack.getItem();
        if(!(item instanceof ItemBlockPottery)) return false;

        //DRY以外のものは弾く
        IPottery ip=(IPottery)((ItemBlockPottery) item).field_150939_a;
        if(ip.getState(BlockCore.pot.getNBT(stack))!=IPottery.PotteryState.DRY) return false;

        //完成品スロットが開いていたらtrue
        return itemStacks[slotsProduct[i]]==null;
    }

    public void smeltItem(int i) {
        if (this.canSmelt(i)) {
            //完成品
            ItemStack product=itemStacks[slotsMaterial[i]].copy();
            IPottery ip=(IPottery)(((ItemBlock)product.getItem()).field_150939_a);

            //段階を進めて品質をセット
            //product=ip.raiseState(product);
            ip.setState(product, IPottery.PotteryState.BAKED);
            ip.setGrade(product, rand);
            product.setItemDamage(ip.getMetadata(BlockCore.pot.getNBT(product)));

            //魔法の効果
            if(ip.hasEffect(BlockCore.pot.getNBT(product))){
                product=ip.getEnchantedItemStack(product, rand);
            }

            itemStacks[slotsProduct[i]]=product;

            //材料消費
            --this.itemStacks[slotsMaterial[i]].stackSize;
            if (this.itemStacks[slotsMaterial[i]].stackSize <= 0) {
                this.itemStacks[slotsMaterial[i]] = null;
            }
        }
    }

    //-------------------------------------ISidedInventory---------------------------------------------
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public int getSizeInventory() {
        return this.itemStacks.length;
    }
    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return this.itemStacks[p_70301_1_];
    }
    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (this.itemStacks[p_70298_1_] != null) {
            ItemStack itemstack;

            if (this.itemStacks[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.itemStacks[p_70298_1_];
                this.itemStacks[p_70298_1_] = null;
                return itemstack;
            } else {
                itemstack = this.itemStacks[p_70298_1_].splitStack(p_70298_2_);

                if (this.itemStacks[p_70298_1_].stackSize == 0) {
                    this.itemStacks[p_70298_1_] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.itemStacks[p_70304_1_] != null) {
            ItemStack itemstack = this.itemStacks[p_70304_1_];
            this.itemStacks[p_70304_1_] = null;
            return itemstack;
        } else {
            return null;
        }
    }
    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.itemStacks[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
    }
    @Override
    public String getInventoryName() {
        return BlockCore.kiln.getLocalizedName();
    }
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {}
    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        if(p_94041_1_<10) return false;
        if(p_94041_1_==9) return TileEntityFurnace.isItemFuel(p_94041_2_);
        return p_94041_2_.getItem() instanceof ItemBlockPottery;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return p_94128_1_ == 0 ? slotsProduct : (p_94128_1_ == 1 ? slotsMaterial : slotsFuel);
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return this.isItemValidForSlot(p_102007_1_, p_102007_2_);
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        if (p_102008_1_ < 9) return false;
        if (p_102008_1_ == 9) return p_102008_2_.getItem() == Items.bucket;
        return true;
    }
}
