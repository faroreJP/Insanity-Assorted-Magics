package jp.plusplus.fbs.magic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;

import java.util.List;

/**
 * Created by plusplus_F on 2015/10/01.
 * ピンチなら
 * 扇いでみよう
 * 大団扇
 */
public class MagicLoveHurricane extends MagicVortex {
    @Override
    public boolean checkSuccess() {
        return true;
    }

    @Override
    public void success() {
        //効果範囲
        int size = isSpelled ? 2 : 1;
        size += Math.max(getLvDiff(), 0) / 5;

        //ダメージ
        float d = 8.0f + 0.5f * (Math.max(getLvDiff(), 0) / 3);

        //吹き飛ばし強さ
        float knockback = 1.f + 0.08f * Math.max(getLvDiff(), 0);

        //効果範囲内の全てのEntityLivingBaseへ
        List list = getEntities(size);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                EntityLivingBase e = (EntityLivingBase) list.get(i);
                if(e.getUniqueID()==player.getUniqueID()) continue;

                e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, e), d);

                //方向の決定
                Vec3 vec = player.getPosition(1.f).subtract(e.getPosition(1.f));
                vec.yCoord = 0;
                vec = vec.normalize();

                //飛ばす
                e.addVelocity(knockback * vec.xCoord, 0, knockback * vec.zCoord);
            }
        }
    }
}
