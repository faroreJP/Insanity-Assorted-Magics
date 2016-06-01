package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class GuiExtractingFurnace extends GuiContainer {
    public static final ResourceLocation rl=new ResourceLocation(FBS.MODID, "textures/gui/extractingFurnace.png");
    private TileEntityExtractingFurnace entity;

    public GuiExtractingFurnace(Container p_i1072_1_, TileEntityExtractingFurnace t) {
        super(p_i1072_1_);
        entity =t;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s=entity.getInventoryName();
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
        this.drawTexturedModalRect(k+68, l+35, 176, 0, entity.getProgressScaled(22), 15);

        int amount = entity.tank.getFluidAmount();
        if (amount > 0) {
            IIcon icon = entity.getFluidIcon();
            if (icon != null) {
                int scale = 52 * amount / entity.tank.getCapacity();
                drawFluid(this, k + 98, l + 69 - scale, icon, 34, scale);
            }
        }
    }

    public static void drawFluid(GuiContainer gui, int x, int y, IIcon icon, int w, int h) {
        if(h==0) h=1;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
        gui.mc.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        int sx, sy;
        for (sy = 0; h - sy * 16 > 16; sy++) {
            for (sx = 0; w - sx * 16 > 16; sx++) {
                gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, 16);
            }
            gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, 16);
        }
        for (sx = 0; w - sx * 16 > 16; sx++) {
            gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, h - sy * 16);
        }
        gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, h - sy * 16);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
