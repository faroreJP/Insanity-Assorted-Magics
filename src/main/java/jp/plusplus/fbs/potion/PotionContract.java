package jp.plusplus.fbs.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.render.RendererGameOverlay;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

/**
 * Created by plusplus_F on 2015/11/09.
 */
public class PotionContract extends Potion {
    public PotionContract(int id) {
        super(id, false, 0xffffff);
        setPotionName("potions.fbs.contract");
    }

    @Override
    public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_) {

    }

    @Override
    public void affectEntity(EntityLivingBase shooter, EntityLivingBase target, int lv, double effect) {

    }

    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        mc.renderEngine.bindTexture(RendererGameOverlay.icons);
        mc.currentScreen.drawTexturedModalRect(x+6,y+7, 0, 16, 18, 18);
    }
}
