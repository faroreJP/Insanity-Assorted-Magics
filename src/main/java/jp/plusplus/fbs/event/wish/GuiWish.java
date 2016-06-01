package jp.plusplus.fbs.event.wish;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerContract;
import jp.plusplus.fbs.gui.button.GuiButtonEnchantment;
import jp.plusplus.fbs.packet.MessageGuiButtonWithString;
import jp.plusplus.fbs.packet.MessageWish;
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
 * Created by plusplus_F on 2015/03/31.
 */
public class GuiWish extends GuiContainer {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/wish.png");
    public GuiTextField textField;

    public GuiWish() {
        super(new ContainerWish());
        this.ySize=37;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;
        Keyboard.enableRepeatEvents(true);

        textField=new GuiTextField(fontRendererObj, i+9, j+17, 159, 9);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(40);
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        PacketHandler.INSTANCE.sendToServer(new MessageWish(textField.getText()));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        super.drawGuiContainerForegroundLayer(par1, par2);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("info.fbs.wish.0"), 6, 4, 0x404040);
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
