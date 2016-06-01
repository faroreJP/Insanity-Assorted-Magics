package jp.plusplus.fbs.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.RecipeBladeSpice;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.spirit.ISpiritTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class RendererGameOverlay {
    public static final ResourceLocation icons = new ResourceLocation(FBS.MODID+":textures/gui/san.png");
    public static Minecraft mc = FMLClientHandler.instance().getClient();

    @SideOnly(Side.CLIENT)
    private static int Count = 0;
    @SideOnly(Side.CLIENT)
    public static boolean renderHUD = false;
    @SideOnly(Side.CLIENT)
    public static boolean renderAria=false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.FOOD && mc.playerController.shouldDrawHUD()) {
            renderHUD = true;
        }
        if(event.type==RenderGameOverlayEvent.ElementType.HOTBAR) renderAria=true;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        int width = event.resolution.getScaledWidth();
        int height = event.resolution.getScaledHeight();

        if (renderHUD) {
            renderHUD = false;

            mc.mcProfiler.startSection(FBS.MODID+"-san");
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(icons);

            int x=width/2-91;
            int y=height - GuiIngameForge.left_height;
            GuiIngameForge.left_height += 10;

            //san
            drawTexturedModalRect(x, y, 0, 0, 24, 8);
            if(mc.thePlayer!=null){
                FBSEntityProperties fbsep=FBSEntityProperties.get(mc.thePlayer);
                int s=fbsep.getSanity();
                int ms=fbsep.get(mc.thePlayer).getMaxSanity();

                for(int i=0;i==0 || s>0;i++){
                    drawTexturedModalRect(x+24+8*(2-i), y, 8 * (s%10), 8, 8, 8);
                    s/=10;
                }
                drawTexturedModalRect(x+24+8*3, y, 24, 0, 8, 8);
                for(int i=0;i==0 || ms>0;i++){
                    drawTexturedModalRect(x+24+8*(5-i), y, 8 * (ms%10), 8, 8, 8);
                    ms/=10;
                }
            }
            mc.mcProfiler.endSection();
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(Gui.icons);
        }

        if(renderAria){
            renderAria = false;

            mc.mcProfiler.startSection(FBS.MODID+"-aria");
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(icons);

            //詠唱時間の描画
            if(mc.thePlayer.isUsingItem()){
                ItemStack itemStack=mc.thePlayer.getCurrentEquippedItem();
                if(itemStack!=null && itemStack.getItem() instanceof ItemBookSorcery){
                    int baseX=width/2-33;
                    int baseY=height/2+9;
                    int w=64-(int)(64.f*mc.thePlayer.getItemInUseCount()/itemStack.getMaxItemUseDuration());

                    drawTexturedModalRect(baseX, baseY, 88, 0, 66, 4);
                    if(w>0) drawTexturedModalRect(baseX+1, baseY+1, 89, 4, w, 2);
                }
            }
            mc.mcProfiler.endSection();
            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(Gui.icons);
        }
    }


    @SubscribeEvent
    public void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.itemStack;
        List<String> toolTip = event.toolTip;

        //使用することでSAN値が増減するアイテム
        Registry.ItemSanity isan=Registry.GetItemSanity(event.itemStack);
        if(isan!=null){
            if(isan.max>0) toolTip.add(ChatFormatting.BLUE+StatCollector.translateToLocalFormatted("info.fbs.sanity.add", isan.trial, isan.max));
            else if(isan.max<0) toolTip.add(ChatFormatting.RED+StatCollector.translateToLocalFormatted("info.fbs.sanity.lose", isan.trial, -isan.max));
        }

        //精霊武器
        if(itemStack.getItem() instanceof ISpiritTool){

        }

        //剣と刃薬
        if(itemStack.getItem() instanceof ItemSword){
            NBTTagCompound nbt=itemStack.getTagCompound();
            if(nbt!=null && nbt.hasKey(RecipeBladeSpice.AMOUNT)){
                toolTip.add("[Blade Spice "+nbt.getInteger(RecipeBladeSpice.AMOUNT)+"]");

                ArrayList<CharacteristicBase> cbs=RecipeBladeSpice.getCharacteristics(itemStack);
                for(CharacteristicBase cb : cbs){
                    toolTip.add(cb.getNameColor()+"-"+cb.getLocalizedName()+":"+cb.getLocalizedEffectValue());
                }
            }
        }
    }

    public static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6){
        float zLevel = -90.0F;

        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((par1 + 0), (par2 + par6), zLevel, ((par3 + 0) * f), ((par4 + par6) * f1));
        tessellator.addVertexWithUV((par1 + par5), (par2 + par6), zLevel, ((par3 + par5) * f), ((par4 + par6) * f1));
        tessellator.addVertexWithUV((par1 + par5), (par2 + 0), zLevel, ((par3 + par5) * f), ((par4 + 0) * f1));
        tessellator.addVertexWithUV((par1 + 0), (par2 + 0), zLevel, ((par3 + 0) * f), ((par4 + 0) * f1));
        tessellator.draw();
    }
}
