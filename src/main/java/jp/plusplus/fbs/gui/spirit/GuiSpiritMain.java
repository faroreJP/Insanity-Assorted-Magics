package jp.plusplus.fbs.gui.spirit;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.ProxyClient;
import jp.plusplus.fbs.container.ContainerAlchemyCauldron;
import jp.plusplus.fbs.container.spirit.ContainerSpiritMain;
import jp.plusplus.fbs.entity.EntityLivingDummy;
import jp.plusplus.fbs.gui.button.GuiButtonEnchantment;
import jp.plusplus.fbs.gui.button.GuiButtonSpiritMain;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.packet.MessageGuiButton;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import jp.plusplus.fbs.tileentity.TileEntityAlchemyCauldron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class GuiSpiritMain extends GuiContainer{
    public static final ResourceLocation rl = new ResourceLocation(FBS.MODID+":textures/gui/spiritMain.png");

    public EntityPlayer player;
    public ItemStack tool;
    public SpiritStatus status;
    public ISpiritTool spiritTool;

    public RendererLivingEntity spiritRenderer;
    public EntityLivingBase dummy;

    private GuiButtonSpiritMain skill;
    private GuiButtonSpiritMain learn;
    private GuiButtonSpiritMain bless;
    private GuiButtonSpiritMain repair;
    private GuiButtonSpiritMain summon;
    private GuiButtonSpiritMain config;
    private float xSizeFloat;
    private float ySizeFloat;

    public GuiSpiritMain(EntityPlayer player) {
        super(new ContainerSpiritMain(player, 0));
        this.player=player;
        tool=player.getCurrentEquippedItem();
        status=SpiritStatus.readFromNBT(tool.getTagCompound());
        spiritTool=(ISpiritTool)tool.getItem();

        spiritRenderer=((ProxyClient)FBS.proxy).getSpiritModel(status.getCharacter());
        dummy=new EntityLivingDummy(FBS.proxy.getClientWorld());
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.allowUserInput = true;

        skill=new GuiButtonSpiritMain(0, i+12, j+96, "skill", status.getCharacter());
        buttonList.add(skill);
        learn=new GuiButtonSpiritMain(1, i+12, j+116, "learn", status.getCharacter());
        buttonList.add(learn);
        bless=new GuiButtonSpiritMain(2, i+64, j+96, "bless", status.getCharacter());
        buttonList.add(bless);
        repair=new GuiButtonSpiritMain(3, i+64, j+116, "repair", status.getCharacter());
        buttonList.add(repair);
        summon=new GuiButtonSpiritMain(4, i+116, j+96, "summon", status.getCharacter());
        buttonList.add(summon);
        config=new GuiButtonSpiritMain(5, i+116, j+116, "config", status.getCharacter());
        buttonList.add(config);

        bless.enabled=summon.enabled=false;
        repair.enabled=status.getItemDamage()>0 && player.inventory.hasItemStack(new ItemStack(ItemCore.alchemyPotion, 1, 2));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        bless.enabled=(player.worldObj.getCurrentDate().get(Calendar.DATE)!=status.getLastBlessDate());

    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        this.xSizeFloat = (float) p_73863_1_;
        this.ySizeFloat = (float) p_73863_2_;
    }

    @Override
    protected void actionPerformed(GuiButton p_146284_1_) {
        PacketHandler.INSTANCE.sendToServer(new MessageGuiButton(p_146284_1_.id));
        if(p_146284_1_.id==2){
            status.updateLastBlessDate(player.worldObj.getCurrentDate().get(Calendar.DATE));

            NBTTagCompound nbt=new NBTTagCompound();
            SpiritStatus.writeToNBT(status, nbt);
            tool.setTagCompound(nbt);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(rl);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        /*
        if(spiritRenderer!=null){
            renderSpirit(k + 33, l + 75, 30, (float)(k + 33) - this.xSizeFloat, (float)(l + 75 - 50) - this.ySizeFloat);
        }
        */
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y){
        int rowHeight=10;
        String str="";

        str="Lv."+status.getLv()+" "+tool.getDisplayName();
        this.fontRendererObj.drawString(str, 64, rowHeight*0+7, 0x404040);

        this.fontRendererObj.drawString("[Status]", 64, rowHeight*1+7, 0x404040);

        str= StatCollector.translateToLocal("spirit.gui.fbs.sp")+":"+status.getSkillPoint();
        this.fontRendererObj.drawString(str, 64, rowHeight*2+7, 0x404040);

        str= StatCollector.translateToLocal("spirit.gui.fbs.str")+":"+status.getStrength();
        str+=" "+StatCollector.translateToLocal("spirit.gui.fbs.tou")+":"+status.getToughness();
        this.fontRendererObj.drawString(str, 64, rowHeight*3+7, 0x404040);

        this.fontRendererObj.drawString("[Tool]", 64, rowHeight*4+7, 0x404040);

        str= String.format(StatCollector.translateToLocal("attribute.name.generic.attackDamage")+":%.1f", 0.5+spiritTool.calcDamage(status));
        this.fontRendererObj.drawString(str, 64, rowHeight*5+7, 0x404040);

        int t=spiritTool.calcDurable(status);
        str= StatCollector.translateToLocal("spirit.gui.fbs.dur")+":"+(t-status.getItemDamage())+"/"+t;
        this.fontRendererObj.drawString(str, 64, rowHeight*6+7, 0x404040);


        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        for(Object obj : buttonList) {
            if (obj instanceof GuiButtonSpiritMain) {
                GuiButtonSpiritMain b = (GuiButtonSpiritMain) obj;
                boolean onMouse = x >= b.xPosition && y >= b.yPosition && x < b.xPosition + b.width && y < b.yPosition + b.height;

                if(!b.character.isEmpty() && onMouse){
                    //説明文の表示
                    List list=new ArrayList();
                    for(int k=0;k<3;k++){
                        String s="spirit.gui.button."+b.rowString+"."+b.character+"."+k;
                        if(k>0 && !StatCollector.canTranslate(s)){
                            break;
                        }
                        list.add(SpiritManager.translateString(s, status));
                    }
                    b.drawHoveringText(list, x-i, y-j, Minecraft.getMinecraft().fontRenderer);
                    break;
                }
            }
        }
    }

    public void renderSpirit(int x, int y, int scale, float rotYaw, float rotPitch)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, 50.0F);
        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = dummy.renderYawOffset;
        float f3 = dummy.rotationYaw;
        float f4 = dummy.rotationPitch;
        float f5 = dummy.prevRotationYawHead;
        float f6 = dummy.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(rotPitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        dummy.renderYawOffset = (float)Math.atan((double)(rotYaw / 40.0F)) * 20.0F;
        dummy.rotationYaw = (float)Math.atan((double)(rotYaw / 40.0F)) * 40.0F;
        dummy.rotationPitch = -((float)Math.atan((double)(rotPitch / 40.0F))) * 20.0F;
        dummy.rotationYawHead = dummy.rotationYaw;
        dummy.prevRotationYawHead = dummy.rotationYaw;
        GL11.glTranslatef(0.0F, dummy.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        spiritRenderer.doRender(dummy, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        dummy.renderYawOffset = f2;
        dummy.rotationYaw = f3;
        dummy.rotationPitch = f4;
        dummy.prevRotationYawHead = f5;
        dummy.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
