package jp.plusplus.fbs.storage;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2016/03/08.
 */
public class RenderMealTerminal extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static ResourceLocation rl = new ResourceLocation(FBS.MODID + ":textures/models/MealInlet.png");
    protected ModelMealInlet inlet=new ModelMealInlet(false);
    protected ModelMealInlet outlet=new ModelMealInlet(true);

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (modelId != getRenderId()) return;

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        float scale = 0.0625f;
        scale *= 1.25f;
        GL11.glScalef(scale, scale, scale);

        bindTexture(rl);
        outlet.render(null, 0, 0, 0, 0, 0, 1.0f);
        GL11.glTranslatef(0,-3/1.25f, 0);
        inlet.render(null, 0, 0, 0, 0, 0, 1.0f);
        inlet.renderCrystal(1.0f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return modelId==getRenderId();
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return FBS.renderMealTerminalId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        bindTexture(rl);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        outlet.render(null, 0, 0, 0, 0, 0, 1.f);
        GL11.glTranslatef(0, -3, 0);
        inlet.render(null, 0,0,0,0,0,1.f);
        if(((IMealDevice)te).hasFragment()) inlet.renderCrystal(1.f);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

        bindTexture(TextureMap.locationBlocksTexture);
    }
}
