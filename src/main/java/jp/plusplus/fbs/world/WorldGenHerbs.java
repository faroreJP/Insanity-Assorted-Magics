package jp.plusplus.fbs.world;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.tileentity.TileEntityHavestable;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeManager;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/22.
 */
public class WorldGenHerbs  extends WorldGenerator {
    private Block field_150552_a;
    private boolean isTop;

    public WorldGenHerbs(boolean isTop) {
        field_150552_a= BlockCore.harvestableHerb;
        this.isTop=isTop;
    }

    public boolean generate(World world, Random rand, int bx, int by, int bz) {
        if(isTop) generateOnGround(world, rand, bx, by, bz);
        else generateUnderGround(world, rand, bx, by, bz);
        return true;
    }

    private void generateOnGround(World world, Random rand, int bx, int by, int bz){
        BiomeGenBase bgb=world.getBiomeGenForCoords(bx,bz);
        int meta=0;

        //--------------------------条件によって生成するハーブのmeta値を変える-------------------------------------
        for(BiomeManager.BiomeEntry be : BiomeManager.getBiomes(BiomeManager.BiomeType.WARM)) {
            if (bgb == be.biome) {
                if(rand.nextFloat()<0.4f) meta=1;
                break;
            }
        }

        boolean flag=false;
        flag=(bgb==BiomeGenBase.desert || bgb==BiomeGenBase.desertHills || bgb==BiomeGenBase.savanna || bgb==BiomeGenBase.savannaPlateau || bgb==BiomeGenBase.mesa || bgb==BiomeGenBase.mesaPlateau|| bgb==BiomeGenBase.mesaPlateau_F);
        if(!flag){
            for(BiomeManager.BiomeEntry be : BiomeManager.getBiomes(BiomeManager.BiomeType.DESERT)) {
                if (bgb == be.biome) {
                    flag=true;
                    break;
                }
            }
        }
        if(flag && rand.nextFloat()<0.65f){
            meta=3;
        }

        if(bgb==Registry.biomeAutumn && rand.nextFloat()<0.4f){
            meta=2;
        }
        if(bgb==Registry.biomeCrack){
            meta=4;
        }
        if(bgb==BiomeGenBase.hell){
            meta=8;
        }
        if(bgb==BiomeGenBase.sky){
            meta=7;
        }

        //--------------------------ハーブを生成する-------------------------------------
        if(meta!=7 && meta!=8){
            for (int l = 0; l < 16; ++l) {
                int x = bx + rand.nextInt(8) - rand.nextInt(8);
                int y = by + rand.nextInt(4) - rand.nextInt(4);
                int z = bz + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(x, y, z) && (!world.provider.hasNoSky || y < 255) && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, meta, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }
        else{
            for (int l = 0; l < 16; ++l) {
                int x = bx + rand.nextInt(8) - rand.nextInt(8);
                int y = by + rand.nextInt(4) - rand.nextInt(4);
                int z = bz + rand.nextInt(8) - rand.nextInt(8);

                if (world.isAirBlock(x, y, z) && y<255 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, meta, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }
    }
    private void generateUnderGround(World world, Random rand, int bx, int by, int bz){
        BiomeGenBase bgb=world.getBiomeGenForCoords(bx,bz);

        if(bgb==BiomeGenBase.hell){
            for (int l = 0; l < 16; ++l) {
                int x = bx + rand.nextInt(4) - rand.nextInt(4);
                int y = by + rand.nextInt(2) - rand.nextInt(2);
                int z = bz + rand.nextInt(4) - rand.nextInt(4);

                if (world.isAirBlock(x, y, z) && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, 8, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }
        else
        if(bgb==BiomeGenBase.sky){
            for (int l = 0; l < 8; ++l) {
                int x = bx + rand.nextInt(4) - rand.nextInt(4);
                int y = by + rand.nextInt(2) - rand.nextInt(2);
                int z = bz + rand.nextInt(4) - rand.nextInt(4);

                if (world.isAirBlock(x, y, z) && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                    if (world.setBlock(x, y, z, this.field_150552_a, 7, 2)) {
                        TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                        te.age = te.ageMax;
                        te.markDirty();
                    }
                }
            }
        }
        else {
            if(by<32+2){
                //ゴールドかマンドレイクか。確率
                if(by<20+2 && rand.nextFloat()<0.5f){
                    for (int l = 0; l < 16; ++l) {
                        int x = bx + rand.nextInt(4) - rand.nextInt(4);
                        int y = by + rand.nextInt(2) - rand.nextInt(2);
                        int z = bz + rand.nextInt(4) - rand.nextInt(4);

                        if (world.isAirBlock(x, y, z) && y<16 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                            if (world.setBlock(x, y, z, this.field_150552_a, 6, 2)) {
                                TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                                te.age = te.ageMax;
                                te.markDirty();
                            }
                        }
                    }
                }
                else{
                    for (int l = 0; l < 16; ++l) {
                        int x = bx + rand.nextInt(4) - rand.nextInt(4);
                        int y = by + rand.nextInt(2) - rand.nextInt(2);
                        int z = bz + rand.nextInt(4) - rand.nextInt(4);

                        if (world.isAirBlock(x, y, z) && y<32 && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                            if (world.setBlock(x, y, z, this.field_150552_a, 5, 2)) {
                                TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                                te.age = te.ageMax;
                                te.markDirty();
                            }
                        }
                    }
                }
            }
            else{
                //y>=32+2ではテケトーに
                int meta=rand.nextInt(4);
                for (int l = 0; l < 32; ++l) {
                    int x = bx + rand.nextInt(4) - rand.nextInt(4);
                    int y = by + rand.nextInt(2) - rand.nextInt(2);
                    int z = bz + rand.nextInt(4) - rand.nextInt(4);

                    if (world.isAirBlock(x, y, z) && this.field_150552_a.canPlaceBlockAt(world, x, y, z)) {
                        if (world.setBlock(x, y, z, this.field_150552_a, meta, 2)) {
                            TileEntityHavestable te = (TileEntityHavestable) world.getTileEntity(x, y, z);
                            te.age = te.ageMax;
                            te.markDirty();
                        }
                    }
                }
            }
        }
    }
}