package jp.plusplus.fbs.world.crack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by plusplus_F on 2015/10/29.
 */
public class WorldProviderCrack extends WorldProvider {
    private float[] skyColors=new float[4];

    @Override
    public String getDimensionName() {
        return "Dimension Crack";
    }

    // 独自のワールドタイプやワールドチャンクマネージャーを設定
    @Override
    protected void registerWorldChunkManager() {
        worldObj.getWorldInfo().setTerrainType(WorldTypeCrack.worldType);
        worldChunkMgr = new WorldChunkManagerCrack(worldObj);
        setDimension(FBS.dimensionCrackId);
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderCrack(worldObj, worldObj.getSeed());
    }

    //太陽の高さ
    @Override
    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        int j = (int)(p_76563_1_ % 24000L);
        float f1 = ((float)j + p_76563_3_) / 24000.0F - 0.25F;

        if (f1 < 0.0F)
        {
            ++f1;
        }

        if (f1 > 1.0F)
        {
            --f1;
        }

        float f2 = f1;
        f1 = 1.0F - (float)((Math.cos((double)f1 * Math.PI) + 1.0D) / 2.0D);
        f1 = f2 + (f1 - f2) / 3.0F;
        return f1;
    }

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        skyColors[0]=0.35f;
        skyColors[1]=0.f;
        skyColors[2]=0.35f;
        skyColors[3]=1.f;
        return skyColors;
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        return Vec3.createVectorHelper(0.35, 0, 0.35);
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
    }

    @Override
    public ChunkCoordinates getEntrancePortalLocation() {
        return new ChunkCoordinates(0, 80, 0);
    }

    @Override
    public int getAverageGroundLevel() {
        return 50;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        return true;
    }

    public boolean isDaytime()
    {
        return false;
    }

    @Override
    public String getWelcomeMessage() {
        return "Entering the Dimension Crack";
    }

    @Override
    public String getDepartMessage() {
        return "Leaving the Dimension Crack";
    }
}
