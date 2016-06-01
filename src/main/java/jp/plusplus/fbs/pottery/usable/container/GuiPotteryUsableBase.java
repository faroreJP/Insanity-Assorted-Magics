package jp.plusplus.fbs.pottery.usable.container;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class GuiPotteryUsableBase extends GuiContainer {
    private static final ResourceLocation field_147017_u = new ResourceLocation("textures/gui/container/generic_54.png");
    private ContainerPotteryUsableBase container;

    public GuiPotteryUsableBase(ContainerPotteryUsableBase p_i1072_1_) {
        super(p_i1072_1_);
        this.container=p_i1072_1_;
        this.allowUserInput = false;
        short short1 = 222;
        int i = short1 - 108;
        this.ySize = i + container.inventoryRows * 18;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.fontRendererObj.drawString(container.inventory.getInventoryName(), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147017_u);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, container.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(k, l + container.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
