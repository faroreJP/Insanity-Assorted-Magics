package jp.plusplus.fbs.block.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * Created by plusplus_F on 2015/10/28.
 */
public class RenderPortalWarp implements ISimpleBlockRenderingHandler {
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(modelId==getRenderId()){
            int meta=world.getBlockMetadata(x,y,z);
            if((meta&8)!=0){
                renderer.setRenderBounds(0.5,0,0,0.5,1,1);
            }
            else{
                renderer.setRenderBounds(0,0,0.5,1,1,0.5);
            }

            renderer.renderStandardBlock(block, x,y,z);

            renderer.setRenderBounds(0,0,0,1,1,1);
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
        return FBS.renderPortalWarpId;
    }
}
