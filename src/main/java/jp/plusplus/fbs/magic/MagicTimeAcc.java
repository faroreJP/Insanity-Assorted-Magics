package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class MagicTimeAcc extends MagicBase {
    @Override
    public boolean checkSuccess() {
        float prob=0.5f+0.025f*(property.getMagicLevel()-bookData.lv);
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        int range=isSpelled?2:1;
        int l=getLvDiff();
        if(l>0) range+=l/4;

        int cx=(int)player.posX, cy=(int)player.posY, cz=(int)player.posZ;
        for(int i=cx-range;i<cx+range;i++){
            for(int k=cz-range;k<cz+range;k++){
                for(int n=cy;n<cy+1;n++){
                    Block b=world.getBlock(i,n,k);
                    if(b instanceof IGrowable){
                        IGrowable g=(IGrowable)b;

                        if (g.func_149851_a(world, i, n, k, world.isRemote)){
                            if (g.func_149852_a(world, rand, i, n, k)){
                                ((IGrowable) b).func_149853_b(world, rand, i, n, k);
                                world.playAuxSFX(2005, i, n, k, 0);
                            }
                        }
                    }
                }
            }
        }

        sanity(1,10);
    }

    @Override
    public void failure() {
        sanity(1,10);
    }
}
