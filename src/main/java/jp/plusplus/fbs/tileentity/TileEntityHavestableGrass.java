package jp.plusplus.fbs.tileentity;

import net.minecraft.block.BlockSapling;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class TileEntityHavestableGrass extends TileEntityHavestable {
    public TileEntityHavestableGrass(){

    }
    public TileEntityHavestableGrass(int ageMax, int meta) {
        super(ageMax, meta);
    }

    @Override
    public boolean canGlow(){
        return worldObj.getBlockLightValue(xCoord, yCoord+1, zCoord)>=9;
        //return worldObj.is;
    }
}
