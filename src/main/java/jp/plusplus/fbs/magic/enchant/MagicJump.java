package jp.plusplus.fbs.magic.enchant;

import jp.plusplus.fbs.api.MagicEnchantBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

/**
 * Createdby pluslus_Fon 2015/09/18.
 */
public class MagicJump extends MagicEnchantBase {
    public MagicJump() {
        super(2, 6);
    }

    @Override
    public boolean checkSuccess() {
        int d=getLvDiff();
        float prob=isSpelled?0.35f:0.1f;
        if(d>0) prob+=0.05f*d;
        return rand.nextFloat()<=prob;
    }

    @Override
    public void enchant(EntityLivingBase entity, boolean success) {
        if(!entity.onGround) return;

        int v=getLvDiff();
        double d=0.5+Math.max(0, v*0.05);
        if(entity instanceof EntityPlayerMP){
            if(!success) d*=2;
            entity.addVelocity(0, d-player.motionY, 0);
            ((EntityPlayerMP) entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
        }
        else{
            if(!success) d*=0.5;
            entity.addVelocity(0, d-player.motionY, 0);
        }
    }

    @Override
    public float damageScale(EntityLivingBase entity){
        return 0.5f;
    }
}
