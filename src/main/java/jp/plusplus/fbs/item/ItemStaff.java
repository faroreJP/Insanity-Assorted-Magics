package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IResonance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/15.
 * 闇の実装
 */
public class ItemStaff extends ItemBase {
    public int gemNum;
    public int bookNum=1;
    public ItemStaff(int bookNum, int gem) {
        this.bookNum = bookNum;
        gemNum = gem;
        setMaxStackSize(1);
        setUnlocalizedName("staff" + bookNum + "_" + gem);
        setTextureName("staff" + bookNum + "_" + gem);
        setFull3D();
        setMaxDamage(59);
        setNoRepair();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.isSneaking()) openGUI(itemStack, world, player);
        else{
            ItemStack[] items=loadInventory(itemStack);
            String name=getStaffMagicName(itemStack);
            if(name.equals("null")) openGUI(itemStack, world, player);
            else{
                if(getStaffMagicMaxUse(itemStack)>0) {
                    execute(itemStack, items, world, player);
                    saveInventory(itemStack, items);
                }
                itemStack.damageItem(1, player);
                player.swingItem();
            }
        }
        return itemStack;
    }

    public static ItemStack[] loadInventory(ItemStack itemStack){
        ItemStaff staff=(ItemStaff)itemStack.getItem();
        ItemStack[] items=new ItemStack[staff.gemNum+staff.bookNum];

        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        if(!itemStack.getTagCompound().hasKey("Items")){
            itemStack.getTagCompound().setTag("Items", new NBTTagList());
        }
        NBTTagList tags = (NBTTagList) itemStack.getTagCompound().getTag("Items");

        for (int i = 0; i < tags.tagCount(); i++) {
            NBTTagCompound tagCompound = tags.getCompoundTagAt(i);
            int slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < items.length) {
                items[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        return items;
    }
    public static void saveInventory(ItemStack itemStack, ItemStack[] inv){
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setByte("Slot", (byte) i);
                inv[i].writeToNBT(compound);
                tagList.appendTag(compound);
            }
        }
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setTag("Items", tagList);

        //resonance
        int minUse=9999;
        LinkedList<ItemStack> books=new LinkedList<ItemStack>();
        for(int i=0;i<((ItemStaff)itemStack.getItem()).bookNum;i++){
            if (inv[i] != null && inv[i].getItem() == ItemCore.bookSorcery) {
                books.add(inv[i]);
                int u=ItemBookSorcery.getMagicMaxUse(inv[i]);
                if(u<minUse) minUse=u;
            }
        }

        Registry.MagicData md=Registry.GetResonanceMagicData(books);
        if(md==null) itemStack.getTagCompound().setString("MagicName", "null");
        else itemStack.getTagCompound().setString("MagicName", md.title);
        itemStack.getTagCompound().setInteger("MagicMinUse", minUse);
    }

    public static String getStaffMagicName(ItemStack item){
        if(!item.hasTagCompound()) return "null";
        NBTTagCompound nbt=item.getTagCompound();
        return nbt.hasKey("MagicName")?nbt.getString("MagicName"):"null";
    }
    public static int getStaffMagicMaxUse(ItemStack item){
        if(!item.hasTagCompound()) return 0;
        NBTTagCompound nbt=item.getTagCompound();
        return nbt.hasKey("MagicMinUse")?nbt.getInteger("MagicMinUse"):0;
    }

    @Override
    public String getItemStackDisplayName(ItemStack item){
        if(!item.hasTagCompound()) return super.getItemStackDisplayName(item);

        String n=getStaffMagicName(item);
        if(n.length()==0 || n.equals("null")) return super.getItemStackDisplayName(item);

        String ln="";
        IResonance r=Registry.GetResonance(n);
        if(r!=null){
            ItemStack[] items=loadInventory(item);
            String[] tt=new String[bookNum];
            for(int i=0;i<bookNum;i++){
                if(items==null) continue;
                tt[i]=Registry.GetUnlocalizedBookTitleFromItemStack(items[i]);
            }
            ln=r.getDisplayMagicName(tt);
        }
        else ln=Registry.GetMagic(n).getLocalizedTitle();

        return super.getItemStackDisplayName(item)+"("+ln+"["+getStaffMagicMaxUse(item)+"]"+")";
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        list.add(String.format(StatCollector.translateToLocal("info.fbs.staff.0"), bookNum));
        list.add(String.format(StatCollector.translateToLocal("info.fbs.staff.1"), gemNum));
    }

    public void openGUI(ItemStack itemStack, World world, EntityPlayer player){
        player.openGui(FBS.instance, FBS.GUI_STAFF_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
    }
    public void execute(ItemStack itemStack, ItemStack[] items, World world, EntityPlayer player){
        ((ItemBookSorcery) ItemCore.bookSorcery).executesMagicWithStaff(world, player, items, ((ItemStaff)itemStack.getItem()).bookNum);
    }
}
