package jp.plusplus.fbs.api;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 * ItemBookSorceryによって発動される魔法の基底クラス
 * 魔法を追加したくばこのクラスを継承すればよい
 */
public abstract class MagicBase {
    /**
     * 術者
     */
    public EntityPlayer player;
    /**
     * 術者のSAN値やら魔術Lvやら
     */
    public FBSEntityProperties property;
    /**
     * 登録されてい魔法データ
     */
    public Registry.MagicData magicData;
    /**
     * 登録されている書物データ
     */
    public Registry.BookData bookData;
    /**
     * worldのインスタンス
     * 基本的に全て isRemote は false となる
     */
    public World world;
    /**
     * 乱数を使うならコレ
     */
    public Random rand;
    /**
     * キチンと詠唱されたか否か
     * true:詠唱
     * false:詠唱破棄
     */
    public boolean isSpelled;
    /**
     * スタッフを用いているか否か
     * true:用いている
     */
    public boolean usingStaff;


    /**
     * 魔法が成功したかを判定する
     * このメソッドが呼ばれる際、既に各フィールドはセットされている
     * @return true:成功
     */
    public abstract boolean checkSuccess();

    /**
     * 行使成功時の魔法処理
     */
    public abstract void success();
    /**
     * 行使失敗時の魔法処理
    */
    public abstract void failure();

    /**
     * 魔法陣の登録名を返す。
     * nullの場合、その魔法は魔法陣を必要としない
     * @return 魔法陣の登録名
     */
    public String getMagicCircleName(){
        return "null";
    }

    /**
     * 術者が魔法陣の中心にいるか判定する
     * @param name 対象魔法陣の登録名
     * @return true:*まほうじんのなかにいる*
     */
    public boolean checkMagicCircle(String name){
        int x= MathHelper.floor_double(player.posX);
        int y=MathHelper.floor_double(player.posY);
        int z=MathHelper.floor_double(player.posZ);

        TileEntity e=world.getTileEntity(x,y,z);
        if(!(e instanceof TileEntityMagicCore)){
            return false;
        }
        return ((TileEntityMagicCore) e).getCircleName().equals(name);
    }

    /**
     * 術者の正気度を(trial)D(max)で喪失させる。
     * @param trial
     * @param max
     */
    protected void sanity(int trial, int max){
        SanityManager.loseSanity(player, trial, max, true);
    }

    /**
     * 術者に失敗時のメッセージを表示させる
    */
    public void failureMessage(){
        player.addChatComponentMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("info.fbs.magic.failure"), magicData.getLocalizedTitle())));
    }

    /**
     * 現在の魔術レベルとこの魔法の適正魔術Lvとの差を求める
     * @return (魔術レベル)-(適性魔術レベル)
     */
    public int getLvDiff(){
        return property.getMagicLevel()-bookData.lv;
    }

    /**
     * 術者が触れたブロックの座標を返す
     * @return
     */
    public Vec3 getTouchPosition(){
        Vec3 stPos = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 vec=player.getLookVec();
        vec.xCoord=vec.xCoord*5+stPos.xCoord;
        vec.yCoord=vec.yCoord*5+stPos.yCoord;
        vec.zCoord=vec.zCoord*5+stPos.zCoord;
        MovingObjectPosition movingobjectposition = world.func_147447_a(stPos, vec, false, true, false);
        if(movingobjectposition!=null){
            Vec3 p=Vec3.createVectorHelper(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
            return p;
        }

        return null;
    }

    /**
     * 術者が触れたEntityを返す
     * @return 触れたEntity
     */
    public Entity getTouchEntity(){
        Vec3 stPos = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 vec=player.getLookVec();
        vec.xCoord=vec.xCoord*5;
        vec.yCoord=vec.yCoord*5;
        vec.zCoord=vec.zCoord*5;

        Vec3 enPos=stPos.addVector(vec.xCoord, vec.yCoord, vec.zCoord);

        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(stPos.xCoord, stPos.yCoord, stPos.zCoord, stPos.xCoord+1, stPos.yCoord+1, stPos.zCoord+1);
        List list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb.addCoord(vec.xCoord, vec.yCoord, vec.zCoord).expand(1.0D, 1.0D, 1.0D));

        double r=0;
        Entity ret=null;
        for (int l = 0; l < list.size(); ++l) {
            Entity entity1 = (Entity) list.get(l);
            if(!entity1.isEntityEqual(player) && entity1.canBeCollidedWith()){
                float f1 = 0.3F;
                AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
                MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(stPos, enPos);

                if (movingobjectposition1 != null) {
                    double d1 = enPos.distanceTo(movingobjectposition1.hitVec);

                    if (d1 < r || r == 0.0D) {
                        ret=entity1;
                        r=d1;
                    }
                }
            }
        }

        return ret;
    }
}
