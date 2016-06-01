package jp.plusplus.fbs.gui.button;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class GuiButtonSpiritCheckBox extends GuiButton {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritConfig.png");
    public boolean checked;
    public String keyString;

    public GuiButtonSpiritCheckBox(int id, int x, int y, String str) {
        super(id, x, y, 10, 10, StatCollector.translateToLocal("spirit.config.fbs."+str));
        checked=false;
        keyString=str;
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            par1Minecraft.getTextureManager().bindTexture(rl);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int drawX = 176;
            int drawY = 48;

            if(checked) drawY+=10;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, drawX, drawY, this.width, this.height);
            par1Minecraft.fontRenderer.drawString(displayString, xPosition + 11, yPosition + 1, 0x404040);
            //drawString(par1Minecraft.fontRenderer, displayString, xPosition+11, yPosition+1, 0x404040);
        }
    }
}
