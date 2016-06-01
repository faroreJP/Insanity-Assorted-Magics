package jp.plusplus.fbs.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class ItemOldBook extends ItemBookNoDecoded {
    public ItemOldBook(){
        setHasSubtypes(true);
        setCreativeTab(null);
        setUnlocalizedName("bookOld");
        setTextureName("bookNoDecoded");
        setMaxStackSize(1);
    }
    @Override
    public boolean hasEffect(ItemStack itemStack, int i){
        return true;
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        Registry.BookData bd = Registry.GetBookDataFromItemStack(itemStack);
        if (bd == null) return;

        //list.add("\"" + bd.getLocalizedTitle() + "\"");
        list.add(I18n.format("info.fbs.book.decoder")+":"+itemStack.getTagCompound().getString("decoder"));
        list.add(I18n.format("info.fbs.book.lv") + ":" + bd.lv);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.none;
    }
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player){
        return itemStack;
    }
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_){
        return p_77659_1_;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        itemList.add(new ItemStack(this, 1, itemRand.nextInt(0xffffff+1)));
    }

    @Override
    public String getItemStackDisplayName(ItemStack item){
        return super.getItemStackDisplayName(item)+"("+StatCollector.translateToLocal("info.fbs.book.decoded")+")";
    }
}
