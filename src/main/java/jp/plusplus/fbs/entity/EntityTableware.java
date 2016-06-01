package jp.plusplus.fbs.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/11/06.
 */
public class EntityTableware extends Entity {

    protected int containerMeta;
    private boolean field_70279_a;
    private double speedMultiplier;
    private int posRotationIncrements;
    private double X;
    private double Y;
    private double Z;
    private double yaw;
    private double pitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

    public EntityTableware(World world) {
        super(world);
        this.containerMeta = 0;
        this.field_70279_a = true;
        this.speedMultiplier = 0.07D;
        this.setSize(0.3F * this.getSize(), 0.1F * this.getSize());
        this.yOffset = this.height;
    }

    public EntityTableware(World world, ItemStack item) {
        this(world);
        this.setContainerMeta(item.getItemDamage());
    }

    public EntityTableware(World world, ItemStack item, double x, double y, double z) {
        this(world, item);
        this.setPosition(x, y + (double)this.yOffset, z);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
    }

    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.setContainerMeta(nbt.getShort("meta"));
    }

    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("meta", (short)this.getItemMetadata());
    }

    public int getItemMetadata() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setContainerMeta(int m) {
        this.containerMeta = m;
        this.dataWatcher.updateObject(17, Integer.valueOf(m));
    }

    public void setForwardDirection(int par1) {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return par1Entity.boundingBox;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public boolean canBePushed() {
        return true;
    }

    public double getMountedYOffset() {
        return (double)this.getScale() * 0.4D - 0.06D;
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if(this.isEntityInvulnerable()) {
            return false;
        } else if(!this.worldObj.isRemote && !this.isDead) {
            this.setBeenAttacked();
            if(par1DamageSource instanceof EntityDamageSource) {
                Entity by = ((EntityDamageSource)par1DamageSource).getEntity();
                if(by != null && by instanceof EntityPlayer) {
                    ItemStack drop1 = this.returnItem();
                    if(drop1 != null) {
                        this.worldObj.playSoundAtEntity(this, "random.pop", 0.4F, 1.8F);
                        this.entityDropItem(drop1, 0.2F);
                        if(this.riddenByEntity != null) {
                            this.riddenByEntity.mountEntity(this);
                        }

                        this.setDead();
                    }
                }
            }

            return true;
        } else {
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
        if(this.field_70279_a) {
            this.posRotationIncrements = par9 + 5;
        } else {
            double d3 = par1 - this.posX;
            double d4 = par3 - this.posY;
            double d5 = par5 - this.posZ;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;
            if(d6 <= 1.0D) {
                return;
            }

            this.posRotationIncrements = 3;
        }

        this.X = par1;
        this.Y = par3;
        this.Z = par5;
        this.yaw = (double)par7;
        this.pitch = (double)par8;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5) {
        this.velocityX = this.motionX = par1;
        this.velocityY = this.motionY = par3;
        this.velocityZ = this.motionZ = par5;
    }

    public void onUpdate() {
        super.onUpdate();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        byte b0 = 5;
        double d0 = 0.0D;
        boolean spl = false;

        int d3;
        for(d3 = 0; d3 < b0; ++d3) {
            double j = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(d3 + 0) / (double)b0 - 0.125D;
            double tile = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(d3 + 1) / (double)b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.boundingBox.minX, j, this.boundingBox.minZ, this.boundingBox.maxX, tile, this.boundingBox.maxZ);
            if(this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
                d0 += 1.0D / (double)b0;
                spl = true;
            }
        }

        if(!this.worldObj.isRemote) {
            d3 = MathHelper.floor_double(this.posX);
            int var21 = MathHelper.floor_double(this.posY);
            int d4 = MathHelper.floor_double(this.posZ);
            if(!this.worldObj.isAirBlock(d3, var21 - 1, d4) && this.worldObj.getTileEntity(d3, var21 - 1, d4) != null) {
                TileEntity var23 = this.worldObj.getTileEntity(d3, var21 - 1, d4);
                if(var23 instanceof IHopper) {
                    ItemStack d5 = this.returnItem();
                    this.entityDropItem(d5, 0.1F);
                    this.setDead();
                }
            }
        }

        double var20 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double var22;
        double var24;
        if(var20 > 0.26249999999999996D && spl) {
            var22 = Math.cos((double)this.rotationYaw * 3.141592653589793D / 180.0D);
            var24 = Math.sin((double)this.rotationYaw * 3.141592653589793D / 180.0D);

            for(int d10 = 0; (double)d10 < 1.0D + var20 * 60.0D; ++d10) {
                double d6 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
                double d7 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double l;
                if(this.rand.nextBoolean()) {
                    d8 = this.posX - var22 * d6 * 0.8D + var24 * d7;
                    l = this.posZ - var24 * d6 * 0.8D - var22 * d7;
                    this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, l, this.motionX, this.motionY, this.motionZ);
                } else {
                    d8 = this.posX + var22 + var24 * d6 * 0.7D;
                    l = this.posZ + var24 - var22 * d6 * 0.7D;
                    this.worldObj.spawnParticle("splash", d8, this.posY - 0.125D, l, this.motionX, this.motionY, this.motionZ);
                }
            }
        }

        double d11;
        double var25;
        if(this.worldObj.isRemote && this.field_70279_a) {
            if(this.posRotationIncrements > 0) {
                var22 = this.posX + (this.X - this.posX) / (double)this.posRotationIncrements;
                var24 = this.posY + (this.Y - this.posY) / (double)this.posRotationIncrements;
                d11 = this.posZ + (this.Z - this.posZ) / (double)this.posRotationIncrements;
                var25 = MathHelper.wrapAngleTo180_double(this.yaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var25 / (double)this.posRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.pitch - (double)this.rotationPitch) / (double)this.posRotationIncrements);
                --this.posRotationIncrements;
                this.setPosition(var22, var24, d11);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                var22 = this.posX + this.motionX;
                var24 = this.posY + this.motionY;
                d11 = this.posZ + this.motionZ;
                this.setPosition(var22, var24, d11);
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        } else {
            if(d0 < 1.0D) {
                var22 = d0 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * var22;
            } else {
                if(this.motionY < 0.0D) {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
                var22 = (double)((EntityLivingBase)this.riddenByEntity).moveForward;
                if(var22 > 0.0D) {
                    var24 = -Math.sin((double)(this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
                    d11 = Math.cos((double)(this.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
                    this.motionX += var24 * this.speedMultiplier * 0.05000000074505806D;
                    this.motionZ += d11 * this.speedMultiplier * 0.05000000074505806D;
                }
            }

            var22 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if(var22 > 0.35D) {
                var24 = 0.35D / var22;
                this.motionX *= var24;
                this.motionZ *= var24;
                var22 = 0.35D;
            }

            if(var22 > var20 && this.speedMultiplier < 0.35D) {
                this.speedMultiplier += (0.1D - this.speedMultiplier) / 35.0D;
                if(this.speedMultiplier > 0.35D) {
                    this.speedMultiplier = 0.35D;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
                if(this.speedMultiplier < 0.07D) {
                    this.speedMultiplier = 0.07D;
                }
            }

            if(this.onGround) {
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9900000095367432D;
            this.motionY *= 0.949999988079071D;
            this.motionZ *= 0.9900000095367432D;
            this.rotationPitch = 0.0F;
            var24 = (double)this.rotationYaw;
            d11 = this.prevPosX - this.posX;
            var25 = this.prevPosZ - this.posZ;
            if(d11 * d11 + var25 * var25 > 0.001D) {
                var24 = (double)((float)(Math.atan2(var25, d11) * 180.0D / 3.141592653589793D));
            }

            double d12 = MathHelper.wrapAngleTo180_double(var24 - (double)this.rotationYaw);
            if(d12 > 20.0D) {
                d12 = 20.0D;
            }

            if(d12 < -20.0D) {
                d12 = -20.0D;
            }

            this.rotationYaw = (float)((double)this.rotationYaw + d12);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if(!this.worldObj.isRemote) {
                List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
                if(list != null && !list.isEmpty()) {
                    for(int var26 = 0; var26 < list.size(); ++var26) {
                        Entity entity = (Entity)list.get(var26);
                        if(entity != this.riddenByEntity && entity.canBePushed()) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }

                if(this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    public void updateRiderPosition() {
        if(this.riddenByEntity != null) {
            double d0 = Math.cos((double)this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
            double d1 = Math.sin((double)this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
            this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
        }

    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.3F;
    }

    public boolean interactFirst(EntityPlayer par1EntityPlayer) {
        ItemStack has;
        has = this.returnItem();
        if (has != null && !par1EntityPlayer.inventory.addItemStackToInventory(has) && !this.worldObj.isRemote) {
            par1EntityPlayer.entityDropItem(has, 1.0F);
        }

        this.setDead();
        this.worldObj.playSoundAtEntity(par1EntityPlayer, "random.pop", 0.4F, 1.8F);
        return true;
    }

    protected ItemStack returnItem(){
        return new ItemStack(ItemCore.tableware, 1, containerMeta);
    }

    @SideOnly(Side.CLIENT)
    public void func_70270_d(boolean par1) {
        this.field_70279_a = par1;
    }

    protected byte particleNumber() {
        return (byte)0;
    }

    protected float getScale() {
        return 1.0F;
    }

    protected float getSize() {
        return 1.0F;
    }

}
