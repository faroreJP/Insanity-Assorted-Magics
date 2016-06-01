package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemStaff;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import java.util.LinkedList;

/**
 * Created by pluslus_F on 2015/06/23.
 * 魔法の拳+付与魔法の汎用共鳴
 */
public class MagicTouchFlexible extends MagicBase {
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

        Entity e=getTouchEntity();
        if(e instanceof EntityLivingBase){
            float d=3.0f;
            int lv=getLvDiff();
            if(lv>0) d+=0.5*(lv/5);
            if(!isSpelled) d/=2.0f;

            float dValue=0.f;
            float dScale=1.f;

            //IMagicEnchantのインスタンス化
            //スタッフによる使用なので常に詠唱破棄状態
            IMagicEnchant[] me=new IMagicEnchant[magics.size()];
            for(int i=0;i<me.length;i++){
                me[i]=(IMagicEnchant)magics.get(i).getMagic(world, player, false);
                dValue+=me[i].damageValue((EntityLivingBase)e);
                dScale*=me[i].damageScale((EntityLivingBase)e);
            }

            //ダメージ
            e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, (EntityLivingBase) e), (d+dValue)*dScale);

            //タッチしたEntityへの付与
            for(int i=0;i<me.length;i++){
                me[i].enchant((EntityLivingBase)e, true);
            }
        }
    }

    @Override
    public void failure() {
        sanity(2,4);
    }
}
