package jp.plusplus.fbs.pottery;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * Created by plusplus_F on 2015/08/31.
 *
 * renderInventoryBlockではてってれーたワイワイしないといけないの、最高に○ソ
 * いちいちめんどくさいんだよ反省しろ
 *
 */
public class RenderPottersWheel implements ISimpleBlockRenderingHandler {
    private float f1=9.0f/16.0f;
    private float f2=10.0f/16.0f;
    private float f3=12.0f/16.0f;

    private float f11=6.0f/16.0f;
    private float f12=11.0f/16.0f;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            IIcon tSide=block.getIcon(2,0);
            IIcon tTop=block.getIcon(1,0);
            IIcon tBottom=block.getIcon(0,0);

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            Tessellator tessellator = Tessellator.instance;
            block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

            //----------------------------土台---------------------------------------
            renderer.setRenderBounds(0,0,0,1,f1,1);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, tBottom);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();
            //------------------------------------------------------------------------

            //----------------------------真ん中---------------------------------------
            renderer.setRenderBounds(f11,f1,f11,f12,f2,f12);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, tBottom);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();
            //------------------------------------------------------------------------

            //----------------------------一番上（マジで○ね）---------------------------------------
            renderer.setRenderBounds(0,f2,0,1,f3,1);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, tBottom);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0d, 0.0d, 0.0d, tTop);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();

            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, tSide);
            tessellator.draw();
            //------------------------------------------------------------------------

            renderer.setRenderBounds(0,0,0,1,1,1);

            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            renderer.setRenderBounds(0,0,0,1,f1,1);
            renderer.renderStandardBlock(block, x,y,z);

            renderer.setRenderBounds(f11,f1,f11,f12,f2,f12);
            renderer.renderStandardBlock(block, x,y,z);

            renderer.setRenderBounds(0,f2,0,1,f3,1);
            renderer.renderStandardBlock(block, x,y,z);

            renderer.setRenderBounds(0,0,0,1,1,1);
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
        return FBS.renderPottersWheelId;
    }
}
