package jp.plusplus.fbs.alchemy.characteristic;

/**
 * Created by plusplus_F on 2015/09/24.
 */
public class CharacteristicWeight extends CharacteristicBase {

    public CharacteristicWeight(){
        setUnlocalizedName("fbs.weight");
    }

    public float getMPScale(){
        int v=getValue();
        if(v==0) return 0.75f;
        if(v==1) return 1.25f;
        return 1.f;
    }

    @Override
    public Type getType() {
        return Type.WEIGHT;
    }
}
