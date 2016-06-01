package jp.plusplus.fbs.world.crack.structure;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.BlockSchoolTable;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/08.
 * がっこう！
 */
public class MapGenSchool extends MapGenStructure {
    public static final int HALL_WIDTH =5;
    public static final int FLOOR_HEIGHT =6;
    public static final int ROOM_SIZE =9;
    public static final int HALL1_NUM =3;
    public static final int FLOOR_NUM =3;

    @Override
    public String func_143025_a() {
        return "School";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        return p_75047_1_==0 & p_75047_2_==0;
    }

    @Override
    protected StructureStart getStructureStart(int i, int j) {
        return new Start(this.worldObj, this.rand, i, j);
    }

    public static class Start extends StructureStart{
        public Start(){}

        public Start(World p_i2060_1_, Random rand, int x, int z) {
            super(x, z);// 構造物の構成パーツを決定する
            // 基点はComponentSampleDungeon1

            //高さを求める
            int maxY=300;
            for(int i=0;i<16;i++){
                for(int k=0;k<16;k++){
                    int t= p_i2060_1_.getHeightValue(x, z);
                    if(maxY>t) maxY=t;
                }
            }

            Hall1 hall1 = new Hall1(0, rand, (x << 4) + 2, maxY, (z << 4) + 2);
            this.components.add(hall1);

            // 次のパーツを得る
            hall1.buildComponent(hall1, components, rand);

            // 次のパーツが決定していないパーツは一時的にstructureComponentsに保持されるので、空になるまで次のパーツの決定を続ける
            List<StructureComponent> list = hall1.structureComponents;
            while(!list.isEmpty()) {
                int k = rand.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(k);
                structurecomponent.buildComponent(hall1, this.components, rand);
            }

            // 構造物全体の占有範囲を更新する
            this.updateBoundingBox();
        }
    }

    public static class Hall1 extends StructureComponent{
        // 構成パーツリストを記憶するためのリスト
        public ArrayList<StructureComponent> structureComponents = new ArrayList();
        public int count;

        public Hall1() {}
        public Hall1(int par1, Random par2Random, int x, int y, int z) {
            this(par1, par2Random, x, y, z, 3, 0);
        }
        public Hall1(int par1, Random par2Random, int x, int y, int z, int dir, int c){
            this.coordBaseMode = dir;
            switch(this.coordBaseMode) {
                case 0:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ HALL_WIDTH,y+ FLOOR_HEIGHT,z+ ROOM_SIZE);
                    break;
                case 1:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ ROOM_SIZE,y+ FLOOR_HEIGHT,z+ HALL_WIDTH);
                    break;
                case 2:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ HALL_WIDTH,y+ FLOOR_HEIGHT,z+ ROOM_SIZE);
                    break;
                case 3:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ ROOM_SIZE,y+ FLOOR_HEIGHT,z+ HALL_WIDTH);
                    break;
            }
            count=c;
        }

        @Override
        public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
            FBS.logger.info("coord:"+coordBaseMode);

            //廊下
            int cx = this.boundingBox.minX, cy = this.boundingBox.minY, cz = this.boundingBox.minZ;
            switch (this.coordBaseMode) {
                case 0:
                    cx = this.boundingBox.minX;
                    cz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                    break;
                case 1:
                    cx = this.boundingBox.maxX + 1;
                    cz = this.boundingBox.minZ;
                    break;
                case 2:
                    cx = this.boundingBox.minX;
                    cz = this.boundingBox.maxZ + 1;
                    break;
                case 3:
                    cx = this.boundingBox.minX - (ROOM_SIZE + 1);
                    cz = this.boundingBox.minZ;
                    break;
            }
            Hall2 h1 = new Hall2(0, par3Random, cx, cy, cz, coordBaseMode, 0, true);
            ((Hall1) par1StructureComponent).structureComponents.add(h1);
            par2List.add(h1);

            switch (this.coordBaseMode) {
                case 2:
                    cx = this.boundingBox.minX;
                    cz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                    break;
                case 3:
                    cx = this.boundingBox.maxX + 1;
                    cz = this.boundingBox.minZ;
                    break;
                case 0:
                    cx = this.boundingBox.minX;
                    cz = this.boundingBox.maxZ + 1;
                    break;
                case 1:
                    cx = this.boundingBox.minX - (ROOM_SIZE + 1);
                    cz = this.boundingBox.minZ;
                    break;
            }
            h1 = new Hall2(0, par3Random, cx, cy, cz, coordBaseMode, 0, false);
            ((Hall1) par1StructureComponent).structureComponents.add(h1);
            par2List.add(h1);

            //階段
            if(count<FLOOR_NUM){
                int rx = boundingBox.minX, ry = boundingBox.minY, rz = boundingBox.minZ;
                switch (this.coordBaseMode) {
                    case 0:
                        rx = this.boundingBox.minX - (ROOM_SIZE + 1);
                        rz = this.boundingBox.minZ;
                        break;
                    case 1:
                        rx = this.boundingBox.maxX;
                        rz = this.boundingBox.minZ + (ROOM_SIZE + 1);
                        break;
                    case 2:
                        rx = this.boundingBox.minX - (ROOM_SIZE + 1);
                        rz = this.boundingBox.maxZ;
                        break;
                    case 3:
                        rx = this.boundingBox.minX;
                        rz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                        break;
                }
                Stairway rm = new Stairway(0, par3Random, rx, ry, rz, coordBaseMode, count);
                ((Hall1) par1StructureComponent).structureComponents.add(rm);
                par2List.add(rm);
            }
        }

        @Override
        protected void func_143012_a(NBTTagCompound nbttagcompound) {}
        @Override
        protected void func_143011_b(NBTTagCompound nbttagcompound) {}

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            // 占有範囲(structureboundingbox)内の指定範囲を指定ブロック＆メタデータで埋める
            // 占有範囲(structureboundingbox)内の指定範囲を空気ブロックで埋める
            this.fillWithMetadataBlocks(world, structureboundingbox, 0, 0, 0, HALL_WIDTH, FLOOR_HEIGHT, ROOM_SIZE, BlockCore.plank, 0, Blocks.air, 0, false);
            this.fillWithAir(world, structureboundingbox, 1, 1, 1, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, ROOM_SIZE - 1);

            //道を空ける
            this.fillWithAir(world, structureboundingbox, 1, 1, 0, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, 0);
            this.fillWithAir(world, structureboundingbox, 1, 1, ROOM_SIZE, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, ROOM_SIZE);

            //接続用の道を空ける
            this.fillWithAir(world, structureboundingbox, 0, 1, 1, 0, FLOOR_HEIGHT - 1, ROOM_SIZE - 1);
            if(count==0) this.fillWithAir(world, structureboundingbox, HALL_WIDTH, 1, 3, HALL_WIDTH, 3, 6);
            return true;
        }
    }
    public static class Hall2 extends Hall1{
        boolean nextDir;
        public Hall2(int par1, Random par2Random, int x, int y, int z, int dir, int c, boolean next){
            super(par1, par2Random, x, y, z, dir, c);
            nextDir=next;
        }

        @Override
        public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
            //廊下
            if (count < HALL1_NUM) {
                int cx = this.boundingBox.minX, cy = this.boundingBox.minY, cz = this.boundingBox.minZ;
                if (nextDir) {
                    //入り口から向かって右(北側)
                    switch (this.coordBaseMode) {
                        case 0:
                            cx = this.boundingBox.minX;
                            cz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                            break;
                        case 1:
                            cx = this.boundingBox.maxX + 1;
                            cz = this.boundingBox.minZ;
                            break;
                        case 2:
                            cx = this.boundingBox.minX;
                            cz = this.boundingBox.maxZ + 1;
                            break;
                        case 3:
                            cx = this.boundingBox.minX - (ROOM_SIZE + 1);
                            cz = this.boundingBox.minZ;
                            break;
                    }
                } else {
                    switch (this.coordBaseMode) {
                        case 2:
                            cx = this.boundingBox.minX;
                            cz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                            break;
                        case 3:
                            cx = this.boundingBox.maxX + 1;
                            cz = this.boundingBox.minZ;
                            break;
                        case 1:
                            cx = this.boundingBox.minX;
                            cz = this.boundingBox.maxZ + 1;
                            break;
                        case 0:
                            cx = this.boundingBox.minX - (ROOM_SIZE + 1);
                            cz = this.boundingBox.minZ;
                            break;
                    }
                }
                Hall2 h2 = new Hall2(0, par3Random, cx, cy, cz, coordBaseMode, count + 1, nextDir);
                ((Hall1) par1StructureComponent).structureComponents.add(h2);
                par2List.add(h2);
            }
            //ランダムで部屋を1つ
            int rx = boundingBox.minX, ry = boundingBox.minY, rz = boundingBox.minZ;
            switch (this.coordBaseMode) {
                case 0:
                    rx = this.boundingBox.minX - (ROOM_SIZE + 1);
                    rz = this.boundingBox.minZ;
                    break;
                case 1:
                    rx = this.boundingBox.maxX;
                    rz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                    break;
                case 2:
                    rx = this.boundingBox.minX - (ROOM_SIZE + 1);
                    rz = this.boundingBox.maxZ;
                    break;
                case 3:
                    rx = this.boundingBox.minX;
                    rz = this.boundingBox.minZ - (ROOM_SIZE + 1);
                    break;
            }

            RoomBase rm;
            int rr=par3Random.nextInt(3);
            if(rr==0) rm = new RoomStudy(0, par3Random, rx, ry, rz, coordBaseMode);
            else if(rr==1) rm = new RoomZombie(0, par3Random, rx, ry, rz, coordBaseMode);
            else rm = new RoomClassroom(0, par3Random, rx, ry, rz, coordBaseMode);

            ((Hall1) par1StructureComponent).structureComponents.add(rm);
            par2List.add(rm);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            // 占有範囲(structureboundingbox)内の指定範囲を指定ブロック＆メタデータで埋める
            // 占有範囲(structureboundingbox)内の指定範囲を空気ブロックで埋める
            this.fillWithMetadataBlocks(world, structureboundingbox, 0, 0, 0, HALL_WIDTH, FLOOR_HEIGHT, ROOM_SIZE, BlockCore.plank, 0, Blocks.air, 0, false);
            this.fillWithAir(world, structureboundingbox, 1, 1, 1, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, ROOM_SIZE - 1);

            //道を空ける
            if (count == HALL1_NUM) {
                if (nextDir)
                    this.fillWithAir(world, structureboundingbox, 1, 1, ROOM_SIZE, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, ROOM_SIZE);
                else this.fillWithAir(world, structureboundingbox, 1, 1, 0, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, 0);
            } else {
                this.fillWithAir(world, structureboundingbox, 1, 1, 0, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, 0);
                if (count < 5)
                    this.fillWithAir(world, structureboundingbox, 1, 1, ROOM_SIZE, HALL_WIDTH - 1, FLOOR_HEIGHT - 1, ROOM_SIZE);
            }

            //接続用の道を空ける
            this.fillWithAir(world, structureboundingbox, 0, 1, 2, 0, 3, 3);
            this.fillWithAir(world, structureboundingbox, 0, 1, 6, 0, 3, 7);

            //窓
            this.fillWithMetadataBlocks(world, structureboundingbox, HALL_WIDTH, 2, 2, HALL_WIDTH, FLOOR_HEIGHT - 2, 7, Blocks.glass_pane, 0, Blocks.air, 0, false);

            return true;
        }
    }

    public static class Stairway extends StructureComponent{
        public int count;
        public Stairway() {}
        public Stairway(int par1, Random par2Random, int x, int y, int z, int dir, int count){
            this.coordBaseMode = dir;
            this.count=count;
            switch(this.coordBaseMode) {
                case 0:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ ROOM_SIZE,y+ FLOOR_HEIGHT*2,z+ ROOM_SIZE);
                    break;
                case 1:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ ROOM_SIZE,y+ FLOOR_HEIGHT*2,z+ ROOM_SIZE);
                    break;
                case 2:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ ROOM_SIZE,y+ FLOOR_HEIGHT*2,z+ ROOM_SIZE);
                    break;
                case 3:
                    this.boundingBox=new StructureBoundingBox(x,y,z,x+ ROOM_SIZE,y+ FLOOR_HEIGHT*2,z+ ROOM_SIZE);
                    break;
            }
        }

        @Override
        public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
            //階段
            int rx = boundingBox.minX, ry = boundingBox.maxY-FLOOR_HEIGHT, rz = boundingBox.minZ;

            switch (this.coordBaseMode) {
                case 2:
                    rx = this.boundingBox.maxX+1;
                    rz = this.boundingBox.minZ;
                    break;
                case 3:
                    rx = this.boundingBox.minX;
                    rz = this.boundingBox.maxZ+1;
                    break;
                case 0:
                    rx = this.boundingBox.minX-(ROOM_SIZE+1);
                    rz = this.boundingBox.minZ;
                    break;
                case 1:
                    rx = this.boundingBox.minX;
                    rz = this.boundingBox.maxZ-(ROOM_SIZE+1);
                    break;
            }
            /*
            switch (this.coordBaseMode) {
                case 2:
                    rx = this.boundingBox.maxX+1;
                    rz = this.boundingBox.minZ+ROOM_SIZE;
                    break;
                case 3:
                    rx = this.boundingBox.maxX+ROOM_SIZE;
                    rz = this.boundingBox.maxZ+1;
                    break;
                case 0:
                    rx = this.boundingBox.minX - (HALL_WIDTH + 1);
                    rz = this.boundingBox.minZ;
                    break;
                case 1:
                    rx = this.boundingBox.minX - ROOM_SIZE;
                    rz = this.boundingBox.minZ - (HALL_WIDTH + 1);
                    break;
            }
            */

            Hall1 h1 = new Hall1(0, par3Random, rx, ry, rz, coordBaseMode, count + 1);
            ((Hall1) par1StructureComponent).structureComponents.add(h1);
            par2List.add(h1);
        }

        @Override
        protected void func_143012_a(NBTTagCompound p_143012_1_) {}

        @Override
        protected void func_143011_b(NBTTagCompound p_143011_1_) {}

        @Override
        public boolean addComponentParts(World world, Random p_74875_2_, StructureBoundingBox structureboundingbox) {
            this.fillWithMetadataBlocks(world, structureboundingbox, 0, 0, 0, ROOM_SIZE, FLOOR_HEIGHT*2, ROOM_SIZE, BlockCore.plank, 0, Blocks.air, 0, false);
            this.fillWithAir(world, structureboundingbox, 1, 1, 1, ROOM_SIZE-1, FLOOR_HEIGHT*2-1, ROOM_SIZE-1);

            int metaL=getMetadataWithOffset(Blocks.oak_stairs, 1), metaR=getMetadataWithOffset(Blocks.oak_stairs, 0);

            //接続穴
            this.fillWithAir(world, structureboundingbox, ROOM_SIZE, 1, 1, ROOM_SIZE, FLOOR_HEIGHT*2-1, ROOM_SIZE-1);
            if(count>0){
                this.fillWithAir(world, structureboundingbox, 1, 0, 1, ROOM_SIZE, 0, ROOM_SIZE-1);

                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE, 0, 3, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE, 0, 4, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE, 0, 5, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE, 0, 6, structureboundingbox);
            }

            //階段 0x4=0,
            for(int i=0;i<3;i++){
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaL, ROOM_SIZE-i, 1+i, 1, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaL, ROOM_SIZE-i, 1+i, 2, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaL, ROOM_SIZE-i, 1+i, 7, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaL, ROOM_SIZE-i, 1+i, 8, structureboundingbox);

                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE+i-2, 4+i, 3, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE+i-2, 4+i, 4, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE+i-2, 4+i, 5, structureboundingbox);
                this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, metaR, ROOM_SIZE+i-2, 4+i, 6, structureboundingbox);
            }

            this.fillWithMetadataBlocks(world, structureboundingbox, 1, 3, 1, 6, 3, ROOM_SIZE-1, BlockCore.plank, 0, Blocks.air, 0, false);

            return true;
        }

    }

    public static class Entrance extends StructureComponent{
        public Entrance() {}
        public Entrance(int par1, Random par2Random, int x, int y, int z, int dir) {
            this.coordBaseMode=dir;
            this.boundingBox=new StructureBoundingBox(x,y,z,x+ HALL_WIDTH,y+ FLOOR_HEIGHT,z+ HALL_WIDTH);
        }

        @Override
        protected void func_143012_a(NBTTagCompound p_143012_1_) {}

        @Override
        protected void func_143011_b(NBTTagCompound p_143011_1_) {}

        @Override
        public boolean addComponentParts(World world, Random p_74875_2_, StructureBoundingBox structureboundingbox) {
            this.fillWithMetadataBlocks(world, structureboundingbox, 0, 0, 0, HALL_WIDTH, FLOOR_HEIGHT, HALL_WIDTH, BlockCore.plank, 0, Blocks.air, 0, false);
            this.fillWithAir(world, structureboundingbox, 1, 1, 1, HALL_WIDTH -1, FLOOR_HEIGHT -1, HALL_WIDTH -1);

            this.fillWithAir(world, structureboundingbox, 1, 1, 0, HALL_WIDTH-1, 3, 0);
            this.fillWithAir(world, structureboundingbox, 1, 1, HALL_WIDTH, HALL_WIDTH-1, 3, HALL_WIDTH);

            return true;
        }

    }

    public static class RoomBase extends StructureComponent {
        public RoomBase() {}
        public RoomBase(int par1, Random par2Random, int x, int y, int z, int dir) {
            this.coordBaseMode = dir;
            this.boundingBox = new StructureBoundingBox(x, y, z, x + ROOM_SIZE, y+FLOOR_HEIGHT, z + ROOM_SIZE);
        }

        protected boolean hasWindow(){ return true; }

        @Override
        protected void func_143012_a(NBTTagCompound nbttagcompound) {}
        @Override
        protected void func_143011_b(NBTTagCompound nbttagcompound) {}

        @Override
        public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random) {
            //何にも派生しない
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            this.fillWithMetadataBlocks(world, structureboundingbox, 0, 0, 0, ROOM_SIZE, FLOOR_HEIGHT, ROOM_SIZE, BlockCore.plank, 0, Blocks.air, 0, false);
            this.fillWithAir(world, structureboundingbox, 1, 1, 1, ROOM_SIZE-1, FLOOR_HEIGHT-1, ROOM_SIZE-1);

            this.fillWithAir(world, structureboundingbox, ROOM_SIZE, 1, 2, ROOM_SIZE, 3, 3);
            this.fillWithAir(world, structureboundingbox, ROOM_SIZE, 1, 6, ROOM_SIZE, 3, 7);

            if(hasWindow()){
                this.fillWithMetadataBlocks(world, structureboundingbox, 0, 2, 2, 0, FLOOR_HEIGHT - 2, 3, Blocks.glass_pane, 0, Blocks.air, 0, false);
                this.fillWithMetadataBlocks(world, structureboundingbox, 0, 2, 6, 0, FLOOR_HEIGHT - 2, 7, Blocks.glass_pane, 0, Blocks.air, 0, false);
            }

            return true;
        }
    }

    public static class RoomStudy extends RoomBase{
        public RoomStudy() {}
        public RoomStudy(int par1, Random par2Random, int x, int y, int z, int dir) {
            super(par1, par2Random, x, y, z, dir);}

        @Override
        protected boolean hasWindow(){ return true; }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            super.addComponentParts(world, random, structureboundingbox);

            //本
            for(int y=1;y<=FLOOR_HEIGHT-2;y++){
                for(int z=1;z<=ROOM_SIZE-1;z++){
                    //this.placeBlockAtCurrentPosition(world, BlockCore.bookshelf, getBookNum(random, 3), 1, y, z, structureboundingbox);

                    if(z!=4 && z!=5){
                        this.placeBlockAtCurrentPosition(world, BlockCore.bookshelf, getBookNum(random, 3), 3, y, z, structureboundingbox);
                        this.placeBlockAtCurrentPosition(world, BlockCore.bookshelf, getBookNum(random, 3), 5, y, z, structureboundingbox);
                    }
                    else {
                        this.placeBlockAtCurrentPosition(world, BlockCore.bookshelf, getBookNum(random, 3), 7, y, z, structureboundingbox);
                    }
                }
            }

            return true;
        }

        protected int getBookNum(Random rand, int max){
            return 2+rand.nextInt(max);
        }
    }

    public static class RoomClassroom extends RoomBase{
        public RoomClassroom() {}
        public RoomClassroom(int par1, Random par2Random, int x, int y, int z, int dir) {
            super(par1, par2Random, x, y, z, dir);
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            super.addComponentParts(world, random, structureboundingbox);

            int meta=0;
            switch (coordBaseMode){
                case 0:
                    meta=2;
                    break;
                case 1:
                    meta=5;
                    break;
                case 2:
                    meta=3;
                    break;
                case 3:
                    meta=4;
                    break;
            }

            //学校の机
            for(int i=0;i<3;i++){
                for(int k=0;k<4;k++){
                    placeBlockAtCurrentPosition(world, BlockCore.schoolTable, meta, 1+k*2, 1, 2+i*2, structureboundingbox);
                }
            }

            //教壇
            fillWithMetadataBlocks(world, structureboundingbox, 2, 1, 8, 7, 1, 8, Blocks.wooden_slab, 0, Blocks.air, 0, false);

            return true;
        }
    }

    public static class RoomZombie extends RoomBase{
        private boolean chest=false;

        public RoomZombie() {}
        public RoomZombie(int par1, Random par2Random, int x, int y, int z, int dir) {
            super(par1, par2Random, x, y, z, dir);
        }

        @Override
        protected void func_143012_a(NBTTagCompound tag) {
            tag.setBoolean("Chest", this.chest);
        }
        @Override
        protected void func_143011_b(NBTTagCompound tag) {
            chest = tag.getBoolean("Chest");
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
            super.addComponentParts(world, random, structureboundingbox);

            placeBlockAtCurrentPosition(world, Blocks.mob_spawner, 0, 4, 1, 4, structureboundingbox);
            TileEntity te=world.getTileEntity(getXWithOffset(4, 4), getYWithOffset(1), getZWithOffset(4, 4));
            if(te instanceof TileEntityMobSpawner){
                ((TileEntityMobSpawner) te).func_145881_a().setEntityName("Zombie");
                te.markDirty();
            }

            //chest
            if(!chest){
                int cx=getXWithOffset(1, ROOM_SIZE-1), cy=getYWithOffset(1), cz=getZWithOffset(1, ROOM_SIZE-1);
                if(structureboundingbox.isVecInside(cx, cy, cz) && world.getBlock(cx, cy, cz)!=Blocks.chest){
                    world.setBlock(cx, cy, cz, Blocks.chest, 0, 2);
                    te=world.getTileEntity(cx,cy,cz);
                    if(te instanceof TileEntityChest){
                        TileEntityChest e=(TileEntityChest)te;
                        ItemStack[] items=new ItemStack[e.getSizeInventory()];
                        Registry.GetChestContents(2, items, 0.25f);
                        for(int i=0;i<items.length;i++){
                            e.setInventorySlotContents(i, items[i]);
                        }
                    }
                    chest=true;
                }
            }

            return true;
        }
    }
}
