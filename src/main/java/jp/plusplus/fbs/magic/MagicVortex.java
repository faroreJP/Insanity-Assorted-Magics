package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.packet.MessageMagicFlexible;
import jp.plusplus.fbs.packet.MessageMagicVortex;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plusplus_F on 2015/10/01.
 */
public class MagicVortex extends MagicBase {
    @Override
    public boolean checkSuccess() {
        float p=0.35f+0.05f*Math.max(getLvDiff(), 0);
        if(isSpelled) p+=0.35f;
        return rand.nextFloat()<p;
    }

    @Override
    public void success() {
        //効果範囲
        int size=isSpelled?2:1;
        size+=Math.max(getLvDiff(), 0)/5;

        //ダメージ
        float d=3.0f+0.5f*(Math.max(getLvDiff(), 0)/3);
        if(!isSpelled) d/=2.f;

        //効果範囲内の全てのEntityLivingBaseへ
        List list=getEntities(size);
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                EntityLivingBase e=(EntityLivingBase)list.get(i);
                if(e.getUniqueID()==player.getUniqueID()) continue;

                e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, e), d);
            }
        }

        //エフェクト
        PacketHandler.INSTANCE.sendToDimension(new MessageMagicVortex(player, size, new NBTTagCompound()), world.provider.dimensionId);
    }

    @Override
    public void failure() {
        sanity(2, 4);
    }

    public List getEntities(int size){
        int px= MathHelper.floor_double(player.posX);
        int py= MathHelper.floor_double(player.posY);
        int pz= MathHelper.floor_double(player.posZ);

        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(px,py,pz, px+1, py+2, pz+1).expand(size, size, size);
        return world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
    }
}
