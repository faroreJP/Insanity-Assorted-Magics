package jp.plusplus.fbs.gui.button;

import jp.plusplus.fbs.spirit.SkillManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/11/29.
 */
public class GuiButtonSpiritLearnDummy extends GuiButtonSpiritLearn {
    public GuiButtonSpiritLearnDummy(int id, int x, int y, String str) {
        super(id, x, y, str);
    }

    @Override
    public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            par1Minecraft.fontRenderer.drawString(skillStr[0], xPosition - 120, yPosition + (this.height - 8) / 2 -3, 0x404040);
        }
    }

    public void setSkill(SkillManager.SkillData data) {
        skillStr=new String[]{data.getString()};
        skillName=data.getSkill().getName();
    }
    @Override
    public void setSkill(SkillManager.SkillEntry entry, int lv){

    }
}
