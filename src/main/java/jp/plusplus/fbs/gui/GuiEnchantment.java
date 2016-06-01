package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerTFKEnchantment;
import jp.plusplus.fbs.gui.button.GuiButtonEnchantment;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.packet.MessageGuiButton;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/10/21.
 */
public class GuiEnchantment extends GuiContainer {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/enchant.png");
    public GuiButtonEnchantment button;
    public ContainerTFKEnchantment con;

    public GuiEnchantment(ContainerTFKEnchantment p_i1072_1_) {
        super(p_i1072_1_);
        con=p_i1072_1_;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;
        button=new GuiButtonEnchantment(0, i+83, j+28);
        this.buttonList.add(button);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        super.drawGuiContainerForegroundLayer(par1, par2);
        String s= StatCollector.translateToLocal(ItemCore.enchantScroll.getUnlocalizedName()+".name");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);

        //エンチャント成功率
        if(con.canEnchant()) {
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            float p = con.getEnchantProbability() * 100;
            if (p < 0) p = 0;
            if (p > 100) p = 100;
            this.fontRendererObj.drawString(String.format("%.2f%%", p), 85, 50, 0x404040);
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
    public void updateScreen() {
        super.updateScreen();
        button.enabled=((ContainerTFKEnchantment)inventorySlots).canEnchant();
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        if(p_146284_1_==button){
            PacketHandler.INSTANCE.sendToServer(new MessageGuiButton(0));
        }
    }
}
