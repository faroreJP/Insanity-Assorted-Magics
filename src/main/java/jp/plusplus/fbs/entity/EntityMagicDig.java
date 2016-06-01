package jp.plusplus.fbs.entity;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Createdby pluslus_Fon 2015/06/07.
 */
public class EntityMagicDig extends EntityMagicProjectileBase {
    protected int till;
    protected boolean isPenetrate;

    public EntityMagicDig(World p_i1582_1_) {
        super(p_i1582_1_);
        till=10;
    }
    public EntityMagicDig(World par1World, EntityLivingBase par2EntityLivingBase, float speed, float speed2, int till, boolean isPenetrate) {
        super(par1World, par2EntityLivingBase, speed, speed2, 0, 0, 0);
        this.till=10+till;
        this.isPenetrate=isPenetrate;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        isPenetrate=nbt.getBoolean("IsPenetrate");
        till=nbt.getInteger("magicTill");
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("IsPenetrate", isPenetrate);
        nbt.setInteger("magicTill", till);
    }

    public boolean canExist(){ return worldObj.isRemote || ticksExisted<till; }
    public void onCollideWithBlock(MovingObjectPosition pos, Block block) {
        if (!worldObj.isRemote) {
            float h = block.getBlockHardness(worldObj, xTile, yTile, zTile);
            if (h <= 30.0f && h != -1.0f){
                worldObj.func_147480_a(xTile, yTile, zTile, true);
            }
            else{
                setDead();
                return;
            }
        }
        if (isPenetrate) {
            inTile = Blocks.air;
            inData = 0;
        } else {
            setDead();
        }
    }

    @Override
    protected void setParticleColor(){
        particleRed=0.5f+0.5f*rand.nextFloat();
        particleGreen=0.25f+0.2f*rand.nextFloat();
        particleBlue=0.2f*rand.nextFloat();
    }
}
