package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.tileentity.TileEntityFBSWorkbench;
import jp.plusplus.fbs.tileentity.TileEntityFillingTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class GuiWorkbench extends GuiContainer {
    private TileEntityFBSWorkbench entity;

    public GuiWorkbench(Container p_i1072_1_, TileEntityFBSWorkbench t) {
        super(p_i1072_1_);
        entity =t;
        ySize=185;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s=entity.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);

        /*
        if(!entity.tank.isEmpty()){
            this.fontRendererObj.drawString(entity.tank.getFluidName()+entity.tank.getFluidAmount()+"mb", 8, this.ySize - 116 + 2, 0x404040);
        }
        */
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(new ResourceLocation(FBS.MODID, "textures/gui/crafting.png"));

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int amount = entity.tank.getFluidAmount();
        if (amount > 0) {
            IIcon icon = entity.getFluidIcon();
            if (icon != null) {
                int scale = 16 * amount / entity.tank.getCapacity();
                GuiExtractingFurnace.drawFluid(this, k + 30, l + 87 - scale, icon, 16, scale);
            }
        }
    }
}
