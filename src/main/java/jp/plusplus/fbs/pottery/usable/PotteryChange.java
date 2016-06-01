package jp.plusplus.fbs.pottery.usable;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotteryChange extends PotteryKeep {

    private static Integer[] itemIds;

    private Random rand=new Random();

    @Override
    public String getUnlocalizedName() {
        return "pottery.fbs.pot.change";
    }

    @Override
    public float getPriceScale(ItemStack pottery){
        return 5.0f;
    }

    @Override
    public ItemStack onInventoryClosing(EntityPlayer player, ItemStack pottery, int index, @Nullable ItemStack itemStack){
        if(itemStack!=null && !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index)){
            if(itemIds==null){
                Map<String,Integer> idMapping = Maps.newHashMap();
                GameData.itemRegistry.serializeInto(idMapping);
                itemIds=new Integer[idMapping.size()];
                idMapping.values().toArray(itemIds);
            }

            for(int i=0;i<10;i++){
                Item item=Item.getItemById(rand.nextInt(itemIds.length));
                if(item==null) continue;

                if(item instanceof ItemBlock){
                    Block b=((ItemBlock) item).field_150939_a;
                    Item d=b.getItemDropped(0, rand, 0);
                    if(d==null || d==Item.getItemById(0) || b.quantityDropped(0, 0, rand)==0){
                        continue;
                    }
                }

                ArrayList<ItemStack> list=new ArrayList<ItemStack>();
                item.getSubItems(item, item.getCreativeTab(), list);

                if(!list.isEmpty()){
                    ItemStack ret=list.get(rand.nextInt(list.size()));
                    ret.stackSize=Math.min(ret.getMaxStackSize(), itemStack.stackSize);
                    return ret;
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player, ItemStack pottery, int index, ItemStack itemStack){
        return !pottery.getTagCompound().getBoolean(CHANGED_INDEXES+index);
    }
}
