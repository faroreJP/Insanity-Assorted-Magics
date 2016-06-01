package jp.plusplus.fbs.alchemy.characteristic;

/**
 * Created by plusplus_F on 2015/09/24.
 */
public class CharacteristicQuality extends CharacteristicBase {

    public CharacteristicQuality(){
        setUnlocalizedName("fbs.quality");
    }

    public float getMPScale(){
        int v=getValue();
        if(v==0) return 3.0f;
        if(v==1) return 1.5f;
        if(v==2) return 0.8f;
        return 1.f;
    }

    @Override
    public Type getType() {
        return Type.QUALITY;
    }
}
