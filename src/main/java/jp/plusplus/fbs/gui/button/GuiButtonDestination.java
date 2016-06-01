package jp.plusplus.fbs.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/10/22.
 */
public class GuiButtonDestination extends GuiButton {
    public boolean selected;

    public GuiButtonDestination(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_) {
        super(p_i1020_1_, p_i1020_2_, p_i1020_3_,86 ,20, "");
        enabled=false;
    }

    public void setDisplayString(String str){
        displayString=str;
        enabled=!str.isEmpty();
    }

    @Override
    public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
        if (this.enabled && !displayString.isEmpty()) {
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            this.mouseDragged(p_146112_1_, p_146112_2_, p_146112_3_);
            int col=selected?0xffffff:0x404040;

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 16) / 2, col);
        }
    }
}
