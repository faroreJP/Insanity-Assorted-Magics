package jp.plusplus.fbs.gui.spirit;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.spirit.ContainerSpiritLearn;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritArrow;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritLearn;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritLearnDummy;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritMain;
import jp.plusplus.fbs.packet.MessageGuiButton;
import jp.plusplus.fbs.packet.MessageGuiButtonWithString;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SkillManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class GuiSpiritSkill extends GuiContainer{
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritConfig.png");
    public static final int PAGE_ITEM_MAX=5;

    public ItemStack tool;
    public SpiritStatus status;
    public ISpiritTool spiritTool;

    private GuiButtonSpiritMain ok;
    private GuiButtonSpiritArrow prev;
    private GuiButtonSpiritArrow next;
    private GuiButtonSpiritLearnDummy[] buttons;

    private ArrayList<SkillManager.SkillData> skillDatas;
    private int page;

    public GuiSpiritSkill(EntityPlayer player) {
        super(new ContainerSpiritLearn(player));
        tool=player.getCurrentEquippedItem();
        status=SpiritStatus.readFromNBT(tool.getTagCompound());
        spiritTool=(ISpiritTool)tool.getItem();
        skillDatas =status.getSkills();
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;

        prev=new GuiButtonSpiritArrow(0, i+144, j+6);
        buttonList.add(prev);
        next=new GuiButtonSpiritArrow(1, i+154, j+6);
        buttonList.add(next);

        ok =new GuiButtonSpiritMain(2, i+116, j+122, "ok");
        buttonList.add(ok);

        buttons =new GuiButtonSpiritLearnDummy[PAGE_ITEM_MAX];
        for(int k=0;k<buttons.length;k++){
            buttons[k]=new GuiButtonSpiritLearnDummy(3+k, i+130, j+25+19*k, "");
            buttonList.add(buttons[k]);
        }
        page=0;
        changePage(0);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        if(p_146284_1_==ok){
            PacketHandler.INSTANCE.sendToServer(new MessageGuiButton(p_146284_1_.id));
        }
        else if(p_146284_1_==prev){
            changePage(-1);
        }
        else if(p_146284_1_==next){
            changePage(1);
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
    protected void drawGuiContainerForegroundLayer(int par1, int par2){
        String s= StatCollector.translateToLocal("spirit.gui.fbs.sp")+":"+status.getSkillPoint();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
    }

    protected void changePage(int add){
        page+=add;
        for(int i=0;i<PAGE_ITEM_MAX;i++){
            buttons[i].enabled=false;
            buttons[i].visible=false;
        }

        int size= skillDatas.size();
        for(int i=0;i<PAGE_ITEM_MAX && page*PAGE_ITEM_MAX+i<size;i++){
            SkillManager.SkillData e= skillDatas.get(i+page * PAGE_ITEM_MAX);
            buttons[i].setSkill(e);
            buttons[i].visible=true;
        }
        prev.enabled=(page>0);
        next.enabled=((page+1)*PAGE_ITEM_MAX<size);
    }
}
