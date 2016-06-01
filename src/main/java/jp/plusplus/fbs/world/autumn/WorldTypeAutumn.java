package jp.plusplus.fbs.world.autumn;

import jp.plusplus.fbs.world.crack.ChunkProviderCrack;
import jp.plusplus.fbs.world.crack.WorldChunkManagerCrack;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by plusplus_F on 2015/10/29.
 */
public class WorldTypeAutumn extends WorldType {
    public static WorldType worldType = new WorldTypeAutumn("Eternal Autumn");

    private WorldTypeAutumn(String name) {
        super(name);
    }

    @Override
    public WorldChunkManager getChunkManager(World world) {
        return new WorldChunkManagerAutumn(world);
    }

    @Override
    public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
        return new ChunkProviderAutumn(world, world.getSeed(), true);
    }
}