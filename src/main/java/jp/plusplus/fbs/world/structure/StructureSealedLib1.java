package jp.plusplus.fbs.world.structure;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 * 1つの図書館からだいたい16.8冊手に入る？
 */
public class StructureSealedLib1 extends StructureComponent {
    private boolean chest=false;

    public StructureSealedLib1(){}
    public StructureSealedLib1(int par1, Random rand, int x, int y, int z) {
        this.coordBaseMode = 0;

        //y=6+rand.nextInt(40-6+1);
        this.boundingBox = new StructureBoundingBox(x, y, z, x + 11, y+6, z + 11);
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
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) {
        int[] bx={1,1,2,3, 7,8,9,9, 9,9,8,7, 3,2,1,1};
        int[] bz={3,2,1,1, 1,1,2,3, 7,8,9,9, 9,9,8,7};
        int[] wx={1,9,9,1};
        int[] wz={1,1,9,9};
        int[] tx={1,5,9,5};
        int[] tz={5,1,5,9};

        this.fillWithBlocks(world, box, 0, 0, 0, 10, 5, 10, BlockCore.plank, BlockCore.plank, false);
        this.fillWithBlocks(world, box, 1, 1, 1, 9, 4, 9, Blocks.air, Blocks.air, false);

        for(int i=0;i<bx.length;i++){
            for(int k=0;k<4;k++){
                this.placeBlockAtCurrentPosition(world, BlockCore.bookshelf, getNum(rand, 2), bx[i], 1+k, bz[i], box);
            }
        }
        for(int i=0;i<wx.length;i++){
            for(int k=0;k<4;k++){
                this.placeBlockAtCurrentPosition(world, BlockCore.plank, 0, wx[i], 1+k, wz[i], box);
            }
        }

        for(int i=0;i<3;i++){
            for(int n=0;n<3;n++){
                for(int k=0;k<4;k++){
                    if(i==1 && n==1){
                        //if(k>0) this.placeBlockAtCurrentPosition(world, BlockCore.plank, 0, 4+i, 1+k, 4+n, box);
                    }
                    else this.placeBlockAtCurrentPosition(world, BlockCore.bookshelf, getNum(rand, 4), 4+i, 1+k, 4+n, box);
                }
            }
        }
        /*
        for(int k=0;k<4;k++){
            this.placeBlockAtCurrentPosition(world, BlockCore.plank, 0, 5, 1+k, 5, box);
        }
        */

        for(int i=0;i<tx.length;i++){
            this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, tx[i], 2, tz[i], box);
        }

        //chest
        if(!chest){
            int cx=getXWithOffset(5, 5), cy=getYWithOffset(1), cz=getZWithOffset(5, 5);

            if(box.isVecInside(cx, cy, cz) && world.getBlock(cx, cy, cz)!=Blocks.chest){
                world.setBlock(cx, cy, cz, Blocks.chest, 0, 2);
                TileEntity te=world.getTileEntity(cx,cy,cz);
                if(te instanceof TileEntityChest){
                    TileEntityChest e=(TileEntityChest)te;
                    ItemStack[] items=new ItemStack[e.getSizeInventory()];
                    Registry.GetChestContents(0, items, 0.15f);
                    for(int i=0;i<items.length;i++){
                        e.setInventorySlotContents(i, items[i]);
                    }
                }
                chest=true;
            }
        }
        //placeBlockAtCurrentPosition(world, Blocks.chest, 0, 5, 1, 5, box);

        //world.setBlock(i1, j1, k1, Blocks.chest, 0, 2);

        //FMLLog.severe("generated at "+box.getCenterX()+","+box.getCenterY()+","+box.getCenterZ());
        return true;
    }

    protected int getNum(Random rand, int max){
        if(rand.nextFloat()<=0.15f){
            return 1+rand.nextInt(max);
        }
        return 0;
    }
}
