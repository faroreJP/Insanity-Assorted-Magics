package jp.plusplus.fbs.gui.button;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/10/21.
 */
public class GuiButtonEnchantment extends GuiButton {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/enchant.png");

    public GuiButtonEnchantment(int id, int x, int y) {
        super(id, x, y, 28, 23, "");
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            par1Minecraft.getTextureManager().bindTexture(rl);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean onMouse = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            int drawX = 176;
            int drawY = 0;

            if (!this.enabled) drawY += height * 2;
            else if (onMouse) drawY += height;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, drawX, drawY, this.width, this.height);
        }
    }
}
