package jp.plusplus.fbs.storage;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.model.ModelHerb;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class RenderMealInlet extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
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
        if (block == BlockCore.mealInlet) {
            inlet.render(null, 0, 0, 0, 0, 0, 1.0f);
            inlet.renderCrystal(1.0f);
        } else {
            outlet.render(null, 0, 0, 0, 0, 0, 1.0f);
            outlet.renderCrystal(1.0f);
        }

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
        return FBS.renderMealInletId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        ForgeDirection dir=ForgeDirection.getOrientation(te.getBlockMetadata()&7);

        bindTexture(rl);

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        switch (dir){
            case DOWN: GL11.glRotatef(180, 0, 0, 1); break;
            case UP: GL11.glRotatef(0, 0, 0, 1); break;
            case NORTH: GL11.glRotatef(90, -1, 0, 0); break;
            case SOUTH: GL11.glRotatef(90, 1, 0, 0); break;
            case WEST: GL11.glRotatef(90, 0, 0, 1); break;
            case EAST: GL11.glRotatef(90, 0, 0, -1); break;
        }
        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        ModelMealInlet model;
        if(te.getBlockType()==BlockCore.mealInlet) model=inlet;
        else model=outlet;

        model.render(null, 0,0,0,0,0,1.f);
        if(((IMealDevice)te).hasFragment()) model.renderCrystal(1.f);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

        bindTexture(TextureMap.locationBlocksTexture);
    }
}
