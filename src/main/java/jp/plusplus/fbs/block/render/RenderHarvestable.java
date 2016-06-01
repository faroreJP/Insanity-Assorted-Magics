package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.model.ModelHerb;
import jp.plusplus.fbs.block.model.ModelMushroom;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
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
 * Created by plusplus_F on 2015/11/15.
 */
public class RenderHarvestable extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static ResourceLocation rlHerb = new ResourceLocation(FBS.MODID + ":textures/models/Herb.png");
    protected ModelHerb[] herbs;

    public static ResourceLocation rlMushroom=new ResourceLocation(FBS.MODID + ":textures/models/Mushroom.png");
    protected ModelMushroom[] mushes;

    public RenderHarvestable(){
        herbs=new ModelHerb[9];
        for(int i=0;i<herbs.length;i++) herbs[i]=new ModelHerb(i);

        mushes=new ModelMushroom[3];
        for(int i=0;i<mushes.length;i++) mushes[i]=new ModelMushroom(i);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId!=getRenderId()) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 1.75f, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        float scale = 0.0625f;
        scale*=1.25f;
        GL11.glScalef(scale, scale, scale);

        if(block==BlockCore.harvestableHerb){
            bindTexture(rlHerb);
            herbs[metadata].render(null, 0, 0, 0, 0, 0, 1.0f);
            herbs[metadata].renderHerbs(1.0f);
        }
        else if(block==BlockCore.harvestableMushroom){
            bindTexture(rlMushroom);
            mushes[metadata].render(null, 0, 0, 0, 0, 0, 1.0f);
            mushes[metadata].renderMush(1.0f);
        }
        else if(block==BlockCore.harvestableGrass){
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            Tessellator tessellator = Tessellator.instance;
            IIcon p_147765_1_=block.getIcon(0, 0);

            double p_147765_8_=0.8;
            double d3 = (double)p_147765_1_.getMinU();
            double d4 = (double)p_147765_1_.getMinV();
            double d5 = (double)p_147765_1_.getMaxU();
            double d6 = (double)p_147765_1_.getMaxV();
            double d7 = 0.45D * (double)p_147765_8_;
            double d8 = 0 + 0.5D - d7;
            double d9 = 0 + 0.5D + d7;
            double d10 = 0 + 0.5D - d7;
            double d11 = 0 + 0.5D + d7;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(d8, (double)p_147765_8_, d10, d3, d4);
            tessellator.addVertexWithUV(d8, 0.0D, d10, d3, d6);
            tessellator.addVertexWithUV(d9, 0.0D, d11, d5, d6);
            tessellator.addVertexWithUV(d9, (double)p_147765_8_, d11, d5, d4);
            tessellator.addVertexWithUV(d9, (double)p_147765_8_, d11, d3, d4);
            tessellator.addVertexWithUV(d9, 0.0D, d11, d3, d6);
            tessellator.addVertexWithUV(d8, 0.0D, d10, d5, d6);
            tessellator.addVertexWithUV(d8, (double)p_147765_8_, d10, d5, d4);
            tessellator.addVertexWithUV(d8, (double)p_147765_8_, d11, d3, d4);
            tessellator.addVertexWithUV(d8, 0.0D, d11, d3, d6);
            tessellator.addVertexWithUV(d9, 0.0D, d10, d5, d6);
            tessellator.addVertexWithUV(d9, (double)p_147765_8_, d10, d5, d4);
            tessellator.addVertexWithUV(d9, (double)p_147765_8_, d10, d3, d4);
            tessellator.addVertexWithUV(d9, 0.0D, d10, d3, d6);
            tessellator.addVertexWithUV(d8, 0.0D, d11, d5, d6);
            tessellator.addVertexWithUV(d8, (double)p_147765_8_, d11, d5, d4);
            tessellator.draw();
        }
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        if(modelId==getRenderId()){
            if(block==BlockCore.harvestableGrass) {
                Tessellator tessellator = Tessellator.instance;
                tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
                int l = block.colorMultiplier(world, x, y, z);
                float f = (float) (l >> 16 & 255) / 255.0F;
                float f1 = (float) (l >> 8 & 255) / 255.0F;
                float f2 = (float) (l & 255) / 255.0F;

                if (EntityRenderer.anaglyphEnable) {
                    float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
                    float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
                    float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
                    f = f3;
                    f1 = f4;
                    f2 = f5;
                }

                tessellator.setColorOpaque_F(f, f1, f2);
                double d1 = (double) x;
                double d2 = (double) y;
                double d0 = (double) z;
                long i1;

                i1 = (long) (x * 3129871) ^ (long) y * 116129781L ^ (long) z;
                i1 = i1 * i1 * 42317861L + i1 * 11L;
                d1 += ((double) ((float) (i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
                d2 += ((double) ((float) (i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
                d0 += ((double) ((float) (i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;

                IIcon iicon = block.getIcon(world, x, y, z, 0);
                RenderBlocks.getInstance().drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
            }
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
        return FBS.renderHerbId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1+0.5f, (float) z + 0.5f);

        GL11.glRotatef(180.0f, 0, 0, 1);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if(te instanceof TileEntityHavestable){
            Block b=te.getBlockType();
            TileEntityHavestable teh=(TileEntityHavestable) te;

            if(b==BlockCore.harvestableHerb){
                this.bindTexture(rlHerb);
                herbs[meta].render(null, 0, 0, 0, 0, 0, 1.0f);
                if(teh.canHarvest()){
                    herbs[meta].renderHerbs(1.0f);
                }
            }
            else if(b==BlockCore.harvestableMushroom){
                this.bindTexture(rlMushroom);
                mushes[meta].render(null, 0, 0, 0, 0, 0, 1.0f);
                if(teh.canHarvest()){
                    mushes[meta].renderMush(1.0f);
                }
            }
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
