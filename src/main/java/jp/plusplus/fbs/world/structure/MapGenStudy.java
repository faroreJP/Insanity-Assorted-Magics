package jp.plusplus.fbs.world.structure;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.*;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/24.
 * 「魔力の秋」にのみ生成される、地上でありながら書物を得られる構造物
 *
 * こんなクッソ面倒な仕様にしやがって！
 * もっと他にやり方あっただろ！
 */
public class MapGenStudy extends MapGenStructure {
    private int maxDistanceBetweenScatteredFeatures=12;
    private int minDistanceBetweenScatteredFeatures=3;

    @Override
    public String func_143025_a() {
        return "Study";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        int k = p_75047_1_;
        int l = p_75047_2_;

        //
        //if(k%4==0 || l%4==0) return true;

        if (p_75047_1_ < 0) {
            p_75047_1_ -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        if (p_75047_2_ < 0) {
            p_75047_2_ -= this.maxDistanceBetweenScatteredFeatures - 1;
        }

        int i1 = p_75047_1_ / this.maxDistanceBetweenScatteredFeatures;
        int j1 = p_75047_2_ / this.maxDistanceBetweenScatteredFeatures;
        Random random = this.worldObj.setRandomSeed(i1, j1, 14357617);
        i1 *= this.maxDistanceBetweenScatteredFeatures;
        j1 *= this.maxDistanceBetweenScatteredFeatures;
        i1 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
        j1 += random.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);

        if (k == i1 && l == j1) {
            //FBS.logger.info("generated at:"+p_75047_1_+","+p_75047_2_);

            BiomeGenBase biomegenbase = this.worldObj.getWorldChunkManager().getBiomeGenAt(k * 16 + 8, l * 16 + 8);
            if(biomegenbase== Registry.biomeAutumn){
                return true;
            }
        }

        return false;
    }

    @Override
    protected StructureStart getStructureStart(int i, int j) {
        return new Start(this.worldObj, this.rand, i, j);
    }

    //----------------------------------------------------------------------------------
    //                 起点
    //----------------------------------------------------------------------------------
    public static class Start extends StructureStart {
        public Start() {
        }

        public Start(World p_i2060_1_, Random rand, int x, int z) {
            super(x, z);
            components.add(new Structure(rand, x*16,100,z*16));
            this.updateBoundingBox();
        }
    }

    public static class Structure extends StructureComponent {
        protected int width;
        protected int height;
        protected int depth;
        protected int field_74936_d = -1;
        protected boolean[] hasShelf={false, false, false};
        protected boolean[] judged={false, false, false};

        public Structure() {
        }

        protected Structure(Random rand, int x, int y, int z) {
            super(0);
            this.width = 27;
            this.height = 9;
            this.depth = 17;
            this.coordBaseMode = rand.nextInt(4);

            switch (this.coordBaseMode) {
                case 0:
                case 2:
                    this.boundingBox = new StructureBoundingBox(x, y, z, x + width - 1, y + height - 1, z + depth - 1);
                    break;
                default:
                    this.boundingBox = new StructureBoundingBox(x, y, z, x + depth - 1, y + height - 1, z + width - 1);
            }
        }

        protected void func_143012_a(NBTTagCompound p_143012_1_) {
            p_143012_1_.setInteger("Width", this.width);
            p_143012_1_.setInteger("Height", this.height);
            p_143012_1_.setInteger("Depth", this.depth);
            p_143012_1_.setInteger("HPos", this.field_74936_d);
            for(int i=0;i<3;i++){
                p_143012_1_.setBoolean("Judged"+i, judged[i]);
                p_143012_1_.setBoolean("HasShelf"+i, hasShelf[i]);
            }
        }

        protected void func_143011_b(NBTTagCompound p_143011_1_) {
            this.width = p_143011_1_.getInteger("Width");
            this.height = p_143011_1_.getInteger("Height");
            this.depth = p_143011_1_.getInteger("Depth");
            this.field_74936_d = p_143011_1_.getInteger("HPos");
            for(int i=0;i<3;i++){
                judged[i]=p_143011_1_.getBoolean("Judged"+i);
                hasShelf[i]=p_143011_1_.getBoolean("HasShelf"+i);
            }
        }

        /**
         * offsetYを計算する
         * @param p_74935_1_
         * @param p_74935_2_
         * @param p_74935_3_
         * @return
         */
        protected boolean func_74935_a(World p_74935_1_, StructureBoundingBox p_74935_2_, int p_74935_3_) {
            if (this.field_74936_d >= 0) {
                return true;
            } else {
                int j = 0;
                int k = 0;
                int maxY=0;

                for (int l = this.boundingBox.minZ; l <= this.boundingBox.maxZ; ++l) {
                    for (int i1 = this.boundingBox.minX; i1 <= this.boundingBox.maxX; ++i1) {
                        if (p_74935_2_.isVecInside(i1, 70, l)) {
                            int __i=Math.max(p_74935_1_.getTopSolidOrLiquidBlock(i1, l), p_74935_1_.provider.getAverageGroundLevel());
                            j += __i;
                            ++k;
                            if(maxY<__i) maxY=__i;
                        }
                    }
                }

                if (k == 0) {
                    return false;
                } else {
                    this.field_74936_d = j / k;
                    field_74936_d=maxY;
                    this.boundingBox.offset(0, this.field_74936_d - this.boundingBox.minY + p_74935_3_, 0);
                    return true;
                }
            }
        }

        @Override
        public boolean addComponentParts(World w, Random rand, StructureBoundingBox box) {
            if (!this.func_74935_a(w, box, 0)){
                return false;
            }

            int west,east,north,south;

            fillWithAir(w, box, 0,0,0,26,8,16);

            //-----------------------------------------------------------------
            //                 階段の設置
            //-----------------------------------------------------------------
            west=getMetadataWithOffset(Blocks.oak_stairs, 4);
            east=getMetadataWithOffset(Blocks.oak_stairs, 5);
            north=getMetadataWithOffset(Blocks.oak_stairs, 2);
            south=getMetadataWithOffset(Blocks.oak_stairs, 3);
            fillWithMetadataBlocks(w, box, 12, 0, 1, 14, 0, 1, Blocks.oak_stairs, south, Blocks.oak_stairs, south, false);
            fillWithMetadataBlocks(w, box, 12, 1, 2, 14, 1, 2, Blocks.oak_stairs, south, Blocks.oak_stairs, south, false);

            fillWithMetadataBlocks(w, box, 11, 0, 0, 11, 0, 2, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 11, 1, 1, 11, 1, 2, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 11, 2, 2, 11, 2, 2, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 15, 0, 0, 15, 0, 2, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 15, 1, 1, 15, 1, 2, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 15, 2, 2, 15, 2, 2, Blocks.fence, 0, Blocks.fence, 0, false);

            //-----------------------------------------------------------------
            //                 屋根の設置
            //-----------------------------------------------------------------
            west=getMetadataWithOffset(Blocks.stone_brick_stairs, 0);
            east=getMetadataWithOffset(Blocks.stone_brick_stairs, 1);
            north=getMetadataWithOffset(Blocks.stone_brick_stairs, 2);
            south=getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
            fillWithMetadataBlocks(w, box, 0, 5, 2,  26, 5, 2,   Blocks.stone_brick_stairs, south, Blocks.stone_brick_stairs, south, false);
            fillWithMetadataBlocks(w, box, 0, 5, 16, 26, 5, 16,  Blocks.stone_brick_stairs, north, Blocks.stone_brick_stairs, north, false);
            fillWithMetadataBlocks(w, box, 0, 5, 3,  0, 5, 15,   Blocks.stone_brick_stairs, west, Blocks.stone_brick_stairs, west, false);
            fillWithMetadataBlocks(w, box, 26, 5, 3, 26, 5, 15,  Blocks.stone_brick_stairs, east, Blocks.stone_brick_stairs, east, false);

            fillWithMetadataBlocks(w, box, 1, 6, 3,  25, 6, 3,   Blocks.stone_brick_stairs, south, Blocks.stone_brick_stairs, south, false);
            fillWithMetadataBlocks(w, box, 1, 6, 15, 25, 6, 15,  Blocks.stone_brick_stairs, north, Blocks.stone_brick_stairs, north, false);
            fillWithMetadataBlocks(w, box, 1, 6, 4,  1, 6, 14,   Blocks.stone_brick_stairs, west, Blocks.stone_brick_stairs, west, false);
            fillWithMetadataBlocks(w, box, 25, 6, 4, 25, 6, 14,  Blocks.stone_brick_stairs, east, Blocks.stone_brick_stairs, east, false);

            fillWithMetadataBlocks(w, box, 2, 7, 4,  24, 7, 4,   Blocks.stone_brick_stairs, south, Blocks.stone_brick_stairs, south, false);
            fillWithMetadataBlocks(w, box, 2, 7, 14, 24, 7, 14,  Blocks.stone_brick_stairs, north, Blocks.stone_brick_stairs, north, false);
            fillWithMetadataBlocks(w, box, 2, 7, 5,  2, 7, 13,   Blocks.stone_brick_stairs, west, Blocks.stone_brick_stairs, west, false);
            fillWithMetadataBlocks(w, box, 24, 7, 5, 24, 7, 13,  Blocks.stone_brick_stairs, east, Blocks.stone_brick_stairs, east, false);

            fillWithMetadataBlocks(w, box, 3, 8, 5, 23, 8, 13,  Blocks.stone_slab, 5, Blocks.stone_slab, 5, false);

            //-----------------------------------------------------------------
            //                 土台の設置
            //-----------------------------------------------------------------
            //原木の向き
            west=east=(coordBaseMode==0 || coordBaseMode==2)?8:4;
            north=south=(coordBaseMode==0 || coordBaseMode==2)?4:8;

            //柵　基礎
            fillWithMetadataBlocks(w, box, 1, 0, 3,     25, 0, 3, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 1, 0, 15,    25, 0, 15, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 1, 0, 4,     1, 0, 14, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 25, 0, 4,    25, 0, 14, Blocks.fence, 0, Blocks.fence, 0, false);
            int my;

            /*
            for(int dx=boundingBox.minX;dx<=boundingBox.maxX;dx++){
                for(my=w.getHeightValue(dx, boundingBox.minZ);my<=boundingBox.minY;my++){
                    w.setBlock(dx, my, boundingBox.minZ, Blocks.fence);
                }
                for(my=w.getHeightValue(dx, boundingBox.maxZ);my<=boundingBox.minY;my++){
                    w.setBlock(dx, my, boundingBox.minZ, Blocks.fence);
                }
            }
            for(int dz=boundingBox.minZ+1;dz<=boundingBox.maxZ-1;dz++){
                for(my=w.getHeightValue(boundingBox.minX, dz);my<=boundingBox.minY;my++){
                    w.setBlock(boundingBox.minX, my, dz, Blocks.fence);
                }
                for(my=w.getHeightValue(boundingBox.maxX, dz);my<=boundingBox.minY;my++){
                    w.setBlock(boundingBox.maxX, my, dz, Blocks.fence);
                }
            }
            */

            //木材
            fillWithMetadataBlocks(w, box, 1,1,3,       25,1,15, Blocks.planks, 0, Blocks.planks, 0, false);

            //原木　外枠
            fillWithMetadataBlocks(w, box, 1, 1, 3,     25, 1, 3, Blocks.log, south, Blocks.log, south, false);
            fillWithMetadataBlocks(w, box, 1, 1, 15,    25, 1, 15, Blocks.log, south, Blocks.log, south, false);
            fillWithMetadataBlocks(w, box, 1, 1, 4,     1, 1, 14, Blocks.log, west, Blocks.log, west, false);
            fillWithMetadataBlocks(w, box, 25, 1, 4,    25, 1, 14, Blocks.log, west, Blocks.log, west, false);

            //原木　内枠
            fillWithMetadataBlocks(w, box, 4, 1, 6,     22, 1, 6, Blocks.log, south, Blocks.log, south, false);
            fillWithMetadataBlocks(w, box, 4, 1, 12,    22, 1, 12, Blocks.log, west, Blocks.log, west, false);
            for(int i=0;i<4;i++){
                fillWithMetadataBlocks(w, box, 4+i*6, 1, 7, 4+i*6, 1, 11, Blocks.log, west, Blocks.log, west, false);
            }

            //-----------------------------------------------------------------
            //                 障子の設置
            //-----------------------------------------------------------------
            fillWithMetadataBlocks(w, box, 4, 2, 6, 22, 4, 12, Blocks.wool, 0, Blocks.wool, 0, false);
            for(int i=0;i<3;i++){
                fillWithAir(w, box, 5+i*6,2,7, 9+i*6,4,11);
            }

            //-----------------------------------------------------------------
            //                 柵の設置
            //-----------------------------------------------------------------
            fillWithMetadataBlocks(w, box, 1, 2, 3,     11, 2, 3,  Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 15, 2, 3,    25, 2, 3,  Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 1, 2, 15,    25, 2, 15, Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 1, 2, 3,     1, 2, 15,  Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 25, 2, 3,    25, 2, 15, Blocks.fence, 0, Blocks.fence, 0, false);

            fillWithMetadataBlocks(w, box, 4, 6, 6,     22, 7, 6,  Blocks.fence, 0, Blocks.fence, 0, false);
            fillWithMetadataBlocks(w, box, 4, 6, 12,    22, 7, 12, Blocks.fence, 0, Blocks.fence, 0, false);
            for(int i=0;i<4;i++){
                fillWithMetadataBlocks(w, box, 4+6*i, 6, 6,     4+6*i, 7, 12,  Blocks.fence, 0, Blocks.fence, 0, false);
            }


            for(int i=0;i<11;i++){
                if(i!=5) fillWithMetadataBlocks(w, box, 3+2*i, 3, 3, 3+2*i, 4, 3, Blocks.fence, 0, Blocks.fence, 0, false);
                fillWithMetadataBlocks(w, box, 3+2*i, 3, 15, 3+2*i, 4, 15, Blocks.fence, 0, Blocks.fence, 0, false);
            }
            for(int i=0;i<5;i++){
                fillWithMetadataBlocks(w, box, 1, 3, 5+2*i, 1, 4, 5+2*i, Blocks.fence, 0, Blocks.fence, 0, false);
                fillWithMetadataBlocks(w, box, 25, 3, 5+2*i, 25, 4, 5+2*i, Blocks.fence, 0, Blocks.fence, 0, false);
            }

            //-----------------------------------------------------------------
            //                 枠組みの設置
            //-----------------------------------------------------------------
            fillWithMetadataBlocks(w, box, 1, 5, 3, 25, 5, 3, Blocks.log, south, Blocks.log, south, false);
            fillWithMetadataBlocks(w, box, 1, 5, 15, 25, 5, 15, Blocks.log, south, Blocks.log, south, false);
            fillWithMetadataBlocks(w, box, 1, 5, 3, 1, 5, 15, Blocks.log, west, Blocks.log, west, false);
            fillWithMetadataBlocks(w, box, 25, 5, 3, 25, 5, 15, Blocks.log, west, Blocks.log, west, false);

            fillWithMetadataBlocks(w, box, 1, 0, 3, 1, 5, 3, Blocks.log, 0, Blocks.log, 0, false);
            fillWithMetadataBlocks(w, box, 1, 0, 15, 1, 5, 15, Blocks.log, 0, Blocks.log, 0, false);
            fillWithMetadataBlocks(w, box, 25, 0, 3, 25, 5, 3, Blocks.log, 0, Blocks.log, 0, false);
            fillWithMetadataBlocks(w, box, 25, 0, 15, 25, 5, 15, Blocks.log, 0, Blocks.log, 0, false);

            fillWithMetadataBlocks(w, box, 4, 5, 6,  22, 5, 6, Blocks.log, south, Blocks.log, south, false);
            fillWithMetadataBlocks(w, box, 4, 5, 12,  22, 5, 12, Blocks.log, south, Blocks.log, south, false);
            for(int i=0;i<4;i++){
                fillWithMetadataBlocks(w, box, 4+i*6, 5, 7,  4+i*6, 5, 12, Blocks.log, west, Blocks.log, west, false);
                fillWithMetadataBlocks(w, box, 4+i*6, 0, 6,  4+i*6, 7, 6, Blocks.log, 0, Blocks.log, 0, false);
                fillWithMetadataBlocks(w, box, 4+i*6, 0, 12, 4+i*6,  7, 12, Blocks.log, 0, Blocks.log, 0, false);
            }


            //-----------------------------------------------------------------
            //                 本棚の設置
            //-----------------------------------------------------------------
            for(int i=0;i<3;i++){
                if(!judged[i]){
                    judged[i]=true;
                    hasShelf[i]=rand.nextFloat()<0.65f;
                }
                if(hasShelf[i]) {
                    for (int xx = 6; xx <= 8; xx++) {
                        for (int zz = 8; zz <= 10; zz++) {
                            placeBlockAtCurrentPosition(w, BlockCore.bookshelf, getNum(rand, 4), xx + 6 * i, 2, zz, box);
                        }
                    }
                }
            }

            //-----------------------------------------------------------------
            //                 たいまつの設置
            //-----------------------------------------------------------------
            int[]tx={5, 7,  9, 7};
            int[]tz={9, 11, 9, 7};

            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 7, 5, 5, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 13, 5, 5, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 19, 5, 5, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 7, 5, 13, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 13, 5, 13, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 19, 5, 13, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 7, 5, 5, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 13, 5, 5, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 3, 5, 9, box);
            placeBlockAtCurrentPosition(w, Blocks.torch, 0, 23, 5, 9, box);
            for(int k=0;k<3;k++){
                for(int i=0;i<4;i++){
                    placeBlockAtCurrentPosition(w, Blocks.torch, 0, tx[i]+k*6, 5, tz[i], box);
                }
            }

            return true;
        }

        protected int getNum(Random rand, int max){
            if(rand.nextFloat()<=0.8f){
                return 1+rand.nextInt(max);
            }
            return 0;
        }
    }
}
