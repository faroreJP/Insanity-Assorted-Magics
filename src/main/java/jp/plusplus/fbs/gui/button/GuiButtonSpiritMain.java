package jp.plusplus.fbs.gui.button;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class GuiButtonSpiritMain extends GuiButton {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritMain.png");
    public String rowString;
    public String character;

    public GuiButtonSpiritMain(int id, int x, int y, String str) {
        this(id, x, y, str, "");
    }
    public GuiButtonSpiritMain(int id, int x, int y, String str, String character){
        super(id, x, y, 48, 16, StatCollector.translateToLocal("spirit.gui.fbs.button."+str));
        rowString=str;
        this.character=character;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            par1Minecraft.getTextureManager().bindTexture(rl);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean onMouse = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            int drawX = 176;
            int drawY = 0;

            if(!enabled) drawY=32;
            else if(onMouse) drawY=16;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, drawX, drawY, this.width, this.height);
            drawCenteredString(par1Minecraft.fontRenderer, displayString, xPosition + width / 2, yPosition + (this.height - 8) / 2, enabled? 0xffffff:0xa0a0a0);
        }
    }


    public void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
        if (!p_146283_1_.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int width = 0;
            Iterator iterator = p_146283_1_.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > width) {
                    width = l;
                }
            }

            int xMin = p_146283_2_ + 12;
            int yMin = p_146283_3_ - 12;
            int i1 = 8;

            if (p_146283_1_.size() > 1) {
                i1 += 2 + (p_146283_1_.size() - 1) * 10;
            }

            /*
            if (xMin + width > this.width) {
                xMin -= 28 + width;
            }

            if (yMin + i1 + 6 > this.height) {
                yMin = this.height - i1 - 6;
            }
            */


            //this.zLevel = 300.0F;
            //itemRender.zLevel = 300.0F;
            int color = -267386864;
            this.drawGradientRect(xMin - 3, yMin - 4, xMin + width + 3, yMin - 3, color, color);
            this.drawGradientRect(xMin - 3, yMin + i1 + 3, xMin + width + 3, yMin + i1 + 4, color, color);
            this.drawGradientRect(xMin - 3, yMin - 3, xMin + width + 3, yMin + i1 + 3, color, color);
            this.drawGradientRect(xMin - 4, yMin - 3, xMin - 3, yMin + i1 + 3, color, color);
            this.drawGradientRect(xMin + width + 3, yMin - 3, xMin + width + 4, yMin + i1 + 3, color, color);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            this.drawGradientRect(xMin - 3, yMin - 3 + 1, xMin - 3 + 1, yMin + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(xMin + width + 2, yMin - 3 + 1, xMin + width + 3, yMin + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(xMin - 3, yMin - 3, xMin + width + 3, yMin - 3 + 1, k1, k1);
            this.drawGradientRect(xMin - 3, yMin + i1 + 2, xMin + width + 3, yMin + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
                String s1 = (String) p_146283_1_.get(i2);
                font.drawStringWithShadow(s1, xMin, yMin, -1);

                if (i2 == 0) {
                    yMin += 2;
                }

                yMin += 10;
            }

            //this.zLevel = 0.0F;
            //itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}
