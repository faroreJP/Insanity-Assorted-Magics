package jp.plusplus.fbs.world.crack;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by plusplus_F on 2015/10/29.
 */
public class WorldTypeCrack extends WorldType {
    public static WorldType worldType = new WorldTypeCrack("Dimension Crack");

    private WorldTypeCrack(String name) {
        super(name);
    }

    @Override
    public WorldChunkManager getChunkManager(World world) {
        return new WorldChunkManagerCrack(world);
    }

    @Override
    public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
        return new ChunkProviderCrack(world, world.getSeed());
    }

    @Override
    public boolean hasVoidParticles(boolean flag)
    {
        return false;
    }
}