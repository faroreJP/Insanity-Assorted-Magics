package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerBonfire;
import jp.plusplus.fbs.container.ContainerMagic;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Created by pluslus_F on 2015/10/19.
 */
public class GuiBonfire extends GuiContainer {
    public static final ResourceLocation rl=new ResourceLocation(FBS.MODID, "textures/gui/bonfire.png");
    ContainerBonfire cm;

    public GuiBonfire(ContainerBonfire p_i1072_1_) {
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

        this.mc.getTextureManager().bindTexture(rl);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

}
