package jp.plusplus.fbs.world.structure;

import jp.plusplus.fbs.FBS;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class MapGenSealdLib extends MapGenStructure {
    public MapGenSealdLib() {
    }

    @Override
    public String func_143025_a() {
        return "Sealed Library";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int i, int j) {
        boolean f=isLibraryChunk(worldObj, i, j);
        //if(f) FBS.logger.info("generated at:"+i+","+j);
        //EntityEnderEye
        //ItemEnderEye
        return f;
    }

    @Override
    protected StructureStart getStructureStart(int i, int j) {
        return new StructureSealedLibStart(this.worldObj, this.rand, i, j);
    }

    /**
     * それが図書館生成チャンクであるかどうか
     *
     * @param x
     * @param z
     * @return
     */
    public static boolean isLibraryChunk(World w, int x, int z) {
        int max = 4;
        int min = 1;

        int k = x;
        int l = z;

        if (x < 0) {
            x -= max - 1;
        }

        if (z < 0) {
            z -= max - 1;
        }

        int i1 = x / max;
        int j1 = z / max;

        long seed = (long) i1 * 341873128712L + (long) j1 * 132897987541L + w.getWorldInfo().getSeed() + (long) 14357617;
        Random random = new Random(seed);

        i1 *= max;
        j1 *= max;
        i1 += random.nextInt(max - min);
        j1 += random.nextInt(max - min);

        return (k == i1 && l == j1);
    }

}