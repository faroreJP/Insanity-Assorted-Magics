package jp.plusplus.fbs.gui.button;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/10/22.
 */
public class GuiButtonSpiritArrow extends GuiButton {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritConfig.png");
    public boolean selected;

    public GuiButtonSpiritArrow(int id, int x, int y) {
        super(id, x, y, 10, 15, "");
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            par1Minecraft.getTextureManager().bindTexture(rl);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean onMouse = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            int drawX = 176;
            int drawY = 15*id;

            if(id<2){
                if (!this.enabled) drawX += width * 2;
                else if (onMouse) drawX += width;
            }
            else{
                if (!this.enabled) drawY += height * 2;
                else if (onMouse) drawY += height;
            }

            //selected?0xffffff:0x404040
            this.drawTexturedModalRect(this.xPosition, this.yPosition, drawX, drawY, this.width, this.height);
        }
    }
}
