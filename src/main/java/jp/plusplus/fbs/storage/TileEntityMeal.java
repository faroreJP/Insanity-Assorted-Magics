package jp.plusplus.fbs.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.*;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class TileEntityMeal extends TileEntity {
    protected ArrayList<ItemStack> listBlocks=new ArrayList<ItemStack>();
    protected ArrayList<ItemStack> listItems=new ArrayList<ItemStack>();

    private LinkedList<ItemStack> cache=new LinkedList<ItemStack>();

    public static final int FRAGEMNT_INTERVAL=20*60*5;
    protected int fragmentTicks=FRAGEMNT_INTERVAL;

    private boolean loaded;

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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound){
        super.readFromNBT(par1NBTTagCompound);

        cache.clear();

        fragmentTicks=par1NBTTagCompound.getInteger("FragmentTicks");

        NBTTagList nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Items");
        listItems.clear();
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            ItemStack it=ItemStack.loadItemStackFromNBT(nbt);
            it.stackSize=nbt.getInteger("StackSize");
            listItems.add(it);
        }

        nbttaglist = (NBTTagList)par1NBTTagCompound.getTag("Blocks");
        listBlocks.clear();
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
            ItemStack it=ItemStack.loadItemStackFromNBT(nbt);
            it.stackSize=nbt.getInteger("StackSize");
            listBlocks.add(it);
        }
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound){
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("FragmentTicks", fragmentTicks);

        NBTTagList nbttaglist = new NBTTagList();
        for(ItemStack it : listItems){
            NBTTagCompound nbt = new NBTTagCompound();
            it.writeToNBT(nbt);
            nbt.setInteger("StackSize", it.stackSize);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("Items", nbttaglist);

        nbttaglist = new NBTTagList();
        for(ItemStack it : listBlocks){
            NBTTagCompound nbt = new NBTTagCompound();
            it.writeToNBT(nbt);
            nbt.setInteger("StackSize", it.stackSize);
            nbttaglist.appendTag(nbt);
        }
        par1NBTTagCompound.setTag("Blocks", nbttaglist);
    }

    private boolean isItemEquals(ItemStack i1, ItemStack i2){
        if(i1==null && i2==null) return true;
        if(i1==null && i2!=null) return false;
        if(i1!=null && i2==null) return false;

        return i1.isItemEqual(i2) && ItemStack.areItemStackTagsEqual(i1, i2);
    }

    public void sendUpdate(){
        //if(worldObj.isRemote) return;

        /*
        for(int i=0;i<worldObj.playerEntities.size();i++){
            EntityPlayer ep=(EntityPlayer)worldObj.playerEntities.get(i);
            if(ep.openContainer instanceof ContainerMealTerminal){
                PacketHandler.INSTANCE.sendTo(new MessageMealTerminal(), (EntityPlayerMP)ep);
            }
        }
        */

        //PacketHandler.INSTANCE.sendToAll(new MessageMealTerminal());
    }

    /**
     * 指定したアイテムをリストに格納する
     * @param itemStack
     */
    public void insertItemStack(ItemStack itemStack){
        if(worldObj.isRemote) return;
        if(itemStack==null) return;

        FBS.logger.info("inserted:"+itemStack.toString());

        //キャッシュを見る
        int size=cache.size();
        for(int i=0;i<size;i++){
            ItemStack t=cache.get(i);
            if(isItemEquals(itemStack, t)){
                //キャッシュにあれば
                t.stackSize+=itemStack.stackSize;

                //キャッシュ更新
                cache.remove(i);
                updateCache(t);

                sendUpdate();
                return;
            }
        }

        //突っ込むリストの決定
        ArrayList<ItemStack> list=null;
        if(itemStack.getItem() instanceof ItemBlock) list=listBlocks;
        else list=listItems;

        //どこに突っ込むか探す
        boolean found=false;
        for(ItemStack it : list){
            if(isItemEquals(it, itemStack)){
                it.stackSize+=itemStack.stackSize;
                found=true;

                //キャッシュの更新
                updateCache(it);
                break;
            }
        }
        if(!found){
            ItemStack a=itemStack.copy();
            list.add(a);

            //キャッシュの更新
            updateCache(a);
        }

        //更新
        markDirty();
        sendUpdate();
    }

    /**
     * 全てのアイテムを得る<br>
     * ただし、ここではItemStackはコピーされないので注意
     * @return
     */
    public ItemStack[] getAllItemStacks(){
        int sizeBlock=listBlocks.size(), sizeItem=listItems.size();
        ItemStack[] ret=new ItemStack[sizeBlock+sizeItem];

        if(ret.length>0){
            int i=0;
            for(;i<sizeBlock;i++) ret[i]=listBlocks.get(i);
            for(int k=0;k<sizeItem;k++) ret[i+k]=listItems.get(k);

            Arrays.sort(ret, new Comparator<ItemStack>() {
                @Override
                public int compare(ItemStack o1, ItemStack o2) {
                    int t=Item.getIdFromItem(o1.getItem())-Item.getIdFromItem(o2.getItem());
                    return t!=0?t:o1.getItemDamage()-o2.getItemDamage();
                }
            });
        }

        return ret;
    }

    /**
     * キャッシュを更新する
     * @param itemStack
     */
    private void updateCache(ItemStack itemStack){
        cache.addFirst(itemStack);
        if(cache.size()>10) cache.removeLast();
    }

    /**
     * リストを走査して、存在しないItemStackが含まれていた場合、それを削除する
     */
    private void refreshList(){
        ArrayList<ItemStack>[] lists=new ArrayList[]{listBlocks, listItems};
        for(ArrayList<ItemStack> list : lists){
            int size=list.size();
            for(int i=0;i<size;){
                ItemStack itemStack=list.get(i);
                if(itemStack==null || itemStack.stackSize<=0){
                    list.remove(i);
                    size--;
                }
                else i++;
            }
        }
    }

    /**
     * 指定したItemStackを取り出す
     * @param req 指定アイテムスタック
     * @param stackSize 取り出すスタックサイズ
     * @return
     */
    public ItemStack exportItemStack(ItemStack req, int stackSize){
        if(worldObj.isRemote) return null;
        if(req==null || req.getItem()==null || stackSize<=0) return null;
        if(req.getMaxStackSize()<stackSize) stackSize=req.getMaxStackSize();

        req.stackSize=stackSize;
        FBS.logger.info("exported:" + req.toString());

        //キャッシュを見る
        int size=cache.size();
        for(int i=0;i<size;i++){
            ItemStack itemStack=cache.get(i);
            if(isItemEquals(itemStack, req)){
                //キャッシュにあれば
                stackSize=Math.min(stackSize, itemStack.stackSize);
                stackSize=Math.min(stackSize, itemStack.getMaxStackSize());
                ItemStack ret=itemStack.copy();
                itemStack.stackSize-=stackSize;
                ret.stackSize=stackSize;

                //キャッシュ更新
                cache.remove(i);

                if(itemStack.stackSize>0) updateCache(itemStack);
                else refreshList();

                sendUpdate();
                return ret;
            }
        }

        //リストを見る
        ArrayList<ItemStack> list=(req.getItem() instanceof ItemBlock)?listBlocks:listItems;
        for(ItemStack itemStack : list){
            if(isItemEquals(itemStack, req)){
                stackSize=Math.min(stackSize, itemStack.stackSize);
                stackSize=Math.min(stackSize, itemStack.getMaxStackSize());
                ItemStack ret=itemStack.copy();
                itemStack.stackSize-=stackSize;
                ret.stackSize=stackSize;

                if(itemStack.stackSize>0) updateCache(itemStack);
                else  refreshList();

                sendUpdate();
                return ret;
            }
        }

        return null;
    }

    public boolean hasFragment(){
        return fragmentTicks>=FRAGEMNT_INTERVAL;
    }
    public ItemStack getFragment(boolean reset){
        if(reset) fragmentTicks=0;
        return ItemMealFragment.getItemStack(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity(){
        if(!worldObj.isRemote){
            if (fragmentTicks < FRAGEMNT_INTERVAL) fragmentTicks++;
            else if (fragmentTicks == FRAGEMNT_INTERVAL) {
                fragmentTicks++;
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }

            if(worldObj.getWorldTime()%20==0){
                AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(xCoord-2, yCoord-1, zCoord-2, xCoord+3, yCoord+3, yCoord+3);
                List list=worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                for(int i=0;i<list.size();i++){
                    EntityPlayer ep=(EntityPlayer)list.get(i);
                    if(!ep.capabilities.isCreativeMode && worldObj.rand.nextInt(16)==0) FBSEntityPropertiesAPI.LoseSanity(ep, 1, 6, true);
                }
            }
        }

        if(!loaded && getBlockMetadata()==0){
            ChunkLoadManager.setChunkLoader(worldObj, xCoord, yCoord, zCoord);
            loaded=true;
        }
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord+1,yCoord+2,zCoord+1);
    }
}
