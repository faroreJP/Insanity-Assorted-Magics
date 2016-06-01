package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemStaff;
import jp.plusplus.fbs.packet.MessageMagicVortex;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by plusplus_F on 2015/10/01.
 */
public class MagicVortexFlexible extends MagicVortex {
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
        NBTTagList tagList=new NBTTagList();
        LinkedList<Registry.MagicData> magics = new LinkedList<Registry.MagicData>();
        for (int i = 0; i < bookNum; i++) {
            if (items[i] != null && items[i].getItem() == ItemCore.bookSorcery) {
                Registry.MagicData md=Registry.GetMagicDataFromItemStack(items[i]);
                if(md==null) continue;
                if(MagicArrow.class==md.magic) continue;

                //付与魔法であればリストに追加
                if(IMagicEnchant.class.isAssignableFrom(md.magic)){
                    magics.add(md);

                    NBTTagCompound nbt1 = new NBTTagCompound();
                    nbt1.setString("MagicName", md.title);
                    tagList.appendTag(nbt1);
                }
            }
        }
        if (magics.isEmpty()) return;

        //効果範囲
        int size=isSpelled?2:1;
        size+=Math.max(getLvDiff(), 0)/5;

        //ダメージ
        float d=3.0f+0.5f*(Math.max(getLvDiff(), 0)/3);
        if(!isSpelled) d/=2.f;

        //効果範囲内の全てのEntityLivingBaseへ
        List list=getEntities(size);
        if(!list.isEmpty()){

            //付与魔法のインスタンス化
            IMagicEnchant[] me=new IMagicEnchant[magics.size()];
            for(int i=0;i<me.length;i++) {
                me[i] = (IMagicEnchant) magics.get(i).getMagic(world, player, false);
            }

            for(int i=0;i<list.size();i++){
                EntityLivingBase e=(EntityLivingBase)list.get(i);
                if(e.getUniqueID()==player.getUniqueID()) continue;

                //一体ずつダメージとか処理していく
                float dScale=1.f;
                float dValue=0.f;
                for(int k=0;k<me.length;k++){
                    dScale*=me[k].damageScale(e);
                    dValue+=me[k].damageValue(e);
                }

                e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, e), (d+dValue)*dScale);

                //付与効果
                for(int k=0;k<me.length;k++){
                    me[k].enchant(e, true);
                }
            }
        }

        //エフェクト
        NBTTagCompound tag=new NBTTagCompound();
        tag.setTag("EnchantMagics", tagList);
        PacketHandler.INSTANCE.sendToDimension(new MessageMagicVortex(player, size, tag), world.provider.dimensionId);
    }
}
