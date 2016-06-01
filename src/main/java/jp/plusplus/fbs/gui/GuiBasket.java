package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerBasket;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/11/11.
 */
public class GuiBasket extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(FBS.MODID, "textures/gui/basket.png");

    public GuiBasket(InventoryPlayer inventoryPlayer) {
        super(new ContainerBasket(inventoryPlayer));
        this.ySize = 222;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int p_146979_2_) {
        String s = StatCollector.translateToLocal(ItemCore.basket.getUnlocalizedName() + ".name");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
