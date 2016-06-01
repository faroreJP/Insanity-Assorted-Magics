package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerStaff;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class GuiStaff extends GuiContainer {
    private static final ResourceLocation[] gui1 = {
            new ResourceLocation(FBS.MODID + ":textures/gui/staff.png"),
            new ResourceLocation(FBS.MODID + ":textures/gui/staffResona.png")
    };
    private ItemStack cItem;

    public GuiStaff(InventoryPlayer inventoryPlayer) {
        super(new ContainerStaff(inventoryPlayer));
        cItem=inventoryPlayer.getCurrentItem();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int p_146979_2_) {
        String s=I18n.format(cItem.getItem().getUnlocalizedName() + ".name");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        ContainerStaff cs=(ContainerStaff)inventorySlots;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(gui1[cs.inventory.bookNum-1]);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int gn=cs.inventory.gemNum;
        for(int i=0;i<5-gn;i++){
            this.drawTexturedModalRect(k+116-18*i, l+53, 176, 0, 16, 16);
        }
    }
}
