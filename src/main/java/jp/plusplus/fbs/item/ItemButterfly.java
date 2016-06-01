package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.entity.EntityButterfly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by plusplus_F on 2015/08/20.
 * ちょうちょ
 */
public class ItemButterfly extends ItemBase {
    public ItemButterfly() {
        setUnlocalizedName("butterfly");
        setTextureName("butterfly");
    }

    //Entityの生成
    public static Entity spawnCreature(World par0World, int meta, double x, double y, double z) {
        Entity entity = null;
        entity = new EntityButterfly(par0World);

        EntityLiving entityliving = (EntityLiving) entity;
        entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
        entityliving.rotationYawHead = entityliving.rotationYaw;
        entityliving.renderYawOffset = entityliving.rotationYaw;
        entityliving.onSpawnWithEgg((IEntityLivingData) null);
        par0World.spawnEntityInWorld(entity);
        entityliving.playLivingSound();

        return entity;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par2World.isRemote) {
            return par1ItemStack;
        } else {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

            if (movingobjectposition == null) {
                return par1ItemStack;
            } else {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!par2World.canMineBlock(par3EntityPlayer, i, j, k)) {
                        return par1ItemStack;
                    }

                    if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack)) {
                        return par1ItemStack;
                    }

                    if (par2World.getBlock(i, j, k) instanceof BlockLiquid) {
                        Entity entity = spawnCreature(par2World, par1ItemStack.getItemDamage(), (double) i, (double) j, (double) k);

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
                                ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                            }

                            if (!par3EntityPlayer.capabilities.isCreativeMode) {
                                --par1ItemStack.stackSize;
                            }
                        }
                    }
                }

                return par1ItemStack;
            }
        }
    }
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (par3World.isRemote) {
            return true;
        } else {
            Block block = par3World.getBlock(x, y, z);
            x += Facing.offsetsXForSide[side];
            y += Facing.offsetsYForSide[side];
            z += Facing.offsetsZForSide[side];

            //-------------------------------ポータル生成処理-------------------------------
            boolean genPortal=false;
            if(block==BlockCore.plank){
                int sizeMax=4;//半径
                int sizeU=0, sizeD=0, sizeL=0, sizeR=0;
                int sx=x, sy=y, sz=z;

                //縦方向に木材があるか
                boolean foundT=false, foundB=false;
                for(int i=0;i<sizeMax;i++){
                    Block b=par3World.getBlock(sx, sy, sz);
                    if(b.getMaterial()!= Material.air){
                        foundT=b==BlockCore.plank;
                        sizeU=i;
                        break;
                    }
                    sy++;
                }
                sy=y;
                for(int i=0;i<sizeMax;i++){
                    Block b=par3World.getBlock(sx, sy, sz);
                    if(b.getMaterial()!= Material.air){
                        foundB=b==BlockCore.plank;
                        sizeD=i;
                        break;
                    }
                    sy--;
                }

                //縦方向が揃っていれば次は横
                if(foundT && foundB){
                    //横方向に木材があるかどうか
                    boolean foundL=false, foundR=false;
                    sy=y;

                    //南北
                    for(int i=0;i<sizeMax;i++){
                        Block b=par3World.getBlock(sx, sy, sz);
                        if(b.getMaterial()!= Material.air){
                            foundL=b==BlockCore.plank;
                            sizeL=i;
                            break;
                        }
                        sz--;
                    }
                    sz=z;
                    for(int i=0;i<sizeMax;i++){
                        Block b=par3World.getBlock(sx, sy, sz);
                        if(b.getMaterial()!= Material.air){
                            foundR=b==BlockCore.plank;
                            sizeR=i;
                            break;
                        }
                        sz++;
                    }

                    if(foundL && foundR){
                        //ポータル生成
                        if(tryMakePortal(par3World, x,y,z, false, sizeU, sizeD, sizeL, sizeR)){
                            return true;
                        }
                    }

                    //東西
                    foundL=foundR=false;
                    sz=z;
                    for(int i=0;i<sizeMax;i++){
                        Block b=par3World.getBlock(sx, sy, sz);
                        if(b.getMaterial()!= Material.air){
                            foundL=b==BlockCore.plank;
                            sizeL=i;
                            break;
                        }
                        sx--;
                    }
                    sx=x;
                    for(int i=0;i<sizeMax;i++){
                        Block b=par3World.getBlock(sx, sy, sz);
                        if(b.getMaterial()!= Material.air){
                            foundR=b==BlockCore.plank;
                            sizeR=i;
                            break;
                        }
                        sx++;
                    }
                    if(foundL && foundR){
                        //ポータル生成
                        if(tryMakePortal(par3World, x,y,z, true, sizeU, sizeD, sizeL, sizeR)){
                            return true;
                        }
                    }

                }
            }

            //--------------------------蝶をスポーンする-------------------------------------
            if(!genPortal){
                double d0 = 0.0D;

                if (side == 1 && block.getRenderType() == 11) {
                    d0 = 0.5D;
                }

                Entity entity = spawnCreature(par3World, par1ItemStack.getItemDamage(), (double) x + 0.5D, (double) y + d0, (double) z + 0.5D);
                if (entity != null) {
                    if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
                        ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                    }

                    if (!par2EntityPlayer.capabilities.isCreativeMode) {
                        --par1ItemStack.stackSize;
                    }
                }
            }

            return true;
        }
    }

    /**
     * ポータルを生成できるか判定し、生成できたらtrue
     * @param world
     * @param x
     * @param y
     * @param z
     * @param sizeU
     * @param sizeD
     * @param sizeL
     * @param sizeR
     * @return
     */
    protected boolean tryMakePortal(World world, int x, int y, int z, boolean we, int sizeU, int sizeD, int sizeL, int sizeR){
        int sizeUD=sizeU+sizeD+1;
        int sizeLR=sizeL+sizeR+1;

        int dx=x-sizeL;
        int dy=y-sizeD;
        int dz=z-sizeL;
        if(we){
            //枠が揃ってるか調べる
            for(int i=0;i<sizeLR;i++){
                if(world.getBlock(dx+i, dy, z)!=BlockCore.plank) return false;
                if(world.getBlock(dx+i, dy+sizeUD-1, z)!=BlockCore.plank) return false;
            }
            for(int i=0;i<sizeUD;i++){
                if(world.getBlock(dx, dy+i, z)!=BlockCore.plank) return false;
                if(world.getBlock(dx+sizeLR-1, dy+i, z)!=BlockCore.plank) return false;
            }

            //ポータル生成
            for(int xx=dx+1;xx<dx+sizeLR-1;xx++){
                for(int yy=dy+1;yy<dy+sizeUD-1;yy++){
                    world.setBlock(xx,yy,z,BlockCore.portal2,1,2);
                }
            }

            return true;
        }
        else{
            //枠が揃ってるか調べる
            for(int i=0;i<sizeLR;i++){
                if(world.getBlock(x, dy, dz+i)!=BlockCore.plank) return false;
                if(world.getBlock(x, dy+sizeUD-1, dz+i)!=BlockCore.plank) return false;
            }
            for(int i=0;i<sizeUD;i++){
                if(world.getBlock(x, dy+i, dz)!=BlockCore.plank) return false;
                if(world.getBlock(x, dy+i, dz+sizeLR-1)!=BlockCore.plank) return false;
            }

            //ポータル生成
            for(int zz=dz+1;zz<dz+sizeLR-1;zz++){
                for(int yy=dy+1;yy<dy+sizeUD-1;yy++){
                    world.setBlock(x,yy,zz,BlockCore.portal2,2,2);
                }
            }

            return true;
        }
    }
}
