package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/09/27.
 */
public class MagicBarrier extends MagicBase {
    @Override
    public boolean checkSuccess() {
        float p=0.5f+0.05f*Math.max(getLvDiff(), 0);
        return rand.nextFloat()<p;
    }

    @Override
    public void success() {
        int range=isSpelled?3:1;

        //中心座標の決定
        int px= MathHelper.floor_double(player.posX);
        int py= MathHelper.floor_double(player.posY);
        int pz= MathHelper.floor_double(player.posZ);

        //メタ値の決定
        int meta;
        if(!isSpelled || usingStaff) meta=2+rand.nextInt(3);
        else meta=5+rand.nextInt(2);

        if(this.checkMagicCircle("fbs.barrier")){
            range=1;
            //魔法陣がある場合、全方位に壁を出す
            for(int y=py;y<py+range+1;y++) {
                Block b;
                for (int x = px - range; x < px + range + 1; x++) {
                    b = world.getBlock(x, y, pz + 2);
                    if (b.isReplaceable(world, x, y, pz + 2)) world.setBlock(x, y, pz + 2, BlockCore.barrier, meta, 2);

                    b = world.getBlock(x, y, pz - 2);
                    if (b.isReplaceable(world, x, y, pz - 2)) world.setBlock(x, y, pz - 2, BlockCore.barrier, meta, 2);
                }
                for (int z = pz - range; z < pz + range + 1; z++) {
                    b = world.getBlock(px + 2, y, z);
                    if (b.isReplaceable(world, px + 2, y, z)) world.setBlock(px + 2, y, z, BlockCore.barrier, meta, 2);

                    b = world.getBlock(px - 2, y, z);
                    if (b.isReplaceable(world, px - 2, y, z)) world.setBlock(px - 2, y, z, BlockCore.barrier, meta, 2);
                }
            }
        }
        else{
            //魔法陣がない場合、前方にのみ壁を出す
            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            if(l==0){
                pz+=2;
                for(int x=px-range;x<px+range+1;x++){
                    for(int y=py;y<py+range+1;y++){
                        Block b=world.getBlock(x,y,pz);
                        if(b.isReplaceable(world, x,y,pz)) world.setBlock(x, y, pz, BlockCore.barrier, meta, 2);
                    }
                }
            }
            else if(l==1){
                px-=2;
                for(int z=pz-range;z<pz+range+1;z++){
                    for(int y=py;y<py+range+1;y++){
                        Block b=world.getBlock(px,y,z);
                        if(b.isReplaceable(world, px,y,z)) world.setBlock(px, y, z, BlockCore.barrier, meta, 2);
                    }
                }
            }
            else if(l==2){
                pz-=2;
                for(int x=px-range;x<px+range+1;x++){
                    for(int y=py;y<py+range+1;y++){
                        Block b=world.getBlock(x,y,pz);
                        if(b.isReplaceable(world, x,y,pz)) world.setBlock(x, y, pz, BlockCore.barrier, meta, 2);
                    }
                }
            }
            else if(l==3){
                px+=2;
                for(int z=pz-range;z<pz+range+1;z++){
                    for(int y=py;y<py+range+1;y++){
                        Block b=world.getBlock(px,y,z);
                        if(b.isReplaceable(world, px,y,z)) world.setBlock(px, y, z, BlockCore.barrier, meta, 2);
                    }
                }
            }
        }
    }

    @Override
    public void failure() {
        sanity(2, 4);
    }
}
