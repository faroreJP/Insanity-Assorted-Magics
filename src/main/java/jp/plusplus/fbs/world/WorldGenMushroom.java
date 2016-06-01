package jp.plusplus.fbs.world;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeManager;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/2/23.
 */
public class WorldGenMushroom extends WorldGenerator {
    private Block field_150552_a;
    private boolean isTop;

    public WorldGenMushroom(boolean isTop) {
        field_150552_a = BlockCore.harvestableMushroom;
        this.isTop = isTop;
    }

    public boolean generate(World world, Random rand, int bx, int by, int bz) {
        if (isTop) generateOnGround(world, rand, bx, by, bz);
        else generateUnderGround(world, rand, bx, by, bz);
        return true;
    }

    private void generateOnGround(World world, Random rand, int bx, int by, int bz) {
        BiomeGenBase bgb = world.getBiomeGenForCoords(bx, bz);
        int id = world.provider.dimensionId;

        //地表
        if (id == 0 || id == FBS.dimensionAutumnId || id == FBS.dimensionCrackId) {
            for (int l = 0; l < 8; ++l) {
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

        //ネザ
        if (id == -1) {
            for (int l = 0; l < 16; ++l) {
                int x = bx + rand.nextInt(8) - rand.nextInt(8);
                int y = by + rand.nextInt(4) - rand.nextInt(4);
                int z = bz + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(x, y, z) && y < 255 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, 1, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }


    }

    private void generateUnderGround(World world, Random rand, int bx, int by, int bz) {
        if (world.provider.dimensionId != 0) return;

        if (by < 50 + 2) {
            if (by < 30 + 2 && rand.nextFloat() < 0.5f) {
                for (int l = 0; l < 16; ++l) {
                    int x = bx + rand.nextInt(4) - rand.nextInt(4);
                    int y = by + rand.nextInt(2) - rand.nextInt(2);
                    int z = bz + rand.nextInt(4) - rand.nextInt(4);

                    if (world.isAirBlock(x, y, z) && y < 16 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                        if (world.setBlock(x, y, z, this.field_150552_a, 2, 2)) {
                            TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                            te.age = te.ageMax;
                            te.markDirty();
                        }
                    }
                }
            } else {
                for (int l = 0; l < 16; ++l) {
                    int x = bx + rand.nextInt(4) - rand.nextInt(4);
                    int y = by + rand.nextInt(2) - rand.nextInt(2);
                    int z = bz + rand.nextInt(4) - rand.nextInt(4);

                    if (world.isAirBlock(x, y, z) && y < 32 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                        if (world.setBlock(x, y, z, this.field_150552_a, 1, 2)) {
                            TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                            te.age = te.ageMax;
                            te.markDirty();
                        }
                    }
                }
            }
        } else {
            for (int l = 0; l < 32; ++l) {
                int x = bx + rand.nextInt(4) - rand.nextInt(4);
                int y = by + rand.nextInt(2) - rand.nextInt(2);
                int z = bz + rand.nextInt(4) - rand.nextInt(4);

                if (world.isAirBlock(x, y, z) && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, 0, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }
    }
}