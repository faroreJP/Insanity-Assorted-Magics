package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerContract;
import jp.plusplus.fbs.container.ContainerTFKEnchantment;
import jp.plusplus.fbs.gui.button.GuiButtonEnchantment;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.packet.MessageGuiButton;
import jp.plusplus.fbs.packet.MessageGuiButtonWithString;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class GuiContract extends GuiContainer {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/magicContract.png");
    public ContainerContract con;
    public GuiButtonEnchantment button;
    public GuiTextField textField;

    public GuiContract(ContainerContract p_i1072_1_) {
        super(p_i1072_1_);
        con=p_i1072_1_;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;
        Keyboard.enableRepeatEvents(true);

        button=new GuiButtonEnchantment(0, i+92, j+40);
        this.buttonList.add(button);

        textField=new GuiTextField(fontRendererObj, i+9, j+34, 68, 9);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(20);
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        super.drawGuiContainerForegroundLayer(par1, par2);
        String s= StatCollector.translateToLocal(con.inventory.getInventoryName());
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

    @Override
    public void updateScreen() {
        super.updateScreen();
        button.enabled=((ContainerContract)inventorySlots).canContract() && !textField.getText().isEmpty();
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        if(p_146284_1_==button && p_146284_1_.enabled){
            PacketHandler.INSTANCE.sendToServer(new MessageGuiButtonWithString(0, textField.getText()));
            textField.setText("");
        }
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        textField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if(!textField.textboxKeyTyped(p_73869_1_, p_73869_2_)){
            super.keyTyped(p_73869_1_, p_73869_2_);
        }
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        textField.drawTextBox();
    }
}
