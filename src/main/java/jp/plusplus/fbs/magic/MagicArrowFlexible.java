package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.entity.EntityMagicArrowFlexible;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemStaff;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;

/**
 * Created by pluslus_F on 2015/06/23.
 *
 * 魔法の矢+付与魔法の汎用共鳴
 */
public class MagicArrowFlexible extends MagicBase {
    @Override
    public boolean checkSuccess() {
        return true;
    }

    @Override
    public void success() {
        ItemStack item=player.getCurrentEquippedItem();
        if(item==null || !(item.getItem() instanceof ItemStaff)) return;

        int bookNum=((ItemStaff) item.getItem()).bookNum;
        ItemStack[] items=ItemStaff.loadInventory(item);

        //付与魔法のリストを作成する
        LinkedList<Registry.MagicData> magics = new LinkedList<Registry.MagicData>();
        for (int i = 0; i < bookNum; i++) {
            if (items[i] != null && items[i].getItem() == ItemCore.bookSorcery) {
                Registry.MagicData md=Registry.GetMagicDataFromItemStack(items[i]);
                if(md==null) continue;
                if(MagicArrow.class==md.magic) continue;

                //付与魔法であればリストに追加
                if(IMagicEnchant.class.isAssignableFrom(md.magic)){
                    magics.add(md);
                }
            }
        }
        if (magics.isEmpty()) return;


        //エンティティの生成
        int d=getLvDiff();
        float dm=isSpelled?2.0f:1.0f;
        if(d>0) dm+=0.5f*(d/5);

        Entity e=new EntityMagicArrowFlexible(world, player, 1.0F, 1.0F, dm, magics.toArray(new Registry.MagicData[magics.size()]));
        world.spawnEntityInWorld(e);
    }

    @Override
    public void failure() {
        sanity(2,4);
    }
}
