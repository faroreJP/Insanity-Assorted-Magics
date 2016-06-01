package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.model.ModelAlchemyTable;
import jp.plusplus.fbs.block.model.ModelBonfire;
import jp.plusplus.fbs.block.model.ModelSchoolTable;
import jp.plusplus.fbs.render.TessellatorWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.client.renderer.RenderBlocks;
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
 * Created by plusplus_F on 2015/09/25.
 */
public class RenderDecorations extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static final ResourceLocation rl0=new ResourceLocation(FBS.MODID+":textures/models/SchoolTable.png");
    public ModelSchoolTable md0=new ModelSchoolTable();

    public static final ResourceLocation rl1 = new ResourceLocation(FBS.MODID + ":textures/models/AlchemyTable.png");
    public ModelAlchemyTable md1 = new ModelAlchemyTable();

    public static final ResourceLocation rl2=new ResourceLocation(FBS.MODID+":textures/models/Bonfire.png");
    public ModelBonfire md2=new ModelBonfire();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(getRenderId()!=modelId) return;
        int type=getRenderType(block, metadata);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        if(type==0) GL11.glRotatef(90, 0, 1, 0);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);

        if(type==0){
            GL11.glTranslatef(0, 2f, 0);
            bindTexture(rl0);
            md0.render(null,0,0,0,0,0,1.0f);
        }
        else if(type==1){
            GL11.glTranslatef(-5, 4f, 0);
            scale=0.8f;
            GL11.glScalef(scale, scale, scale);
            bindTexture(rl1);
            md1.render(null, 0, 0, 0, 0, 0, 1.0f);
        }
        else if(type==2){
            //GL11.glTranslatef(-5, 4f, 0);
            bindTexture(rl2);
            GL11.glTranslatef(0, -16f, 0);
            md2.render(null, 0, 0, 0, 0, 0, 1.0f);
        }

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();

        bindTexture(TextureMap.locationBlocksTexture);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(getRenderId()==modelId){

            int meta=world.getBlockMetadata(x,y,z);
            int type=getRenderType(block, meta);
            if(type==2 && meta>0){
                /*
                renderer.setOverrideBlockTexture(Blocks.fire.getIcon(0,0));

                renderer.renderMinY=0;
                renderer.renderMaxY=1;

                GL11.glPushMatrix();

                renderer.renderMinZ=0;
                renderer.renderMaxZ=1;
                renderer.renderMinX=renderer.renderMaxX=0.25;
                renderer.renderStandardBlock(block, x, y, z);
                renderer.renderMinX=renderer.renderMaxX=0.75;
                renderer.renderStandardBlock(block, x, y, z);

                renderer.renderMinX=0;
                renderer.renderMaxX=1;
                renderer.renderMinZ=renderer.renderMaxZ=0.25;
                renderer.renderStandardBlock(block, x, y, z);
                renderer.renderMinZ=renderer.renderMaxZ=0.75;
                renderer.renderStandardBlock(block, x, y, z);

                renderer.clearOverrideBlockTexture();
                */
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
        return FBS.renderDecorationId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        int meta=te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        int type=getRenderType(te.getBlockType(), meta);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 0, 0, 1);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if(type==0){
            switch(meta){
                case 4: GL11.glRotatef(90, 0, -1, 0); break;
                case 5: GL11.glRotatef(90, 0, 1, 0); break;
                case 3: GL11.glRotatef(180, 0, -1, 0); break;
                default: break;
            }

            bindTexture(rl0);
            md0.render(null, 0,0,0,0,0,1.f);
        }
        else if(type==1){
            if((meta&8)==0){
                switch(meta){
                    case 4: GL11.glRotatef(90, 0, -1, 0); break;
                    case 5: GL11.glRotatef(90, 0, 1, 0); break;
                    case 3: GL11.glRotatef(180, 0, -1, 0); break;
                    default: break;
                }

                this.bindTexture(rl1);
                md1.render(null, 0, 0, 0, 0, 0, 1.0f);
            }
        }
        else if(type==2){
            GL11.glTranslatef(0, -16.f, 0);
            bindTexture(rl2);
            md2.render(null, 0,0,0,0,0,1.f);

            //焔の描画
            if(meta>0) {
                bindTexture(TextureMap.locationBlocksTexture);
                IIcon icon = Blocks.fire.getIcon(0, 0);

                GL11.glPushMatrix();
                GL11.glTranslatef(-4f, 16.f-3f, -4f);
                //GL11.glScalef(0.5f, 0.5f, 0.5f);
                float ssc=9f;
                GL11.glScalef(ssc, ssc, ssc);

                TessellatorWrapper.SetBlockRender(false);
                TessellatorWrapper.DrawXNeg(0.25f, 0.f, 0.f, 1.f, 1.f, icon);
                TessellatorWrapper.DrawXPos(0.25f, 0.f, 0.f, 1.f, 1.f, icon);
                TessellatorWrapper.DrawXNeg(0.75f, 0.f, 0.f, 1.f, 1.f, icon);
                TessellatorWrapper.DrawXPos(0.75f, 0.f, 0.f, 1.f, 1.f, icon);

                TessellatorWrapper.DrawZNeg(0.f, 0.f, 0.25f, 1.f, 1.f, icon);
                TessellatorWrapper.DrawZPos(0.f, 0.f, 0.25f, 1.f, 1.f, icon);
                TessellatorWrapper.DrawZNeg(0.f, 0.f, 0.75f, 1.f, 1.f, icon);
                TessellatorWrapper.DrawZPos(0.f, 0.f, 0.75f, 1.f, 1.f, icon);

                GL11.glPopMatrix();
            }
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public int getRenderType(Block block, int meta){
        if(block==BlockCore.schoolTable) return 0;
        if(block==BlockCore.tableAlchemist) return 1;
        if(block==BlockCore.bonfire) return 2;
        return 0;
    }
}
