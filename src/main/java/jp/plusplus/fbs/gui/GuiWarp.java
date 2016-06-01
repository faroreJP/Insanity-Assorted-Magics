package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.ContainerWarp;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.gui.button.GuiButtonDestination;
import jp.plusplus.fbs.gui.button.GuiButtonWarp;
import jp.plusplus.fbs.packet.MessageGuiButtonDecide;
import jp.plusplus.fbs.packet.MessageGuiButtonWithString;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/10/22.
 */
public class GuiWarp extends GuiContainer {
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/magicWarp.png");
    public static final int PAGE_ITEM_MAX=11;
    protected ContainerWarp con;
    protected GuiButtonWarp buttonPrev;
    protected GuiButtonWarp buttonNext;
    protected GuiButtonWarp buttonDecide;
    protected GuiButtonWarp buttonRename;
    protected GuiButtonDestination[] buttons=new GuiButtonDestination[PAGE_ITEM_MAX];
    protected GuiTextField textField;
    protected int page;

    protected ArrayList<FBSEntityProperties.WarpPosition> destinations;

    public GuiWarp(ContainerWarp p_i1072_1_) {
        super(p_i1072_1_);
        con=p_i1072_1_;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;
        page=0;

        buttonPrev=new GuiButtonWarp(0, i+15, j+16, "");
        buttonNext=new GuiButtonWarp(1, i+115, j+16, "");
        buttonDecide=new GuiButtonWarp(2, i+128, j+16, "Decide");
        buttonDecide.enabled=false;
        buttonRename=new GuiButtonWarp(3, i+128, j+48, "Rename");
        buttonRename.enabled=false;

        buttonList.add(buttonPrev);
        buttonList.add(buttonNext);
        buttonList.add(buttonDecide);
        buttonList.add(buttonRename);

        for(int k=0;k<PAGE_ITEM_MAX;k++){
            buttons[k]=new GuiButtonDestination(4+k, i+27, j+18+12*k);
            buttonList.add(buttons[k]);
        }

        textField=new GuiTextField(fontRendererObj, i+118, j+34, 51, 11);
        textField.setTextColor(-1);
        textField.setDisabledTextColour(-1);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(40);

        destinations=FBSEntityProperties.get(con.player).getDestinations();
        changePage(0);
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        if(p_146284_1_==buttonPrev && page>1) changePage(-1);
        else if(p_146284_1_==buttonNext && (page+1)*PAGE_ITEM_MAX<=destinations.size()) changePage(1);

        int i=getSelectedIndex();
        if(p_146284_1_==buttonDecide){
            PacketHandler.INSTANCE.sendToServer(new MessageGuiButtonDecide(con.player, destinations.get(i+page*PAGE_ITEM_MAX)));
        }
        if(p_146284_1_==buttonRename && i!=-1){
            if(i!=-1){
                PacketHandler.INSTANCE.sendToServer(new MessageGuiButtonWithString(i, textField.getText()));
                destinations.get(i).setName(textField.getText());
                buttons[i].setDisplayString(textField.getText());
                textField.setText("");
            }
        }
        if(p_146284_1_ instanceof GuiButtonDestination){
            for(int k=0;k<PAGE_ITEM_MAX;k++){
                buttons[k].selected=false;
            }
            ((GuiButtonDestination)p_146284_1_).selected=true;
            buttonDecide.enabled=true;
            buttonRename.enabled=false;
            textField.setText("");
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
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
        else{
            buttonRename.enabled=!textField.getText().isEmpty();
        }
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        textField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        super.drawGuiContainerForegroundLayer(par1, par2);
        String s= StatCollector.translateToLocal("book.fbs.warp.title");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        //this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(rl);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    protected int getSelectedIndex(){
        for(int i=0;i<PAGE_ITEM_MAX;i++){
            if(buttons[i].selected) return i;
        }
        return -1;
    }
    protected void changePage(int add){
        page+=add;
        for(int i=0;i<PAGE_ITEM_MAX;i++){
            buttons[i].enabled=false;
            buttons[i].selected=false;
        }
        for(int i=0;i<PAGE_ITEM_MAX && page*PAGE_ITEM_MAX+i<destinations.size();i++){
            buttons[i].enabled=true;
            buttons[i].setDisplayString(destinations.get(page*PAGE_ITEM_MAX+i).getName());
        }
        buttonDecide.enabled=false;
        buttonRename.enabled=false;
        buttonPrev.enabled=(page>0);
        buttonNext.enabled=((page+1)*PAGE_ITEM_MAX<destinations.size());
    }
}
