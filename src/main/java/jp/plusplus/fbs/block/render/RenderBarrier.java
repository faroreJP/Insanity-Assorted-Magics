package jp.plusplus.fbs.block.render;

import codechicken.lib.render.BlockRenderer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/09/27.
 */
public class RenderBarrier implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {


        if (modelId == this.getRenderId()){
            IIcon icon=block.getIcon(0,0);

            GL11.glPushMatrix();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);

            Tessellator tessellator = Tessellator.instance;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderer.setRenderBoundsFromBlock(block);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            renderer.setRenderBoundsFromBlock(block);
            GL11.glPopMatrix();
        }

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){


            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);

            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return FBS.renderBarrierId;
    }
}
