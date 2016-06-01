package jp.plusplus.fbs.event.wish;

import jp.plusplus.fbs.event.wish.entry.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class WishHandler {
    private static WishHandler instance=new WishHandler();
    private ArrayList<IWishEntry> wishEntries=new ArrayList<IWishEntry>();
    private boolean isSorted=false;

    private WishHandler(){}

    public static void register(){
        addWishEntry(new WishEntryGeneric());

        addWishEntry(new WishEntryText("panty"));
        addWishEntry(new WishEntryText("herobrine"));
        addWishEntry(new WishEntryText("wish"));
        addWishEntry(new WishEntryText("goddess"));
        addWishEntry(new WishEntryText("achievement"));
        addWishEntry(new WishEntryText("insanity"));

        addWishEntry(new WishEntryMoney());
        addWishEntry(new WishEntryDeath());
        addWishEntry(new WishEntryExp());
        addWishEntry(new WishEntryHealth());
        addWishEntry(new WishEntrySanity());
    }

    public static void addWishEntry(IWishEntry entry){
        instance.wishEntries.add(entry);
        instance.isSorted=false;
    }

    public static void handleWish(EntityPlayer player, String wish){
        if(!instance.isSorted){
            instance.isSorted=true;
            Collections.sort(instance.wishEntries, new Comparator<IWishEntry>() {
                @Override
                public int compare(IWishEntry o1, IWishEntry o2) {
                    float p=o2.priority()-o1.priority();
                    return p>0?1:(p<0?-1:0);
                }
            });
        }

        for(IWishEntry entry : instance.wishEntries){
            if(entry.matches(wish)){
                ItemStack itemStack=entry.get(player, wish);
                if(itemStack!=null){
                    EntityItem entityItem=player.entityDropItem(itemStack, player.getEyeHeight());
                    if(entityItem!=null){
                        entityItem.delayBeforeCanPickup=0;
                    }
                }
                return;
            }
        }
    }

    public static String getGetMessage(String key){
        return "<"+ StatCollector.translateToLocal("wish.fbs.goddess")+">"+StatCollector.translateToLocal("wish.fbs."+key+".get");
    }
}
