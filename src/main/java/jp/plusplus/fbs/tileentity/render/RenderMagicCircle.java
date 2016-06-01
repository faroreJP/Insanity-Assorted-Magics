package jp.plusplus.fbs.tileentity.render;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.ProxyClient;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.HashMap;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class RenderMagicCircle extends TileEntitySpecialRenderer {
    private static HashMap<String, IIcon> icons=new HashMap<String, IIcon>();
    public static RenderMagicCircle renderer;

    public void renderTileEntityCupAt(TileEntityMagicCore par1Tile, double par2, double par4, double par6, float par8) {
        this.setRotation(par1Tile, (float) par2, (float) par4, (float) par6);
    }

    public void setTileEntityRenderer(TileEntityRendererDispatcher par1TileEntityRenderer) {
        super.func_147497_a(par1TileEntityRenderer);
        renderer = this;
    }

    public void setRotation(TileEntityMagicCore par0Tile, float par1, float par2, float par3) {
        //テセレータを使って、一枚の平面テクスチャとして表示させる。

        Tessellator tessellator = Tessellator.instance;
        String name = par0Tile.getCircleName();
        float tick = FBS.proxy.getRenderPartialTicks();

        if (!name.equals("null")) {
            float r = par0Tile.getCircleRadius();

            //コメントアウト部分を復帰させると、水面の描写が半透明になる。
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
            GL11.glTranslatef((float) par1 + 0.5f, (float) par2, (float) par3 + 0.5f);
            GL11.glRotatef(360.0f / 120.0f * ((par0Tile.ticks % 120) + tick), 0f, 1f, 0f);


            IIcon iicon = GetMagicCircleIcon(name);
            float f14 = iicon.getMinU();
            float f15 = iicon.getMaxU();
            float f4 = iicon.getMinV();
            float f5 = iicon.getMaxV();

            this.bindTexture(TextureMap.locationBlocksTexture);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV(-0.5f - r, 0.0625D, 0.5f + r, (double) f15, (double) f4);
            tessellator.addVertexWithUV(0.5f + r, 0.0625D, 0.5f + r, (double) f14, (double) f4);
            tessellator.addVertexWithUV(0.5f + r, 0.0625D, -0.5f - r, (double) f14, (double) f5);
            tessellator.addVertexWithUV(-0.5f - r, 0.0625D, -0.5f - r, (double) f15, (double) f5);
            tessellator.draw();


            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
        this.renderTileEntityCupAt((TileEntityMagicCore) par1TileEntity, par2, par4, par6, par8);
    }

    public static void ClearMagicCircleIcons(){
        icons.clear();
    }
    public static void RegisterMagicCircleIcon(String name, TextureMap ir, String iconName){
        if(icons.containsKey(name)) icons.remove(name);
        icons.put(name, ir.registerIcon(iconName));
    }
    public static IIcon GetMagicCircleIcon(String name){
        return icons.get(name);
    }

}
