package jp.plusplus.fbs.world.autumn;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by plusplus_F on 2015/11/07.
 */
public class WorldProviderAutumn extends WorldProvider {

    @Override
    public String getDimensionName() {
        return "Eternal Autumn";
    }

    // 独自のワールドタイプやワールドチャンクマネージャーを設定
    @Override
    protected void registerWorldChunkManager() {
        worldObj.getWorldInfo().setTerrainType(WorldTypeAutumn.worldType);
        worldChunkMgr = new WorldChunkManagerAutumn(worldObj);
        setDimension(FBS.dimensionAutumnId);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderAutumn(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
    }

    @Override
    public String getWelcomeMessage() {
        return "Entering the Eternal Autumn";
    }

    @Override
    public String getDepartMessage() {
        return "Leaving the Eternal Autumn";
    }
}
