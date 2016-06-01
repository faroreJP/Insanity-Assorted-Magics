package jp.plusplus.fbs.exprop;

import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.ArrayList;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class FBSEntityProperties implements IExtendedEntityProperties {
    public final static String EXT_PROP_NAME = "FBSPlayerData";

    /**
     * 魔術レベルの最大値
     */
    public static final int MAGIC_LEVEL_MAX=50;

    /**
     * 一般人の魔術レベル最大値
     */
    public static final int MAGIC_LEVEL_BASIC_MAX=25;

    private int san=99;
    private int lvMagic=1;
    private double exp=0;
    private double next=30;

    private String spiritToolName="";
    private int spiritToolLv=-1;

    private ArrayList<WarpPosition> positions=new ArrayList<WarpPosition>();
    private ArrayList<String> decodedBooks=new ArrayList<String>();


    private ItemStack[] bindInventory=new ItemStack[40];
    private ItemStack[] pocketInventory=new ItemStack[72];

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new FBSEntityProperties());
    }
    public static FBSEntityProperties get(EntityPlayer player) {
        return (FBSEntityProperties)player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setInteger("SAN", san);
        nbt.setInteger("LvMagic", lvMagic);
        nbt.setDouble("EXP", exp);
        nbt.setDouble("Next", next);
        nbt.setString("SpiritToolName", spiritToolName);
        nbt.setInteger("SpiritToolLevel", spiritToolLv);

        NBTTagList tags=new NBTTagList();
        for(WarpPosition pos : positions){
            NBTTagCompound n=new NBTTagCompound();
            pos.writeToNBT(n);
            tags.appendTag(n);
        }
        nbt.setTag("WarpPositions", tags);

        tags=new NBTTagList();
        for (int i=0;i<bindInventory.length;i++){
            if (bindInventory[i] != null){
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte("Slot", (byte) i);
                bindInventory[i].writeToNBT(nbt1);
                tags.appendTag(nbt1);
            }
        }
        nbt.setTag("BindInventory", tags);

        tags=new NBTTagList();
        for (int i=0;i<pocketInventory.length;i++){
            if (pocketInventory[i] != null){
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setByte("Slot", (byte) i);
                pocketInventory[i].writeToNBT(nbt1);
                tags.appendTag(nbt1);
            }
        }
        nbt.setTag("PocketInventory", tags);

        tags=new NBTTagList();
        for(String s : decodedBooks){
            NBTTagCompound nbt1=new NBTTagCompound();
            nbt1.setString("Name", s);
            tags.appendTag(nbt1);
        }
        nbt.setTag("DecodedBooks", tags);


        compound.setTag(EXT_PROP_NAME, nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt=compound.getCompoundTag(EXT_PROP_NAME);
        san=nbt.getInteger("SAN");
        lvMagic=nbt.getInteger("LvMagic");
        exp=nbt.getDouble("EXP");
        next=nbt.getDouble("Next");
        spiritToolName=nbt.getString("SpiritToolName");
        spiritToolLv=nbt.getInteger("SpiritToolLevel");

        positions.clear();
        if(nbt.hasKey("WarpPositions")){
            NBTTagList tags=(NBTTagList)nbt.getTag("WarpPositions");
            for(int i=0;i<tags.tagCount();i++){
                positions.add(new WarpPosition(tags.getCompoundTagAt(i)));
            }
        }

        NBTTagList nbttaglist = (NBTTagList)nbt.getTag("BindInventory");
        bindInventory = new ItemStack[bindInventory.length];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt1.getByte("Slot");

            if (b0>=0 && b0<bindInventory.length){
                bindInventory[b0] = ItemStack.loadItemStackFromNBT(nbt1);
            }
        }

        nbttaglist = (NBTTagList)nbt.getTag("PocketInventory");
        pocketInventory = new ItemStack[72];
        for (int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbt1.getByte("Slot");

            if (b0>=0 && b0<pocketInventory.length){
                pocketInventory[b0] = ItemStack.loadItemStackFromNBT(nbt1);
            }
        }

        if(nbt.hasKey("DecodedBooks")){
            decodedBooks=new ArrayList<String>();
            nbttaglist=(NBTTagList)nbt.getTag("DecodedBooks");
            for(int i=0;i<nbttaglist.tagCount();i++){
                NBTTagCompound nbt1=nbttaglist.getCompoundTagAt(i);
                decodedBooks.add(nbt1.getString("Name"));
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {}

    public int getMagicLevelMax(){
        return MAGIC_LEVEL_BASIC_MAX+(getSpiritToolLevel()+1)/2;
    }

    public int getMaxSanity(){
        int m=100-lvMagic-(spiritToolLv/2);
        return m<100?(m>5?m:5):99;
    }
    public int getSanity(){ return san; }
    public void setSanity(int s){
        san=s;
        if(san>getMaxSanity()){
            san=getMaxSanity();
        }
        if(san<0){
            san=0;
        }
    }
    public int getMagicLevel(){ return lvMagic; }
    public void setMagicLevel(int s){
        if(s<1) s=1;

        lvMagic=s;
        exp=0;
        if(lvMagic==1) next=10;
        else next=MathHelper.ceiling_double_int(10+Math.exp(0.25*(lvMagic-1)));
    }
    public double getEXP(){
        return exp;
    }
    public double getNext(){
        return next;
    }
    public void addEXP(double s){
        if(s<=0) return;
        exp+=s;
        while(exp>=next && lvMagic<getMagicLevelMax()){
            LvUp();
        }
    }
    public void LvUp(){
        lvMagic++;
        if(san>getMaxSanity()){
            san=getMaxSanity();
        }
        exp-=next;
        //next=MathHelper.ceiling_double_int(10+Math.exp(0.25*(lvMagic-1)));
        next+=15*(lvMagic+1);
    }

    public String getSpiritToolName(){ return spiritToolName; }
    public void setSpiritToolName(String s){ spiritToolName=s; }
    public int getSpiritToolLevel(){ return spiritToolLv; }
    public void setSpiritToolLevel(int l){
        if(l<=0 || l>SpiritStatus.LEVEL_MAX) return;
        spiritToolLv =l;
    }

    public void bindPlayerInventory(EntityPlayer player, boolean spiritOnly){
        bindInventory=new ItemStack[player.inventory.getSizeInventory()];

        if(spiritOnly && SpiritManager.findSpiritToolIndex(player)!=-1){
            for(int i=0;i<player.inventory.getSizeInventory();i++){
                ItemStack is=player.inventory.getStackInSlot(i);
                if(is!=null){
                    if(spiritOnly){
                        if(is.getItem() instanceof ISpiritTool){
                            bindInventory[i]=is.copy();
                        }
                    }
                    else{
                        bindInventory[i]=is.copy();
                    }
                }
            }
        }
    }
    public void copyFromBindInventory(EntityPlayer player){
        int size=player.inventory.getSizeInventory();
        for(int i=0;i<size && i<bindInventory.length;i++){
            if(bindInventory[i]!=null){
                if(player.inventory.getStackInSlot(i)==null){
                    player.inventory.setInventorySlotContents(i, bindInventory[i]);
                }
                else{
                    player.inventory.addItemStackToInventory(bindInventory[i]);
                }
            }
            bindInventory[i]=null;
        }
    }

    public void addDecodedBook(String name){
        decodedBooks.add(name);
    }
    public boolean isDecoded(String name){
        return decodedBooks.contains(name);
    }
    public ArrayList<String> getDecodedBooks(){
        return decodedBooks;
    }

    public ArrayList<WarpPosition> getDestinations(){
        return positions;
    }
    public boolean addDestination(int dimId, int x, int y, int z){
        WarpPosition wp=new WarpPosition(dimId, x, y, z);
        if(!positions.contains(wp)){
            positions.add(wp);
            return true;
        }
        return false;
    }

    public static class WarpPosition{
        public String name;
        public int dimId;
        public int x,y,z;

        public WarpPosition(NBTTagCompound nbt){
            readFromNBT(nbt);
        }
        public WarpPosition(int dimId, int x, int y, int z){
            this.dimId=dimId;
            this.x=x;
            this.y=y;
            this.z=z;
            name=getDimName()+"("+x+","+y+","+z+")";
        }

        public void setName(String n){
            name=n;
        }
        public String getName(){ return name; }

        public boolean canWarp(){
            World w=DimensionManager.getWorld(dimId);
            if(w==null) return false;

            Block b=w.getBlock(x,y,z);
            if(b!=BlockCore.magicCore) return false;

            TileEntity te=w.getTileEntity(x,y,z);
            if(!(te instanceof TileEntityMagicCore)) return false;

            return "fbs.warp".equals(((TileEntityMagicCore) te).getCircleName());
        }

        public String getDimName(){
            WorldProvider wp=WorldProvider.getProviderForDimension(dimId);
            if(wp==null) return "";
            return wp.getDimensionName();
        }

        public void writeToNBT(NBTTagCompound nbt){
            nbt.setString("Name", name);
            nbt.setInteger("Dim", dimId);
            nbt.setInteger("x", x);
            nbt.setInteger("y", y);
            nbt.setInteger("z", z);
        }
        public void readFromNBT(NBTTagCompound nbt){
            name=nbt.getString("Name");
            dimId=nbt.getInteger("Dim");
            x=nbt.getInteger("x");
            y=nbt.getInteger("y");
            z=nbt.getInteger("z");
        }
        @Override
        public boolean equals(Object obj){
            if(!(obj instanceof WarpPosition)) return false;
            WarpPosition wp=(WarpPosition)obj;
            return dimId==wp.dimId && x==wp.x && y==wp.y && z==wp.z;
        }
    }
}
