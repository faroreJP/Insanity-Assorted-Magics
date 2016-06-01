package jp.plusplus.fbs.world.structure;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class StructureSealedLibStart extends StructureStart {

    public StructureSealedLibStart() {}

    public StructureSealedLibStart(World par1World, Random par2Random, int par3, int par4) {
        super(par3, par4);
        // 構造物の構成パーツを決定する

        //decides Position
        int x=par3*16+2,y,z=par4*16+2;
        /*
        int sy=0, sym=8*par2Random.nextInt(4);
        Block b2;
        do{
            sy+=8;
            b2=par1World.getBlock(x,12+sy,z);
        }while(b2.getMaterial() != Material.water && b2.getMaterial() !=Material.lava && b2.getMaterial()!=Material.air && sy<sym);

        y=12+sy-8;
        */

        y=par1World.getChunkHeightMapMinimum(x, z)-10-12;
        if(y<0) y=10;
        else y=12+par2Random.nextInt(Math.min(y, 50));

        //y=100;
        StructureSealedLib1 ssl1 = new StructureSealedLib1(0, par2Random, x,y,z);
        this.components.add(ssl1);

        // 次のパーツを得る
        ssl1.buildComponent(ssl1, components, par2Random);

        // 構造物全体の占有範囲を更新する
        this.updateBoundingBox();
    }
}
