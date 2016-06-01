package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.entity.EntityMagicDig;

/**
 * Createdby pluslus_Fon 2015/06/07.
 */
public class MagicDig extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(isSpelled) return true;

        float prob=0.4f+0.03f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        boolean penetrate=(isSpelled && property.getMagicLevel()>=20);
        int till=getLvDiff();
        EntityMagicDig e=new EntityMagicDig(world, player, 0.6F, 1.0F, till, penetrate);
        world.spawnEntityInWorld(e);
    }

    @Override
    public void failure() {
        sanity(1, 6);
    }
}
