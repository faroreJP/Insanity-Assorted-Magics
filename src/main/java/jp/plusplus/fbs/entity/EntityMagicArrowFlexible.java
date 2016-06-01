package jp.plusplus.fbs.entity;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.packet.MessageMagicFlexible;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Created by pluslus_F on 2015/06/23.
 * 汎用共鳴魔法の矢
 */
public class EntityMagicArrowFlexible extends EntityMagicProjectileBase {
    protected Registry.MagicData[] magics;
    protected IMagicEnchant[] instances;

    public EntityMagicArrowFlexible(World p_i1582_1_) {
        super(p_i1582_1_);
    }
    public EntityMagicArrowFlexible(World par1World, EntityLivingBase par2EntityLivingBase, float speed, float speed2, float damage, Registry.MagicData... magics) {
        super(par1World, par2EntityLivingBase, speed, speed2, 0, 0, 0);
        setDamage(damage);
        this.magics=magics;
    }

    public void readMagicsFromNBT(NBTTagCompound nbt){
        NBTTagList nbttaglist = (NBTTagList)nbt.getTag("EnchantMagics");
        magics=new Registry.MagicData[nbttaglist.tagCount()];
        for(int i=0;i<nbttaglist.tagCount();i++){
            NBTTagCompound nbt1 = nbttaglist.getCompoundTagAt(i);
            magics[i]=Registry.GetMagic(nbt1.getString("MagicName"));
        }
    }
    public void writeMagicsToNBT(NBTTagCompound nbt){
        NBTTagList nbttaglist = new NBTTagList();
        for(int i=0;i<magics.length;i++){
            Registry.MagicData md=magics[i];
            NBTTagCompound nbt1 = new NBTTagCompound();
            nbt1.setString("MagicName", md.title);
            nbttaglist.appendTag(nbt1);
        }
        nbt.setTag("EnchantMagics", nbttaglist);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        readMagicsFromNBT(nbt);
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        writeMagicsToNBT(nbt);
    }

    public void hitWith(EntityLivingBase e){
        float dValue=0.f;
        float dScale=1.f;

        //IMagicEnchantのインスタンス化
        //スタッフによる使用なので常に詠唱破棄状態
        IMagicEnchant[] me=new IMagicEnchant[magics.length];
        for(int i=0;i<me.length;i++){
            me[i]=(IMagicEnchant)magics[i].getMagic(worldObj, (EntityPlayer)shootingEntity, false);
            dValue+=me[i].damageValue(e);
            dScale*=me[i].damageScale(e);
        }

        //攻撃
        e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(shootingEntity, e), (getDamage()+dValue)*dScale);

        //エンチャント
        for(int i=0;i<me.length;i++){
            me[i].enchant(e, true);
        }

        setDead();
    }

    @Override
    public boolean canExist(){ return worldObj.isRemote || ticksExisted<20*1.5; }

    @Override
    public void onCollideWithPlayer(MovingObjectPosition pos, EntityPlayer entity){
        if(!worldObj.isRemote && !shootingEntity.isEntityEqual(entity)) {
            hitWith(entity);
        }
    }
    @Override
    public void onCollideWithMob(MovingObjectPosition pos, EntityMob entity){
        if(!worldObj.isRemote) {
            hitWith(entity);
        }
    }
    @Override
    public void onCollideWithLiving(MovingObjectPosition pos, EntityLiving entity){
        if(!worldObj.isRemote) {
            hitWith(entity);
        }
    }

    @Override
    protected void setParticleColor(){
        if(magics==null) return;

        //インスタンスの生成
        if(instances==null){
            instances=new IMagicEnchant[magics.length];
            for(int i=0;i<instances.length;i++){
                instances[i]=(IMagicEnchant)magics[i].getMagic(worldObj, (EntityPlayer)shootingEntity, false);
            }
        }

        //色の設定
        int i=rand.nextInt(magics.length);
        IMagicEnchant.ParticleColor col=instances[i].setParticleColor();
        if(col!=null){
            particleRed=col.red;
            particleGreen=col.green;
            particleBlue=col.blue;
        }
        else{
            particleRed=particleGreen=particleBlue=1;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(ticksInAir==1 && !worldObj.isRemote){
            PacketHandler.INSTANCE.sendToDimension(new MessageMagicFlexible(this), worldObj.provider.dimensionId);
        }
    }
}
