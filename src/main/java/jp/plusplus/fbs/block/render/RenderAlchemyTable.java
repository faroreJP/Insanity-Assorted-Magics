package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.model.ModelAlchemyCauldron;
import jp.plusplus.fbs.block.model.ModelAlchemyTable;
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
 * Created by pluslus_F on 2015/09/25.
 */
public class RenderAlchemyTable extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static ResourceLocation rl = new ResourceLocation(FBS.MODID + ":textures/models/AlchemyTable.png");
    protected ModelAlchemyTable model = new ModelAlchemyTable();
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
        return FBS.renderAlchemyTableId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        if ((meta&8) != 0) return;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f + 1.f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 0, 0, 1);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        switch(meta){
            case 4: GL11.glRotatef(90, 0, -1, 0); break;
            case 5: GL11.glRotatef(90, 0, 1, 0); break;
            case 3: GL11.glRotatef(180, 0, -1, 0); break;
            default: break;
        }

        this.bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
