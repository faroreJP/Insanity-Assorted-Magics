package jp.plusplus.fbs.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.*;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class ItemBookNoDecoded extends ItemBase {
    private IIcon iconOverlay;

    public ItemBookNoDecoded(){
        setCreativeTab(FBS.tabBook);
        setMaxStackSize(1);
        setUnlocalizedName("bookNDec");
        setTextureName("bookNoDecoded");
        setHasSubtypes(true);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_){
        return 20*6;
    }
    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return FBS.actionDecode;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        /*
        ItemStack helm=p_77659_3_.getCurrentArmor(3);
        if(helm!=null && helm.getItem() instanceof ItemMonocle) {
            p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        }
        */
        if(ItemMonocle.findMonocle(p_77659_3_)!=null) p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if(!itemStack.hasTagCompound()) return itemStack;

        ItemStack monocle=ItemMonocle.findMonocle(player);
        if(monocle==null) return itemStack;

        if(!player.capabilities.isCreativeMode){
            ItemMonocle.damageMonocle(player, monocle);
            player.inventory.markDirty();
        }

        if(world.isRemote) return itemStack;

        NBTTagCompound nbt=itemStack.getTagCompound();
        boolean result=Registry.TryDecodingBook(nbt.getString("title"), player, true);
        if(result){
            NBTTagCompound nbtNew=(NBTTagCompound)nbt.copy();
            nbtNew.setString("decoder", player.getDisplayName());

            ItemStack ret=null;
            String t=nbt.getString("title");
            if(Registry.GetBook(t).isMagic) {
                ret=new ItemStack(ItemCore.bookSorcery, 1, itemStack.getItemDamage());
                ret.setTagCompound(nbtNew);
                ((ItemBookSorcery)ItemCore.bookSorcery).setMagicMaxUse(ret, Registry.GetMagic(t));
            }
            else{
                ret=new ItemStack(ItemCore.bookOld, 1, itemStack.getItemDamage());
                ret.setTagCompound(nbtNew);
            }

            return ret;
        }

        //p_77654_2_.playSoundAtEntity(p_77654_3_, "random.burp", 0.5F, p_77654_2_.rand.nextFloat() * 0.1F + 0.9F);
        return itemStack;
    }
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        Map.Entry<String, Registry.BookData>[] books=Registry.GetBooks();
        Arrays.sort(books, new Comparator<Map.Entry<String, Registry.BookData>>() {
            @Override
            public int compare(Map.Entry<String, Registry.BookData> o1, Map.Entry<String, Registry.BookData> o2) {
                return o1.getValue().lv-o2.getValue().lv;
            }
        });

        for(int i=0;i<books.length;i++){
            if(books[i].getValue().weight<=0) continue;

            ItemStack itemStack = new ItemStack(this, 1, itemRand.nextInt(0xfff+1));
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("title", books[i].getValue().title);
            itemStack.setTagCompound(nbt);//ItemStackにNBTTagCompoundを格納
            itemList.add(itemStack);//クリエイティブタブのアイテムリストに追加
        }
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        Registry.BookData bd=Registry.GetBookDataFromItemStack(itemStack);
        if(bd==null) return;

        /*
        ItemStack helm=player.getCurrentArmor(3);
        if(helm!=null && helm.getItem() instanceof ItemMonocle){
            */
        if(ItemMonocle.findMonocle(player)!=null){
            FBSEntityProperties prop=FBSEntityProperties.get(player);
            if(prop==null) return;

            String d;
            float prob=Registry.GetDecodingProbability(prop.getMagicLevel(), bd.lv, bd.scProb);
            if(prob<0.3f) d=ChatFormatting.RED+ I18n.format("info.fbs.book.high");
            else if(prob<0.8) d=ChatFormatting.YELLOW+I18n.format("info.fbs.book.middle");
            else d=ChatFormatting.DARK_GREEN+I18n.format("info.fbs.book.low");

            list.add(I18n.format("info.fbs.book.lv")+":"+bd.lv);
            list.add(I18n.format("info.fbs.book.difficult")+":"+d);
        }
        else{
            list.add(I18n.format("info.fbs.book.lv")+":???");
            list.add(I18n.format("info.fbs.book.difficult")+":???");
        }
    }

    public String getLocalizedBookTitle(ItemStack item){
        NBTTagCompound nbt=item.getTagCompound();
        if(nbt==null) return "";
        return StatCollector.translateToLocal("book."+nbt.getString("title")+".title");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1){
        return super.getIconFromDamage(par1);
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }
    @Override
    public int getSpriteNumber(){
        return 1;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        iconOverlay=par1IconRegister.registerIcon(FBS.MODID+":book");
    }
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses(){
        return true;
    }
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2){
        return par2 > 0 ? iconOverlay : itemIcon;
    }
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        if(par2==0) return 0xffffff;
        int d=par1ItemStack.getItemDamage();
        int r=(d>>8)&0xf, g=(d>>4)&0xf, b=d&0xf;
        return ~(((r*17)<<16)+((g*17)<<8)+(b*17));
    }

    @Override
    public String getItemStackDisplayName(ItemStack item){
        String s=getLocalizedBookTitle(item);
        if(s.length()==0) return super.getItemStackDisplayName(item);
        return s;
    }
}
