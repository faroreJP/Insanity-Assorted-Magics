package jp.plusplus.fbs.pottery;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by plusplus_F on 2015/08/26.
 */
public class RenderPottery extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if(modelId!=getRenderId() || !(block instanceof IPottery)) return;
        IPottery ip=(IPottery)block;

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 1.5f, 0.5f);
        GL11.glTranslatef(0, -0.25f, 0);
        GL11.glRotatef(180, 0, 0, 1);

        IPottery.PotteryState state= IPottery.PotteryState.Get((metadata>>6)&3);
        if(state== IPottery.PotteryState.DRY) GL11.glColor3b((byte) (240/2), (byte) (235/2), (byte) (212/2));
        else if(state==IPottery.PotteryState.MOLDED) GL11.glColor3b((byte) (218/2), (byte) (203/2), (byte) (144/2));
        else GL11.glColor3b((byte) (73/2), (byte) (37/2), (byte) (12/2));

        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);

        bindTexture(ip.getResourceLocation(metadata));
        ip.getModel(metadata).render(null, 0, 0, 0, 0, 0, 1.0f);
        bindTexture(TextureMap.locationBlocksTexture);

        GL11.glColor3b((byte)127,(byte)127,(byte)127);
        GL11.glScalef(1f, 1f, 1f);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == getRenderId()) {
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
        return FBS.renderJarId;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float p_147500_8_) {
        Block b=entity.getBlockType();

        if(!(entity instanceof TileEntityPottery)) return;
        TileEntityPottery te=(TileEntityPottery)entity;
        //int meta=entity.getWorldObj().getBlockMetadata(entity.xCoord, entity.yCoord, entity.zCoord);
        int meta=te.getMetadata();

        if(!(b instanceof IPottery)) return;
        IPottery ip=(IPottery)b;
        IPottery.PotteryState state=te.state;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);

        if(state== IPottery.PotteryState.DRY) GL11.glColor3b((byte) (240/2), (byte) (235/2), (byte) (212/2));
        else if(state==IPottery.PotteryState.MOLDED) GL11.glColor3b((byte) (218/2), (byte) (203/2), (byte) (144/2));
        else GL11.glColor3b((byte) (73/2), (byte) (37/2), (byte) (12/2));

        float scale = 0.0625f;
        GL11.glScalef(scale, scale, scale);

        bindTexture(ip.getResourceLocation(meta));
        ip.getModel(meta).render(null, 0, 0, 0, 0, 0, 1.0f);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
