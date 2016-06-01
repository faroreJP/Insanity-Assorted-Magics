package jp.plusplus.fbs.pottery;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class GuiKiln extends GuiContainer {
    private TileEntityKiln entity;

    public GuiKiln(Container p_i1072_1_, TileEntityKiln t) {
        super(p_i1072_1_);
        entity =t;
        ySize=197;
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

        this.mc.getTextureManager().bindTexture(new ResourceLocation(FBS.MODID, "textures/gui/kiln.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (entity.isBurning()) {
            int ss = entity.getBurnTimeRemainingScaled(13);
            this.drawTexturedModalRect(k + 81, l + 77 + 12 - ss, 176, 15+12 - ss, 14, ss + 1);

            for(int n=0;n<9;n++){
                if(entity.progress[n]>0){
                    ss=entity.getCookProgressScaled(n, 14);
                    this.drawTexturedModalRect(k+8+18*n, l+41+14-ss, 176, 14-ss, 16, ss+1);
                }
            }
        }
    }
}
