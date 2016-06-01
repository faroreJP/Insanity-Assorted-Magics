package jp.plusplus.fbs.render;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;

/**
 * Createdby pluslus_Fon 2015/06/07.
 */
public class RendererBook implements IItemRenderer {
    protected static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        EnumAction act=item.getItemUseAction();
        if(act!= FBS.actionDecode && act!=FBS.actionSpell) return false;
        return type==ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if(type==ItemRenderType.EQUIPPED_FIRST_PERSON) {
            EntityPlayer ep = FBS.proxy.getEntityPlayerInstance();

            if (ep.isUsingItem()) {
                float tick = FBS.proxy.getRenderPartialTicks();
                float f1 = 1.0f;
                float f2 = ep.prevRotationPitch + (ep.rotationPitch - ep.prevRotationPitch) * tick;
                float f5;
                float f6;
                float f7;

                //---------------------------------
                float f13 = 0.8F;

                f6 = (float) ep.getItemInUseCount() - tick + 1.0F;
                f7 = 1.0F - f6 / (float) item.getMaxItemUseDuration();
                float f8 = 1.0F - f7;
                f8 = f8 * f8 * f8;
                f8 = f8 * f8 * f8;
                f8 = f8 * f8 * f8;
                float f9 = 1.0F - f8;
                GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(f6 / 4.0F * (float) Math.PI) * 0.1F) * (float) ((double) f7 > 0.2D ? 1 : 0), 0.0F);
                GL11.glTranslatef(f9 * 0.6F, -f9 * 0.4F, 0.0F);
                GL11.glRotatef(f9 * 45.0f, -f9 * 45.0f, f9 * 45.0f, 0.0F);
                GL11.glRotatef(f9 * 10.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f9 * 30.0F, 0.0F, 0.0F, 1.0F);
            }

            IIcon icon = item.getIconIndex();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
            for (int x = 1; x < item.getItem().getRenderPasses(item.getItemDamage()); x++) {
                int k1 = item.getItem().getColorFromItemStack(item, x);
                float f10 = (float) (k1 >> 16 & 255) / 255.0F;
                float f11 = (float) (k1 >> 8 & 255) / 255.0F;
                float f12 = (float) (k1 & 255) / 255.0F;
                GL11.glColor4f(1.0F * f10, 1.0F * f11, 1.0F * f12, 1.0F);
                icon = item.getItem().getIcon(item, x);
                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
            }

            if (item.hasEffect(0)) {
                TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
                Tessellator tessellator=Tessellator.instance;

                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                texturemanager.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(768, 1, 1, 0);
                float f7 = 0.76F;
                GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float f8 = 0.125F;
                GL11.glScalef(f8, f8, f8);
                float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(f9, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f8, f8, f8);
                f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
        }
    }
}
