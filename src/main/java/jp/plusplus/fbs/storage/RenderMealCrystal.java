package jp.plusplus.fbs.storage;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.model.ModelAlchemyCauldron;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class RenderMealCrystal extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static ResourceLocation rl = new ResourceLocation(FBS.MODID, "textures/models/MealCrystal.png");
    protected ModelMealCrystal model = new ModelMealCrystal();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return getRenderId()==modelId;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return FBS.renderMealId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        int meta = te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord, te.zCoord);
        if (meta!=0) return;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f+1.0f/16.0f, (float) z + 0.5f);
        GL11.glRotatef(180.0f, 0, 0, 1);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        this.bindTexture(rl);
        model.render(null, 0, 0, 0, 0, 0, 1.0f);
        model.renderCrystal(1.f, (float)te.getWorldObj().getTotalWorldTime()+p_147500_8_);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
