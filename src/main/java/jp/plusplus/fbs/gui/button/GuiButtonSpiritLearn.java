package jp.plusplus.fbs.gui.button;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.spirit.SkillManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/11/28.
 */
public class GuiButtonSpiritLearn extends GuiButton {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritConfig.png");

    public String[] skillStr=new String[0];
    public String skillName="";

    public GuiButtonSpiritLearn(int id, int x, int y, String str) {
        super(id, x, y, 34, 16, StatCollector.translateToLocal("spirit.gui.fbs.button."+str));
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            par1Minecraft.getTextureManager().bindTexture(rl);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean onMouse = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;

            int drawX = 186;
            int drawY = 48;

            if(!enabled) drawY+=32;
            else if(onMouse) drawY+=16;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, drawX, drawY, this.width, this.height);
            drawCenteredString(par1Minecraft.fontRenderer, displayString, xPosition + width / 2, yPosition + (this.height - 8) / 2, enabled? 0xffffff:0xa0a0a0);

            for(int i=0;i<skillStr.length;i++){
                par1Minecraft.fontRenderer.drawString(skillStr[i], xPosition - 120, yPosition + (this.height - 8) / 2-3+par1Minecraft.fontRenderer.FONT_HEIGHT*i, 0x404040);
            }
        }
    }

    public void setSkill(SkillManager.SkillEntry entry, int lv) {
        skillStr=entry.getString(lv).split("\n");
        skillName=SkillManager.getSkill(entry.getSkillId()).getName();
    }
}
