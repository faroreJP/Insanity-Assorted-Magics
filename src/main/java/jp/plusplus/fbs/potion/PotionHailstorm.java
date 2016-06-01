package jp.plusplus.fbs.potion;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.entity.EntityMagicHailstorm;
import jp.plusplus.fbs.packet.MessagePacketThrower;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.particle.EntitySnowFX;
import jp.plusplus.fbs.particle.EntityTracksFX;
import jp.plusplus.fbs.render.RendererGameOverlay;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import java.util.List;

/**
 * Created by plusplus_F on 2016/03/18.
 */
public class PotionHailstorm extends Potion {
    private static int lastDuration;

    public PotionHailstorm(int id) {
        super(id, false, 0x000000);
        setPotionName("potions.fbs.hailstorm");
    }

    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        lastDuration=p_76397_1_;
        return p_76397_1_%3==0 || p_76397_1_<=1;
    }

    @Override
    public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_) {
        if(p_76394_1_.worldObj.isRemote){
            //パーティクル
            for(int i=0;i<3;i++){
                spawnParticle(p_76394_1_);
            }
            return;
        }

        //周囲の水を凍らせる
        int range=3;
        int cx= MathHelper.floor_double(p_76394_1_.posX);
        int cy= MathHelper.floor_double(p_76394_1_.posY);
        int cz= MathHelper.floor_double(p_76394_1_.posZ);
        for(int x=cx-range;x<cx+range;x++){
            for(int z=cz-range;z<cz+range;z++){
                for(int y=cy-1;y<cy+2;y++){
                    Block block=p_76394_1_.worldObj.getBlock(x,y,z);
                    if(block.getMaterial()== Material.water && p_76394_1_.worldObj.getBlockMetadata(x,y,z)==0){
                        p_76394_1_.worldObj.setBlock(x,y,z, Blocks.ice);
                    }
                }
            }
        }

        if(lastDuration>1 && lastDuration%(Math.max(20-5*p_76394_2_, 10))==0){
            int r=5;
            //氷塊をスポーンさせる
            List list=p_76394_1_.worldObj.getEntitiesWithinAABB(EntityMagicHailstorm.class, AxisAlignedBB.getBoundingBox(p_76394_1_.posX-r, p_76394_1_.posY-1, p_76394_1_.posZ-r, p_76394_1_.posX+r+1, p_76394_1_.posY+3, p_76394_1_.posZ+1+r));
            if(list.size()<3+p_76394_2_){
                EntityMagicHailstorm entity=new EntityMagicHailstorm(p_76394_1_.worldObj, p_76394_1_, 0.8f+0.8f*p_76394_1_.worldObj.rand.nextFloat(), 4.5f+1.5f*p_76394_2_);
                p_76394_1_.worldObj.spawnEntityInWorld(entity);
                PacketHandler.INSTANCE.sendToDimension(new MessagePacketThrower(entity), p_76394_1_.worldObj.provider.dimensionId);
            }
        }

    }

    @Override
    public void affectEntity(EntityLivingBase shooter, EntityLivingBase target, int lv, double effect) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        mc.renderEngine.bindTexture(RendererGameOverlay.icons);
        mc.currentScreen.drawTexturedModalRect(x+6,y+7, 0, 34, 18, 18);
    }

    public void spawnParticle(EntityLivingBase entity){
        EntitySnowFX fx=new EntitySnowFX(entity.worldObj, entity.posX+3*entity.worldObj.rand.nextFloat()-1.5, entity.posY+2.5*entity.worldObj.rand.nextFloat()-0.5, entity.posZ+3*entity.worldObj.rand.nextFloat()-1.5);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }
}
