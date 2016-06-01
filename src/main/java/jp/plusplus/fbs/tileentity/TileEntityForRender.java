package jp.plusplus.fbs.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by plusplus_F on 2015/09/25.
 * 毎度おなじみレンダーのためだけのTE
 */
public class TileEntityForRender extends TileEntity {
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if(getBlockType()== BlockCore.tableAlchemist) return AxisAlignedBB.getBoundingBox(xCoord-1,yCoord,zCoord-1,xCoord+2,yCoord+1,zCoord+2);
        else return super.getRenderBoundingBox();
    }
}
