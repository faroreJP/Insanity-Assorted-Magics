package jp.plusplus.fbs.magic;

import cpw.mods.fml.common.registry.VillagerRegistry;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.entity.EntityMagicAuthor;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;

/**
 * Created by plusplus_F on 2015/09/27.
 */
public class MagicSummonVillager extends MagicBase {
    public static final int BOOKSHELF=24;

    @Override
    public boolean checkSuccess() {
        return false;
    }

    @Override
    public String getMagicCircleName(){
        return "fbs.summonVillager";
    }

    @Override
    public void success() {
        sanity(2,6);

        //魔法陣の周囲の本棚を数える
        int x= MathHelper.floor_double(player.posX);
        int y= MathHelper.floor_double(player.posY);
        int z= MathHelper.floor_double(player.posZ);
        int count=0;
        for(int k=0;k<2;k++){
            for(int i=0;i<5;i++){
                if(world.getBlock(x-2+i, y+k, z-2)==BlockCore.bookshelf) count++;
                if(world.getBlock(x-2+i, y+k, z+2)==BlockCore.bookshelf) count++;
            }
            for(int i=0;i<3;i++){
                if(world.getBlock(x-2, y+k, z-1+i)==BlockCore.bookshelf) count++;
                if(world.getBlock(x+2, y+k, z-1+i)==BlockCore.bookshelf) count++;
            }
        }

        //本棚が一定数以上あれば作家を湧かせる
        if(count>=BOOKSHELF){
            EntityMagicAuthor e=new EntityMagicAuthor(world);
            e.setPosition(player.posX+2*rand.nextFloat(), player.posY, player.posZ+2*rand.nextFloat());
            world.spawnEntityInWorld(e);
        }
        else{
            //通常の処理
            EntityVillager e=new EntityVillager(world, rand.nextInt(5+VillagerRegistry.getRegisteredVillagers().size()));
            e.setPosition(player.posX+2*rand.nextFloat(), player.posY, player.posZ+2*rand.nextFloat());
            world.spawnEntityInWorld(e);
        }

    }

    @Override
    public void failure() {
        sanity(3, 8);
    }
}
