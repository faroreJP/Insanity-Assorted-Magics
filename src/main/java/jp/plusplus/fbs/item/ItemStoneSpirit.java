package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;

import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/14.
 */
public class ItemStoneSpirit extends ItemBase {
    protected Random rand=new Random();

    public ItemStoneSpirit(){
        setCreativeTab(FBS.tabSpirit);
        setMaxStackSize(1);
    }

    public boolean getSex(){
        if(this==ItemCore.stoneActiveFemale) return false;
        if(this==ItemCore.stoneActiveMale) return true;
        return rand.nextBoolean();
    }
}
