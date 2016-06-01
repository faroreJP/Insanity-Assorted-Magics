package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.model.ModelMirror;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import shift.sextiarysector.renderer.model.ModelWindmill;
import shift.sextiarysector.tileentity.TileEntityWindmill;

/**
 * Created by pluslus_F on 2015/06/24.
 */
public class RenderMirror extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
    public static ResourceLocation rl=new ResourceLocation(FBS.MODID+":textures/entity/mirror.png");
    protected ModelMirror model=new ModelMirror();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == this.getRenderId()){
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
        return FBS.renderMirrorId;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_) {
        int dir=te.getBlockMetadata();
        if((dir&8)!=0) return;

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x+0.5f, (float)y, (float)z+0.5f);
        float scale = 0.0625f;
        GL11.glScalef(scale,scale,scale);

        switch(dir){
            case 5:
                GL11.glRotatef(90, 0, 1, 0);
                break;
            case 4:
                GL11.glRotatef(90, 0, -1, 0);
                break;
            case 2:
                GL11.glRotatef(180, 0, 1, 0);
                break;
            default:
                break;
        }

        this.bindTexture(rl);
        model.render(null, 0,0,0, 0,0, 1.0f);
        GL11.glPopMatrix();
    }
}
