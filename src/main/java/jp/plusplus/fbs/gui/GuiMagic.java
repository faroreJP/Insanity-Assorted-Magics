package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerMagic;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class GuiMagic extends GuiContainer {
    ContainerMagic cm;

    public GuiMagic(ContainerMagic p_i1072_1_) {
        super(p_i1072_1_);
        cm=p_i1072_1_;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s=cm.inventory.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(cm.inventory.getResource());

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k+cm.inventory.getProgressX(), l+cm.inventory.getProgressY(), 176, 0, cm.inventory.getProgressScaled(22), 15);
    }
}
