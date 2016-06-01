package jp.plusplus.fbs.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.render.RendererGameOverlay;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by plusplus_F on 2016/03/18.
 */
public class PotionMagnet extends Potion {
    public PotionMagnet(int id) {
        super(id, false, 0x000000);
        setPotionName("potions.fbs.magnet");
    }

    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return p_76397_1_%5==0;
    }

    @Override
    public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_) {

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
}
