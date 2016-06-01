package jp.plusplus.fbs.world;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/2/23.
 */
public class WorldGenGrass extends WorldGenerator {
    private Block field_150552_a;
    public WorldGenGrass() {
        field_150552_a = BlockCore.harvestableGrass;
    }

    public boolean generate(World world, Random rand, int bx, int by, int bz) {
        BiomeGenBase bgb = world.getBiomeGenForCoords(bx, bz);
        int id = world.provider.dimensionId;

        //地表
        if (bgb == BiomeGenBase.forest || bgb == BiomeGenBase.plains || bgb == BiomeGenBase.taiga || bgb == BiomeGenBase.taigaHills || bgb == BiomeGenBase.extremeHills ||
                bgb == BiomeGenBase.extremeHillsEdge || bgb == BiomeGenBase.extremeHillsPlus || bgb == BiomeGenBase.birchForest || bgb == BiomeGenBase.birchForestHills ||
                bgb == BiomeGenBase.roofedForest || bgb==BiomeGenBase.jungle || bgb==BiomeGenBase.jungleEdge || bgb==BiomeGenBase.jungleHills ||
                bgb == Registry.biomeCrack || bgb==Registry.biomeAutumn) {

            for (int l = 0; l < 10; ++l) {
                int x = bx + rand.nextInt(8) - rand.nextInt(8);
                int y = by + rand.nextInt(4) - rand.nextInt(4);
                int z = bz + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(x, y, z) && y < 255 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, 0, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }
        return true;
    }
}