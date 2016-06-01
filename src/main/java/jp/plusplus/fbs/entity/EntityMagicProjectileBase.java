package jp.plusplus.fbs.entity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.event.FBSEventHandler;
import jp.plusplus.fbs.particle.EntityGlowFX;
import jp.plusplus.fbs.particle.EntityTracksFX;
import jp.plusplus.fbs.particle.EntityTrajectoryFX;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;

import java.util.List;
import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/07.
 */
public class EntityMagicProjectileBase extends Entity implements IProjectile {
    /* 地中判定に使うもの */
    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected Block inTile;
    protected int inData;
    protected boolean inGround;

    /* この弾を撃ったエンティティ */
    public Entity shootingEntity;

    /* 地中・空中にいる時間 */
    protected int ticksInGround;
    protected int ticksInAir;

    /* ダメージの大きさ */
    protected float damage;

    /* ノックバックの大きさ */
    protected int knockbackStrength = 1;

    /* パーティクル */
    protected boolean enableParticle=true;
    protected float particleRed=1.f;
    protected float particleGreen=1.f;
    protected float particleBlue=1.f;

    /* パーティクル関係 */
    protected boolean hasMatrix=false;
    protected double[] matrix=new double[4*4];
    protected double preMotionX;
    protected double preMotionY;
    protected double preMotionZ;

    public EntityMagicProjectileBase(World p_i1582_1_) {
        super(p_i1582_1_);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.1875F, 0.3125F);
        rand=new Random();
    }

    public EntityMagicProjectileBase(World par1World, EntityLivingBase entity, float speed, float speed2, float adjustX, float adjustZ, float adjustY) {
        super(par1World);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = entity;
        this.yOffset = 0.0F;
        this.setSize(0.1875F, 0.3125F);

        //初期状態での向きの決定
        this.setLocationAndAngles(entity.posX, entity.posY + (double) entity.getEyeHeight() -0.10000000149011612D, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        //this.posY += adjustY-0.10000000149011612D;
        this.setPosition(this.posX, this.posY, this.posZ);

        xTile=(int)posX;
        yTile=(int)posY;
        zTile=(int)posZ;

        //初速度
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F, speed2);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.xTile = nbt.getShort("xTile");
        this.yTile = nbt.getShort("yTile");
        this.zTile = nbt.getShort("zTile");

        String t=nbt.getString("inTile");
        if(t.equals("null")) inTile=null;
        else this.inTile = Block.getBlockFromName(t);

        this.inData = nbt.getByte("inData") & 255;
        this.inGround = nbt.getByte("inGround") == 1;

        if (nbt.hasKey("damage")) {
            this.damage = nbt.getFloat("damage");
        }

        if(nbt.hasKey("EnableParticle")){
            enableParticle=nbt.getBoolean("EnableParticle");
            particleRed=nbt.getFloat("ParticleRed");
            particleGreen=nbt.getFloat("ParticleGreen");
            particleBlue=nbt.getFloat("ParticleBlue");
        }

        if(nbt.hasKey("ShooterId")){
            shootingEntity=worldObj.getEntityByID(nbt.getInteger("ShooterId"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("xTile", (short) this.xTile);
        nbt.setShort("yTile", (short) this.yTile);
        nbt.setShort("zTile", (short) this.zTile);
        nbt.setString("inTile", inTile == null ? "null" : Block.blockRegistry.getNameForObject(inTile));
        nbt.setByte("inData", (byte) this.inData);
        nbt.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbt.setFloat("damage", this.damage);
        nbt.setBoolean("EnableParticle", enableParticle);
        nbt.setFloat("ParticleRed", particleRed);
        nbt.setFloat("ParticleGreen", particleGreen);
        nbt.setFloat("ParticleBlue", particleBlue);
        nbt.setInteger("ShooterId", shootingEntity.getEntityId());
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double) f2;
        par3 /= (double) f2;
        par5 /= (double) f2;
        par1 += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) par8;
        par3 += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) par8;
        par5 += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) par8;
        par1 *= (double) par7;
        par3 *= (double) par7;
        par5 *= (double) par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5) {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, (double) f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }


    public boolean isPenetrateBlock(){
        return false;
    }
    public boolean isPenetrateEntity(){
        return false;
    }

    public void onCollideWithPlayer(MovingObjectPosition pos, EntityPlayer entity){}

    public void onCollideWithMob(MovingObjectPosition pos, EntityMob entity){}

    public void onCollideWithLiving(MovingObjectPosition pos, EntityLiving entity){}

    public void onCollideWithEntity(MovingObjectPosition pos, Entity entity){}

    public void onCollideWithBlock(MovingObjectPosition pos, Block block){}

    public void inWater(){}

    public float fallSpeed(){ return 0; }

    public DamageSource thisDamageSource(Entity entity) {
        return entity != null ? EntityDamageSource.causeIndirectMagicDamage(entity, this) : DamageSource.magic;
    }

    public String getSoundName(){
        return "random.bowhit";
    }

    public boolean canExist(){ return ticksExisted<20; }

    protected boolean canTriggerWalking() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public void setDamage(float par1) {
        this.damage = par1;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int par1) {
        this.knockbackStrength = par1;
    }

    public boolean canAttackWithItem() {
        return false;
    }

    /**
     * パーティクル発生間隔
     * @return
     */
    protected int getParticleInterval(){
        return 1;
    }

    /**
     * パーティクルの色を設定する。
     * enableParticleがtrueでなければ呼ばれない
     */
    protected void setParticleColor(){
        particleRed=particleGreen=particleBlue=1.f;
    }

    /**
     * パーティクルの大きさを得る
     * @return
     */
    protected float getParticleSize(){
        return 0.75f+0.5f*rand.nextFloat();
    }

    protected void calcMatrix(){
        double pc=MathHelper.cos(rotationPitch);
        double ps=MathHelper.sin(rotationPitch);
        double yc=MathHelper.cos(rotationYaw);
        double ys=MathHelper.sin(rotationYaw);

        // ロール・ピッチ・ヨーの回転行列を得る
        matrix[0]=1*pc;
        matrix[1]=1*ps*ys;
        matrix[2]=1*ps*yc+0;
        matrix[3]=0;

        matrix[4]=ys*pc;
        matrix[5]=0+1*yc;
        matrix[6]=0-1*ys;
        matrix[7]=0;

        matrix[8]=-ps;
        matrix[9]=pc*ys;
        matrix[10]=pc*yc;
        matrix[11]=0;

        matrix[12]=matrix[13]=matrix[14]=0;
        matrix[15]=1;
    }

    @SideOnly(Side.CLIENT)
    protected void spawnParticle(){
        //1.法線を得る
        //Vec3 normal=Vec3.createVectorHelper(motionX, motionY, motionZ).normalize();

        //2.法線から回転行列を得る(計算済み)

        //3.x-z平面に360度の範囲で動くベクトルを得る
        float angle=(float)(rand.nextDouble()*Math.PI*2);
        float size=0.5f+0.75f*rand.nextFloat();
        Vec3 baseV=Vec3.createVectorHelper(MathHelper.cos(angle), 0, MathHelper.sin(angle));
        baseV.xCoord*=size;
        baseV.zCoord*=size;

        //4.回転行列でベクトルを回転させる
        Vec3 vector=Vec3.createVectorHelper(0,0,0);
        vector.xCoord=matrix[0]*baseV.xCoord+matrix[1]*baseV.yCoord+matrix[2]*baseV.zCoord+matrix[3]*1;
        vector.yCoord=matrix[4]*baseV.xCoord+matrix[5]*baseV.yCoord+matrix[6]*baseV.zCoord+matrix[7]*1;
        vector.zCoord=matrix[8]*baseV.xCoord+matrix[9]*baseV.yCoord+matrix[10]*baseV.zCoord+matrix[11]*1;

        //5.パーティクルつくる
        setParticleColor();

        EntityTracksFX fx=new EntityTracksFX(worldObj, posX, posY, posZ, vector.xCoord, vector.yCoord, vector.zCoord, particleRed, particleGreen, particleBlue, getParticleSize());
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);

        if(this.ticksExisted%3==0){
            EntityTrajectoryFX fx2=new EntityTrajectoryFX(worldObj, posX, posY, posZ, motionX, motionY, motionZ);
            fx2.setParticleIcon(FBSEventHandler.SpellTexture);
            fx2.setRBGColorF(particleRed, particleGreen, particleBlue);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx2);
        }
    }

    /*
     * Tick毎に呼ばれる更新処理。
     * 速度の更新、衝突判定などをここで行う。
     */
    public void onUpdate() {
        super.onUpdate();

        //パーティクル用行列の生成
        if(!hasMatrix || preMotionX!=motionX || preMotionY!=motionY || preMotionZ!=motionZ){
            calcMatrix();
            hasMatrix=true;
            preMotionX=motionX;
            preMotionY=motionY;
            preMotionZ=motionZ;
        }

        //パーティクルを生成
        if(FBS.proxy.getClientWorld()!=null && worldObj.isRemote && enableParticle && this.ticksExisted%getParticleInterval()==0){
            spawnParticle();
        }

        //直前のパラメータと新パラメータを一致させているところ。
        //また、速度に応じてエンティティの向きを調整し、常に進行方向に前面が向くようにしている。
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / Math.PI);
        }

        //激突したブロックを確認している
        Block block=worldObj.getBlock(xTile, yTile, zTile);
        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, xTile, yTile, zTile);

            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        //空気じゃないブロックに当たった
        if (this.inGround) {
            block = worldObj.getBlock(xTile, yTile, zTile);
            int meta = worldObj.getBlockMetadata(xTile, yTile, zTile);

            if (block == this.inTile && meta == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround >= (isPenetrateBlock() ? 20 : 2)) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else{
            //埋まってない時。速度の更新。
            //ブロックとの衝突判定
            ++this.ticksInAir;
            Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
            vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            //ブロック貫通がONの場合、ブロック衝突判定をスキップ
            if (this.isPenetrateBlock()) {
                movingobjectposition = null;
            }

            //ブロックに当たった
            if (movingobjectposition != null) {
                vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            //Entityとの衝突判定。
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;

            //1ブロック分の範囲内にいるエンティティ全てに対して繰り返す
            for (l = 0; l < list.size(); ++l) {
                Entity entity1 = (Entity) list.get(l);
                //FMLLog.severe("checking hit at:"+entity1.toString());
                //発射物自身or発射後5tick以外だとすりぬける
                if (entity1.canBeCollidedWith() /*&& (entity1 != this.shootingEntity ||this.ticksInAir >= 5)*/ ) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            //FMLLog.severe("hit at:"+entity1.toString());
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            //エンティティに当たった
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            int hitType=-1;

            /* 当たったエンティティそれそれについての判定部分。*/
            if (movingobjectposition != null && movingobjectposition.entityHit != null) {
                if (movingobjectposition.entityHit instanceof EntityPlayer) {
                    hitType=0;
                } else if(movingobjectposition.entityHit instanceof EntityMob){
                    hitType=1;
                }
                else if (movingobjectposition.entityHit instanceof EntityLiving) {
                    hitType=2;
                } else {
                    hitType=3;
                }

                /*
                //当たったあと、弾を消去する。エンティティ貫通がONの弾種はそのまま残す。
                if (!(movingobjectposition.entityHit instanceof EntityEnderman) && !this.isPenetrateEntity()) {
                    this.setDead();
                }
                */
            }

            //当たったあとの処理
            if (movingobjectposition != null) {
                if(movingobjectposition.entityHit!=null){
                    switch (hitType){
                        case 0:
                            onCollideWithPlayer(movingobjectposition, (EntityPlayer) movingobjectposition.entityHit);
                            break;

                        case 1:
                            onCollideWithMob(movingobjectposition, (EntityMob)movingobjectposition.entityHit);
                            break;

                        case 2:
                            onCollideWithLiving(movingobjectposition, (EntityLiving)movingobjectposition.entityHit);
                            break;

                        case 3:
                            onCollideWithEntity(movingobjectposition, entity);
                            break;

                        default:
                            break;
                    }
                }
                else {
                    //エンティティには当たってない。ブロックに当たった。
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

                    onCollideWithBlock(movingobjectposition, worldObj.getBlock(xTile, yTile, zTile));

                    if (!isPenetrateBlock() && inTile.getMaterial()!=Material.air) {
                        this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
                        this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
                        this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
                        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                        this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
                        this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
                        this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;

                        this.playSound(getSoundName(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    }

                    //ブロックが破壊されていない場合
                    if (this.inTile.getMaterial() != Material.air) {
                        this.inGround = true;
                        inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                    }
                }
            }

            //改めてポジションに速度を加算。
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            motionY-=fallSpeed();

            if(isInWater()){
                inWater();
            }

            if (!canExist()) {
                this.setDead();
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();

            //ticksExisted++;
        }
    }
}
