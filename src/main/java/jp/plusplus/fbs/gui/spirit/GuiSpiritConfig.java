package jp.plusplus.fbs.gui.spirit;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.container.spirit.ContainerSpiritMain;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritCheckBox;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritMain;
import jp.plusplus.fbs.packet.MessageGuiButton;
import jp.plusplus.fbs.packet.MessageGuiButtonWithNBT;
import jp.plusplus.fbs.packet.MessageGuiButtonWithString;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class GuiSpiritConfig extends GuiContainer{
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritConfig.png");

    public ItemStack tool;
    public SpiritStatus status;
    public SpiritStatus.Configuration configuration;
    public ISpiritTool spiritTool;

    private GuiButtonSpiritMain ok;
    private GuiButtonSpiritMain cancel;
    private ArrayList<GuiButtonSpiritCheckBox> checks=new ArrayList<GuiButtonSpiritCheckBox>();

    public GuiSpiritConfig(EntityPlayer player) {
        super(new ContainerSpiritMain(player, 1));
        tool=player.getCurrentEquippedItem();
        status=SpiritStatus.readFromNBT(tool.getTagCompound());
        configuration=status.getConfiguration();
        spiritTool=(ISpiritTool)tool.getItem();
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;

        ok=new GuiButtonSpiritMain(0, i+64, j+116, "ok");
        buttonList.add(ok);
        cancel=new GuiButtonSpiritMain(1, i+116, j+116, "cancel");
        buttonList.add(cancel);

        int count=0;
        for(String key : configuration.getKeys()){
            GuiButtonSpiritCheckBox b=new GuiButtonSpiritCheckBox(count+2, i+6, j+25+11*count, key);
            b.checked=configuration.get(key);
            checks.add(b);
            buttonList.add(b);
            count++;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        if(p_146284_1_ instanceof GuiButtonSpiritMain){
            NBTTagCompound nbt=new NBTTagCompound();
            configuration.writeToNBT(nbt);
            PacketHandler.INSTANCE.sendToServer(new MessageGuiButtonWithNBT(p_146284_1_.id, nbt));

            if(p_146284_1_.id==0){
                NBTTagCompound nbt1=new NBTTagCompound();
                SpiritStatus.writeToNBT(status, nbt1);
                tool.setTagCompound(nbt1);
            }
        }
        else if(p_146284_1_ instanceof GuiButtonSpiritCheckBox) {
            GuiButtonSpiritCheckBox b = ((GuiButtonSpiritCheckBox) p_146284_1_);
            b.checked = !b.checked;
            configuration.update(b.keyString, b.checked);
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
    protected void drawGuiContainerForegroundLayer(int par1, int par2){}
}
