package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.entity.EntityMagicHealingBall;
import net.minecraft.entity.Entity;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class MagicHealingBall extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(isSpelled) return true;

        float prob=0.4f+0.02f*getLvDiff();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        int a = 5+getLvDiff()/4;
        Entity e=new EntityMagicHealingBall(world, player, a);
        world.spawnEntityInWorld(e);
    }

    @Override
    public void failure() {
        sanity(2, 6);
    }
}
