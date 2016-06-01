package jp.plusplus.fbs.magic.resonance;

import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.IResonance;
import net.minecraft.util.StatCollector;

import java.util.LinkedList;

/**
 * Created by pluslus_F on 2015/06/23.
 * 特定の魔導書+付与魔法1個以上による共鳴
 * 付与魔法が複数ある場合は全てに対して動作するように魔法は実装してね
 */
public class FlexibleResonance implements IResonance {
    public String type; //ベースとなる魔法のタイプ

    public LinkedList<Registry.MagicData> enchants=new LinkedList<Registry.MagicData>();
    public Registry.MagicData base;

    public FlexibleResonance(String type){
        this.type=type;
    }


    @Override
    public IResonance copy() {
        return new FlexibleResonance(type);
    }

    @Override
    public boolean isMatch(Registry.MagicData[] magics) {
        boolean foundBase=false;
        boolean foundEnchant=false;
        for(Registry.MagicData md : magics){

            //ベースとなる魔法の判定。ベースが2つ以上あればfalse
            if(md.title.equals(type)){
                if(foundBase) return false;
                foundBase=true;
            }

            //付与魔法ならフラグを立てる
            if(IMagicEnchant.class.isAssignableFrom(md.magic)){
                foundEnchant=true;
            }
        }

        return foundBase && foundEnchant;
    }

    @Override
    public String getResonanceMagicName() {
        return "resonance."+type;
    }

    @Override
    public String getDisplayMagicName(String[] titles){
        String sss=StatCollector.translateToLocal("magic.suffix."+type);
        String ppp=null;

        for(int i=0;i<titles.length;i++){
            if(titles==null) continue;
            Registry.MagicData md=Registry.GetMagic(titles[i]);
            if(IMagicEnchant.class.isAssignableFrom(md.magic)){
                ppp=StatCollector.translateToLocal("magic.prefix."+md.title);
                break;
            }
        }

        if(StatCollector.canTranslate("locale.fbs.jp")){
            return ppp+sss;
        }
        else{
            return ppp+" "+sss;
        }
    }

    @Override
    public int priority() {
        return 0;
    }
}
