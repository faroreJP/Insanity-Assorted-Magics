package jp.plusplus.fbs.magic.resonance;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IResonance;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by pluslus_F on 2015/06/23.
 * 固定された魔導書同士での共鳴
 */
public class ShapedResonance implements IResonance {
    public String[] books;
    public String title;

    public ShapedResonance(String title, String ... books){
        this.title=title;
        this.books=new String[books.length];
        for(int i=0;i<books.length;i++){
            this.books[i]=books[i];
        }
    }

    @Override
    public IResonance copy() {
        return new ShapedResonance(title, books);
    }

    @Override
    public boolean isMatch(Registry.MagicData[] magics) {
        LinkedList<String> list=new LinkedList<String>();
        for(int i=0;i<this.books.length;i++){
            list.add(this.books[i]);
        }

        for(Registry.MagicData bd : magics){
            boolean find=false;
            Iterator<String> it=list.iterator();
            while(it.hasNext()){
                String t=it.next();
                if(t.equals(bd.title)){
                    find=true;
                    it.remove();
                    break;
                }
            }

            if(!find) return false;
        }

        return list.isEmpty();
    }

    @Override
    public String getResonanceMagicName() {
        return title;
    }
    @Override
    public String getDisplayMagicName(String[] titles){
        return Registry.GetLocalizedBookTitle(title);
    }

    @Override
    public int priority() {
        return 100;
    }
}
