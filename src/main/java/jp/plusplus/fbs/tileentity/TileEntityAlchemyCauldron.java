package jp.plusplus.fbs.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicQuality;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by plusplus_F on 2015/09/23.
 */
public class TileEntityAlchemyCauldron extends TileEntity implements IInventory{
    public static final int MATERIAL=0;
    public static final int PRODUCT=1;
    public static final int RECIPE=2;
    public ItemStack[] itemStacks=new ItemStack[3];
    public ArrayList<ItemStack> inputMaterial=new ArrayList<ItemStack>();
    public ArrayList<MaterialPair> materials=new ArrayList<MaterialPair>(); //描画用

    protected ItemStack lastMaterialItemStack;
    protected ItemStack lastProductItemStack;
    protected ItemStack lastRecipeItemStack;
    public AlchemyRegistry.Recipe cachedRecipe;

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);

        //アイテムスロット
        boolean productFlag=itemStacks[PRODUCT]==null;
        NBTTagList nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt.getByte("Slot");

            if (b0 >= 0 && b0 < itemStacks.length) {
                itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbt);
            }
        }

        //投入済み素材
        nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("InputMaterials");
        inputMaterial.clear();
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            inputMaterial.add(ItemStack.loadItemStackFromNBT(nbt));
        }

        //描画用素材リスト
        nbttaglist = (NBTTagList) par1NBTTagCompound.getTag("Materials");
        materials.clear();
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            MaterialPair p=new MaterialPair(nbt.getString("Name"));
            if(nbt.getBoolean("Satisfy")) p.set(true);
            materials.add(p);
        }

        //キャッシュたち
        if(par1NBTTagCompound.hasKey("LastMaterial")) lastMaterialItemStack=ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("LastMaterial"));
        else lastMaterialItemStack=null;

        if(par1NBTTagCompound.hasKey("LastProduct")) lastProductItemStack=ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("LastProduct"));
        else lastProductItemStack=null;

        if(par1NBTTagCompound.hasKey("LastRecipe")) lastRecipeItemStack=ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("LastRecipe"));
        else lastRecipeItemStack=null;

        if(par1NBTTagCompound.hasKey("CachedRecipe")) cachedRecipe=AlchemyRegistry.GetRecipe(ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("CachedRecipe")));
        else cachedRecipe=null;

    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        //アイテムスロット
        NBTTagList nbttaglist = new NBTTagList();
        for (int i=0;i<itemStacks.length;i++){
            if (itemStacks[i] != null){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                itemStacks[i].writeToNBT(nbt);
                nbttaglist.appendTag(nbt);
            }
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        //投入済み素材
        nbttaglist = new NBTTagList();
        for (int i=0;i<inputMaterial.size();i++) {
            NBTTagCompound nbt = new NBTTagCompound();
            inputMaterial.get(i).writeToNBT(nbt);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("InputMaterials", nbttaglist);

        //描画用素材リスト
        nbttaglist = new NBTTagList();
        for (int i=0;i<materials.size();i++) {
            NBTTagCompound nbt = new NBTTagCompound();
            MaterialPair p=materials.get(i);
            nbt.setString("Name", p.getName());
            nbt.setBoolean("Satisfy", p.get());
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("Materials", nbttaglist);

        //キャッシュたち
        if(lastMaterialItemStack!=null){
            NBTTagCompound nbt=new NBTTagCompound();
            lastMaterialItemStack.writeToNBT(nbt);
            par1NBTTagCompound.setTag("LastMaterial", nbt);
        }
        if(lastProductItemStack!=null){
            NBTTagCompound nbt=new NBTTagCompound();
            lastProductItemStack.writeToNBT(nbt);
            par1NBTTagCompound.setTag("LastProduct", nbt);
        }
        if(lastRecipeItemStack!=null){
            NBTTagCompound nbt=new NBTTagCompound();
            lastRecipeItemStack.writeToNBT(nbt);
            par1NBTTagCompound.setTag("LastRecipe", nbt);
        }
        if(cachedRecipe!=null){
            NBTTagCompound nbt=new NBTTagCompound();
            cachedRecipe.getKey().writeToNBT(nbt);
            par1NBTTagCompound.setTag("CachedRecipe", nbt);
        }
    }

    @Override
    public void updateEntity(){
        boolean recipeChanged=false;
        boolean materialsChanged=false;
        boolean productChanged=false;

        //-----------------------------------------------------------------------
        // 完成品スロットの更新
        //-----------------------------------------------------------------------
        if(itemStacks[PRODUCT]!=lastProductItemStack){
            productChanged=true;
            lastProductItemStack=itemStacks[PRODUCT];
        }

        //-----------------------------------------------------------------------
        // レシピの更新
        //-----------------------------------------------------------------------
        if(itemStacks[RECIPE]!=lastRecipeItemStack){
            AlchemyRegistry.Recipe last=cachedRecipe;

            if(itemStacks[RECIPE]!=null)  cachedRecipe=AlchemyRegistry.GetRecipe(itemStacks[RECIPE]);
            else cachedRecipe=null;

            if(last!=cachedRecipe){
                recipeChanged=true;
                //アイテムのドロップ
                if(!worldObj.isRemote){
                    for(ItemStack it : inputMaterial){
                        dropItemStack(it);
                    }
                }
                inputMaterial.clear();

                //素材リストの更新
                materials.clear();
                if(cachedRecipe!=null){
                    for(String str : cachedRecipe.getMaterialList()){
                        materials.add(new MaterialPair(str));
                    }
                }
            }
        }
        lastRecipeItemStack=itemStacks[RECIPE];

        //-----------------------------------------------------------------------
        // 投入素材の更新
        //-----------------------------------------------------------------------
        if(itemStacks[RECIPE]!=null && cachedRecipe!=null && (recipeChanged || productChanged || itemStacks[MATERIAL]!=lastMaterialItemStack)){
            //素材スロットの更新
            while(itemStacks[MATERIAL]!=null && AlchemyRegistry.IsMaterial(cachedRecipe, itemStacks[MATERIAL], inputMaterial)){
                materialsChanged=true;

                //素材リストの更新
                for(MaterialPair it : materials){
                    if(!it.get() && AlchemyRegistry.IsMatching(it.getName(), itemStacks[MATERIAL])){
                        it.set(true);
                        break;
                    }
                }
                ItemStack itc=itemStacks[MATERIAL].copy();
                itc.stackSize=1;
                inputMaterial.add(itc);

                //素材スロットの更新
                    itemStacks[MATERIAL].stackSize--;
                    if(itemStacks[MATERIAL].stackSize<=0) setInventorySlotContents(MATERIAL, null);

            }
        }
        lastMaterialItemStack=itemStacks[MATERIAL];

        //-----------------------------------------------------------------------
        // 完成品の生成
        //-----------------------------------------------------------------------
        /*
        if((productChanged || materialsChanged) && itemStacks[PRODUCT]==null && AlchemyRegistry.IsSatisfied(cachedRecipe, inputMaterial)){
            if(!worldObj.isRemote) setInventorySlotContents(PRODUCT, AlchemyRegistry.GetAlchemyProduct(cachedRecipe, inputMaterial));
            inputMaterial.clear();

            //素材リストの更新
            materials.clear();
            if(cachedRecipe!=null){
                for(String str : cachedRecipe.getMaterialList()){
                    materials.add(new MaterialPair(str));
                }
            }
            productChanged=true;
        }
        */

        if(materialsChanged || productChanged || recipeChanged) markDirty();
    }

    /**
     * 周囲にアイテムスタックを落とす
     * @param itemStack
     */
    protected void dropItemStack(ItemStack itemStack){
        if (itemStack != null){
            float f =  AlchemyRegistry.getRandom().nextFloat() * 0.8F + 0.1F;
            float f1 = AlchemyRegistry.getRandom().nextFloat() * 0.8F + 0.1F;
            float f2 = AlchemyRegistry.getRandom().nextFloat() * 0.8F + 0.1F;

            while (itemStack.stackSize > 0){
                int k1 = AlchemyRegistry.getRandom().nextInt(21) + 10;

                if (k1 > itemStack.stackSize){
                    k1 = itemStack.stackSize;
                }

                itemStack.stackSize -= k1;
                EntityItem entityitem = new EntityItem(worldObj, (double)((float)xCoord + f), (double)((float)yCoord + f1), (double)((float)zCoord + f2), new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));

                if (itemStack.hasTagCompound()){
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (double)((float)AlchemyRegistry.getRandom().nextGaussian() * f3);
                entityitem.motionY = (double)((float)AlchemyRegistry.getRandom().nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double)((float)AlchemyRegistry.getRandom().nextGaussian() * f3);
                worldObj.spawnEntityInWorld(entityitem);
            }
        }
    }

    public float getCompoundingProbability(EntityPlayer ep){
        int mLv=FBSEntityPropertiesAPI.GetMagicLevel(ep);
        int lv=cachedRecipe.getLevel();

        float p=cachedRecipe.getProb()+0.02f*Math.max(mLv-lv, 0);
        if(p<0) p=0;
        if(p>5.f) p=5.f;

        return p;
    }

    public boolean canCompounding(){
        return itemStacks[PRODUCT]==null && AlchemyRegistry.IsSatisfied(cachedRecipe, inputMaterial);
    }

    /**
     * 調合をする
     * @param ep
     */
    public void tryCompounding(EntityPlayer ep){
        if(this.cachedRecipe==null) return;

        //成功判定
        float pr=getCompoundingProbability(ep);
        if(worldObj.rand.nextFloat()<pr){
            setInventorySlotContents(PRODUCT, AlchemyRegistry.GetAlchemyProduct(cachedRecipe, inputMaterial));

            //品質の向上
            if(pr>1.f){
                ItemStack product=getStackInSlot(PRODUCT);
                if(product!=null){
                    ArrayList<CharacteristicBase> cbs=AlchemyRegistry.ReadCharacteristicFromNBT(product.getTagCompound());

                    while(pr>1.f){
                        float pp=pr-1.f;
                        if(worldObj.rand.nextFloat()<pp){
                            //品質は2,(null),0,1の順で変化する
                            CharacteristicQuality cq=null;
                            for(CharacteristicBase cb : cbs){
                                if(cb instanceof CharacteristicQuality){
                                    cq=(CharacteristicQuality)cb;
                                    break;
                                }
                            }
                            if(cq==null){
                                cq=new CharacteristicQuality();
                                cq.setValue(0);
                                cbs.add(cq);
                            }

                            if(cq.getValue()==0) cq.setValue(1);
                            else if(cq.getValue()==2) cbs.remove(cq);
                        }

                        pr-=1.f;
                    }
                    NBTTagCompound nbt=new NBTTagCompound();
                    AlchemyRegistry.WriteCharacteristicToNBT(nbt, cbs);
                    product.setTagCompound(nbt);
                }
            }
        }

        //素材の消費
        inputMaterial.clear();

        //素材リストの更新
        materials.clear();
        if(cachedRecipe!=null){
            for(String str : cachedRecipe.getMaterialList()){
                materials.add(new MaterialPair(str));
            }
        }

        markDirty();
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord-1,yCoord,zCoord-1,xCoord+2,yCoord+1,zCoord+2);
    }

    //--------------------------------------------------------------------------------------------

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        if(p_70301_1_<0 || p_70301_1_>=itemStacks.length) return null;
        return itemStacks[p_70301_1_];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (itemStacks[i] != null){
            ItemStack itemstack;

            if (itemStacks[i].stackSize <= j){
                itemstack = itemStacks[i];
                itemStacks[i] = null;
                markDirty();
                return itemstack;
            }
            else{
                itemstack = itemStacks[i].splitStack(j);

                if (itemStacks[i].stackSize == 0){
                    itemStacks[i] = null;
                }
                markDirty();
                return itemstack;
            }
        }
        else{
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (itemStacks[i] != null){
            ItemStack itemstack = itemStacks[i];
            itemStacks[i] = null;
            markDirty();
            return itemstack;
        }
        else{
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()){
            itemStack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
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
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : entityPlayer.getDistanceSq((double)xCoord+0.5D, (double)yCoord+0.5D, (double)zCoord+0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    public class MaterialPair{
        public String name;
        public boolean satisfy;

        public MaterialPair(String name){
            this.name=name;
        }
        public void set(boolean flag){ satisfy=flag; }
        public boolean get(){ return satisfy; }
        public String getName(){ return name; }
    }
}
