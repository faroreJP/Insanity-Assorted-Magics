package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.entity.EntityMagicArrow;
import net.minecraft.entity.Entity;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class MagicArrow extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(isSpelled) return true;

        float prob=0.4f+0.03f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        int d=getLvDiff();
        float dm=isSpelled?2.0f:1.0f;
        if(d>0) dm+=0.5f*(d/5);

        Entity e=new EntityMagicArrow(world, player, 1.0F, 1.0F, dm);
        world.spawnEntityInWorld(e);
    }

    @Override
    public void failure() {
        sanity(1, 6);
    }
}
