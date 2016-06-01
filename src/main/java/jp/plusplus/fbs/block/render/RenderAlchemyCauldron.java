package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.model.ModelAlchemyCauldron;
import jp.plusplus.fbs.model.ModelMirror;
import mods.defeatedcrow.common.fluid.BlockCamOilFluid;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by pluslus_F on 2015/09/24.
 */
public class RenderAlchemyCauldron extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static ResourceLocation rl = new ResourceLocation(FBS.MODID + ":textures/models/AlchemyCauldron.png");
    protected ModelAlchemyCauldron model = new ModelAlchemyCauldron();
    float f4 = 4.0f / 16.0f;
    float f5 = 5.0f / 16.0f;
    float f9 = 9.0f / 16.0f;
    float f11 = 11.0f / 16.0f;
    float f12 = 12.0f / 16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90, 0, -1, 0);

        float scale = 0.0625f*0.8f;
        GL11.glScalef(scale, scale, scale);

        bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == this.getRenderId()) {
            int meta = world.getBlockMetadata(x, y, z)&7;

            renderer.setOverrideBlockTexture(Blocks.brick_block.getIcon(0, 0));


            if (meta == 0) {
                renderer.setRenderBounds(f5, 0, f5, 1, f5, 1);
                renderer.renderStandardBlock(block, x, y, z);
                renderer.setRenderBounds(f5, f5, f5, f12, f9, f12);
                renderer.renderStandardBlock(block, x, y, z);
            } else if (meta == 1) {
                renderer.setRenderBounds(0, 0, f5, f11, f5, 1);
                renderer.renderStandardBlock(block, x, y, z);
                renderer.setRenderBounds(f4, f5, f5, f11, f9, f12);
                renderer.renderStandardBlock(block, x, y, z);
            } else if (meta == 2) {
                renderer.setRenderBounds(f5, 0, 0, 1, f5, f11);
                renderer.renderStandardBlock(block, x, y, z);
                renderer.setRenderBounds(f5, f5, f5, f12, f9, f11);
                renderer.renderStandardBlock(block, x, y, z);
            } else if (meta == 3) {
                renderer.setRenderBounds(0, 0, 0, f11, f5, f11);
                renderer.renderStandardBlock(block, x, y, z);
                renderer.setRenderBounds(f4, f5, f5, f11, f9, f11);
                renderer.renderStandardBlock(block, x, y, z);
            }

            renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
            renderer.clearOverrideBlockTexture();
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return FBS.renderAlchemyCauldronId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        if ((meta&7) != 0) return;

        GL11.glPushMatrix();

        if((meta&8)!=0){
            GL11.glTranslatef((float) x + 0.5f, (float) y + (16.0f - 7.0f) / 16.0f, (float) z + 0.5f);
            GL11.glRotatef(90.0f, 0, 1, 0);
        }
        else{
            GL11.glTranslatef((float) x + 0.5f + 1.f, (float) y + (16.0f - 7.0f) / 16.0f, (float) z + 0.5f);
        }

        GL11.glRotatef(180.0f, 0, 0, 1);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        this.bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();


        //溜まった水の描画
        IIcon icon = BlockCore.mana.getIcon();
        if (icon == null) return;
        float minU=icon.getInterpolatedU(16 * 0);
        float maxU=icon.getInterpolatedU(16 * 1);
        float minV=icon.getInterpolatedV(16 * 0);
        float maxV=icon.getInterpolatedV(16 * 1);

        bindTexture(TextureMap.locationBlocksTexture);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

        float height = 15.0f/16.0f;
        Tessellator tessellator=Tessellator.instance;

        //0
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(f5, height, f5, minU, minV);
        tessellator.addVertexWithUV(f5, height, 1, minU, maxV);
        tessellator.addVertexWithUV(1, height, 1, maxU, maxV);
        tessellator.addVertexWithUV(1, height, f5, maxU, minV);
        tessellator.draw();

        //1
        GL11.glTranslatef(1.0f, 0, 0);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(0, height, f5, minU, minV);
        tessellator.addVertexWithUV(0, height, 1, minU, maxV);
        tessellator.addVertexWithUV(f11, height, 1, maxU, maxV);
        tessellator.addVertexWithUV(f11, height, f5, maxU, minV);
        tessellator.draw();

        //2
        GL11.glTranslatef(-1.0f, 0, 1);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(f5, height, 0, minU, minV);
        tessellator.addVertexWithUV(f5, height, f11, minU, maxV);
        tessellator.addVertexWithUV(1, height, f11, maxU, maxV);
        tessellator.addVertexWithUV(1, height, 0, maxU, minV);
        tessellator.draw();

        //3
        GL11.glTranslatef(1.0f, 0, 0);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(0, height, 0, minU, minV);
        tessellator.addVertexWithUV(0, height, f11, minU, maxV);
        tessellator.addVertexWithUV(f11, height, f11, maxU, maxV);
        tessellator.addVertexWithUV(f11, height, 0, maxU, minV);
        tessellator.draw();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
