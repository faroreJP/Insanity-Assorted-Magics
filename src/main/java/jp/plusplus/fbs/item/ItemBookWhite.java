package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by plusplus_F on 2015/08/25.
 */
public class ItemBookWhite extends ItemBase {
    protected Random rand=new Random();

    public ItemBookWhite(){
        setCreativeTab(FBS.tabBook);
        setUnlocalizedName("bookWhite");
        setTextureName("bookWhite");
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if(p_77659_2_.isRemote) return p_77659_1_;

        ArrayList<Registry.BookData> list=new ArrayList<Registry.BookData>();
        Map.Entry<String, Registry.BookData>[] books=Registry.GetBooks();

        //魔導書の抽出
        for(Map.Entry<String, Registry.BookData> bd : books){
            if(!bd.getValue().isMagic) continue;

            Registry.MagicData md=Registry.GetMagic(bd.getValue().title);
            if(md.isResonance) continue;

            list.add(bd.getValue());
        }

        //ランダムに変化
        int r=rand.nextInt(list.size());
        return Registry.GetBookItemStack(list.get(r).title);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.bookWhite.0"));
    }
}
