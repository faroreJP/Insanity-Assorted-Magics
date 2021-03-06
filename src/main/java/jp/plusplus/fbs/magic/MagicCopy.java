package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class MagicCopy extends MagicBase {
    @Override
    public boolean checkSuccess() {
        float prob=0.2f;
        int l=getLvDiff();
        if(l>0) prob+=0.05*prob;
        return rand.nextFloat()<prob;
    }
    @Override
    public void success() {

    }
    @Override
    public void failure() {

    }

    public String getMagicCircleName(){
        return "fbs.copy";
    }
}
