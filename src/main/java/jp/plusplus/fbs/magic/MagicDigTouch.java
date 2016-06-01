package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3;

/**
 * Created by pluslus_F on 2015/06/19.
 */
public class MagicDigTouch extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(isSpelled) return true;
        float prob=0.4f;
        int l=getLvDiff();
        if(l>0) prob+=0.05*l;
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        int l=getLvDiff();
        Vec3 pos=getTouchPosition();
        if(pos==null) return;

        float hardness=30+1.0f+(l>0?0.5f*l:0);

        Block b=world.getBlock((int)pos.xCoord, (int)pos.yCoord,(int)pos.zCoord);
        float bh=b.getBlockHardness(world, (int)pos.xCoord, (int)pos.yCoord,(int)pos.zCoord);
        if(bh!=-1 && bh<hardness){
            world.func_147480_a((int)pos.xCoord, (int)pos.yCoord,(int)pos.zCoord, true);
        }
    }

    @Override
    public void failure() {
        sanity(1, 4);
    }
}
