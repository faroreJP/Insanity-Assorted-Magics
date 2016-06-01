package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Created by pluslus_F on 2015/06/17.
 */
public class RenderCharm implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == this.getRenderId()){

            if(block== BlockCore.magicCore){
                TileEntity te=world.getTileEntity(x,y,z);
                if(te instanceof TileEntityMagicCore){
                    if(!((TileEntityMagicCore) te).getCircleName().equals("null")) return true;
                }
            }

            block.setBlockBounds(0, 0, 0, 1, 0.0625f, 1);
            renderer.renderMaxY=0.01f;
            renderer.renderStandardBlock(block, x, y, z);

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
        return FBS.renderCharmId;
    }
}
