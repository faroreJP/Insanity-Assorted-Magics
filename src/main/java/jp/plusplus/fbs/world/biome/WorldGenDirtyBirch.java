package jp.plusplus.fbs.world.biome;

/**
 * Created by plusplus_F on 2015/08/20.
 * 紅葉した白樺
 */
public class WorldGenDirtyBirch extends WorldGenDirtyOak {
    public WorldGenDirtyBirch(boolean notify) {
        super(notify, false);
        woodMeta=2;
        leaveMeta=1;
    }
}
