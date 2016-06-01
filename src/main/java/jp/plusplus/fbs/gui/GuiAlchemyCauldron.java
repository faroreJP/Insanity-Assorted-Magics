package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerAlchemyCauldron;
import jp.plusplus.fbs.gui.button.GuiButtonEnchantment;
import jp.plusplus.fbs.packet.MessageGuiButton;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.tileentity.TileEntityAlchemyCauldron;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Createdby pluslus_Fon 2015/06/08.
 */
public class GuiAlchemyCauldron extends GuiContainer {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID, "textures/gui/alchemyCauldron.png");
    private TileEntityAlchemyCauldron entity;
    private GuiButtonEnchantment button;

    public GuiAlchemyCauldron(Container p_i1072_1_, TileEntityAlchemyCauldron t) {
        super(p_i1072_1_);
        entity =t;
        ySize=201;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;
        button=new GuiButtonEnchantment(0, i+99, j+36);
        buttonList.add(button);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        button.enabled=((ContainerAlchemyCauldron)inventorySlots).entity.canCompounding();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s=entity.getInventoryName();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if(entity.cachedRecipe!=null){
            if(!entity.materials.isEmpty()) {
                this.fontRendererObj.drawString(entity.cachedRecipe.getProduct().getDisplayName(), 9, 37, 0x404040);
                for (int i = 0; i < entity.materials.size(); i++) {
                    TileEntityAlchemyCauldron.MaterialPair p = entity.materials.get(i);
                    this.fontRendererObj.drawString(p.getName(), 9, 48 + (fontRendererObj.FONT_HEIGHT + 1) * i, p.get() ? 0xffffff : 0x404040);
                }
            }
            float p=entity.getCompoundingProbability(((ContainerAlchemyCauldron) inventorySlots).player)*100;
            fontRendererObj.drawString(String.format("%.2f%%", p), 102, 60, 0x404040);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(rl);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        if(p_146284_1_==button){
            entity.inputMaterial.clear();
            for(TileEntityAlchemyCauldron.MaterialPair mp : entity.materials){
                mp.set(false);
            }
            PacketHandler.INSTANCE.sendToServer(new MessageGuiButton(0));
        }
    }
}
