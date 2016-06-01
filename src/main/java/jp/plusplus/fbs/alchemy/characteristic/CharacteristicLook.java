package jp.plusplus.fbs.alchemy.characteristic;

/**
 * Created by plusplus_F on 2015/09/24.
 */
public class CharacteristicLook extends CharacteristicBase {

    public CharacteristicLook(){
        setUnlocalizedName("fbs.look");
    }

    public float getMPScale(){
        int v=getValue();
        if(v==0) return 2.0f;
        if(v==1) return 1.5f;
        if(v==2) return 0.5f;
        if(v==3) return 1.0f;
        return 1.f;
    }

    @Override
    public Type getType() {
        return Type.LOOK;
    }
}
