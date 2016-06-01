package jp.plusplus.fbs.tileentity;

/**
 * Created by plusplus_F on 2016/02/23.
 */
public class TileEntityHavestableMushroom extends TileEntityHavestable {
    public TileEntityHavestableMushroom(){

    }
    public TileEntityHavestableMushroom(int ageMax, int meta) {
        super(ageMax, meta);
    }

    @Override
    public boolean canGlow(){
        return getBlockMetadata()==0 || worldObj.getBlockLightValue(xCoord, yCoord, zCoord)<=8;
    }
}
