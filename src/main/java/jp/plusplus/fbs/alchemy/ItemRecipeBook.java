package jp.plusplus.fbs.alchemy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.item.ItemBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.*;

/**
 * Created by plusplus_F on 2015/09/25.
 */
public class ItemRecipeBook extends ItemBase{
    public ItemRecipeBook(){
        setCreativeTab(FBS.tabAlchemy);
        setUnlocalizedName("recipe");
        setTextureName("recipe");
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ArrayList<AlchemyRegistry.Recipe> rbs=new ArrayList<AlchemyRegistry.Recipe>();
        for(AlchemyRegistry.Recipe r : AlchemyRegistry.GetRecieps()){
            if(r.getKey().getItem()==this){
                rbs.add(r);
            }
        }
        Collections.sort(rbs, new Comparator<AlchemyRegistry.Recipe>() {
            @Override
            public int compare(AlchemyRegistry.Recipe o1, AlchemyRegistry.Recipe o2) {
                return o1.level-o2.level;
            }
        });
        for(AlchemyRegistry.Recipe r : rbs){
            list.add(r.getKey().copy());
        }
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean flag) {
        AlchemyRegistry.Recipe r = AlchemyRegistry.GetRecipe(item);
        if (r != null) {
            list.add(r.getProduct().getDisplayName());
            list.add(StatCollector.translateToLocal("info.fbs.book.lv")+":" + r.getLevel());
            list.add("["+StatCollector.translateToLocal("alchemy.fbs.usage")+"]");
            for (String n : r.getMaterialList()) {
                list.add("-"+n);
            }
        }
    }

}
