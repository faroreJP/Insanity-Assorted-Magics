package jp.plusplus.fbs.storage;

import jp.plusplus.fbs.FBS;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class GuiMealTerminal extends GuiContainer {
    private static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/terminal.png");

    private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private GuiTextField searchField;
    private boolean field_147057_D;

    public GuiMealTerminal(Container p_i1072_1_) {
        super(p_i1072_1_);
        this.allowUserInput = true;
        this.xSize = 195;
        this.ySize = 222;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.searchField = new GuiTextField(this.fontRendererObj, this.guiLeft + 80, this.guiTop + 5, 89, this.fontRendererObj.FONT_HEIGHT);
        this.searchField.setMaxStringLength(15);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setVisible(false);
        this.searchField.setTextColor(16777215);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if (this.field_147057_D) {
            this.field_147057_D = false;
            this.searchField.setText("");
        }

        if (!this.checkHotbarKeys(p_73869_2_)) {
            if (this.searchField.textboxKeyTyped(p_73869_1_, p_73869_2_)) {
                this.updateSearch();
            } else {
                super.keyTyped(p_73869_1_, p_73869_2_);
            }
        }
    }

    protected void updateSearch(){

    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if (i != 0) {
            ContainerMealTerminal cmt=(ContainerMealTerminal)inventorySlots;
            int j = cmt.inv.allItem.length / 9 - 5 + 1;

            if (i > 0) i = 1;
            if (i < 0) i = -1;

            this.currentScroll = (float) ((double) this.currentScroll - (double) i / (double) j);
            if (this.currentScroll < 0.0F) this.currentScroll = 0.0F;
            if (this.currentScroll > 1.0F) this.currentScroll = 1.0F;

            cmt.scrollTo(currentScroll);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(rl);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        //GuiContainerCreative
        this.drawTexturedModalRect(k+175, l+18+ (int)((196.f-15.f) * this.currentScroll), 232, 0, 12, 15);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        GL11.glDisable(GL11.GL_BLEND);
        this.fontRendererObj.drawString(I18n.format("tile.fbs.mealCrystal.name"), 8, 6, 4210752);
    }


    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        boolean flag = Mouse.isButtonDown(0);
        int k = this.guiLeft;
        int l = this.guiTop;
        int i1 = k + 175;
        int j1 = l + 18;
        int k1 = i1 + 14;
        int l1 = j1 + 196;

        ContainerMealTerminal cmt=(ContainerMealTerminal)inventorySlots;

        if (!this.wasClicking && flag && p_73863_1_ >= i1 && p_73863_2_ >= j1 && p_73863_1_ < k1 && p_73863_2_ < l1) {
            this.isScrolling = true;
        }

        if (!flag) {
            this.isScrolling = false;
        }

        this.wasClicking = flag;

        if (this.isScrolling) {
            this.currentScroll = ((float) (p_73863_2_ - j1) - 7.5F) / ((float) (l1 - j1) - 15.0F);

            if (this.currentScroll < 0.0F) this.currentScroll = 0.0F;
            if (this.currentScroll > 1.0F) this.currentScroll = 1.0F;

            cmt.scrollTo(this.currentScroll);
        }

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
    }
}
