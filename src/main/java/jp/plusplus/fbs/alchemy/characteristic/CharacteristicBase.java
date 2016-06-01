package jp.plusplus.fbs.alchemy.characteristic;

import com.mojang.realmsclient.gui.ChatFormatting;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by plusplus_F on 2015/09/08.
 * 錬金術の素材・生成品の「特性」
 */
public abstract class CharacteristicBase {
    protected int value;
    protected String uName="";

    public CharacteristicBase(){}

    /**
     * 特性の強さの単位を得る
     * @return 特性の強さの単位
     */
    public abstract Type getType();

    /**
     * 生成品のMP価格への倍率補正を返す
     * @return 価格倍率
     */
    public float getMPScale(){
        return 1.f;
    }

    /**
     * この特性を持つアイテムをplayerが使用した際に行う処理
     * @param world
     * @param entity
     */
    public void affectEntity(World world, EntityLivingBase entity){}

    /**
     * ツールチップでの表示色を返す
     * @return 表示色
     */
    public ChatFormatting getNameColor(){
        return ChatFormatting.GRAY;
    }

    public void writeToNBT(NBTTagCompound nbt){
        nbt.setInteger("value", getValue());
    }
    public void readFromNBT(NBTTagCompound nbt){
        value=nbt.getInteger("value");
    }

    public int getValue(){ return getType().getCorrectedValue(value); }
    /**
     * 効果の強さを設定する
     * @param value
     */
    public void setValue(int value){
        this.value=value;
    }


    public int getId(){
        return AlchemyRegistry.GetCharacteristicId(this.getClass());
    }

    public void setUnlocalizedName(String u){ uName=u; }
    public String getUnlocalizedName(){
        return "alchemy.chara."+uName;
    }
    public String getLocalizedName(){
        return StatCollector.translateToLocal(getUnlocalizedName());
    }

    public String getUnlocalizedEffectValue(){
        return getType().getUnlocalizedName(value);
    }
    public String getLocalizedEffectValue(){
        return StatCollector.translateToLocal(getUnlocalizedEffectValue());
    }

    /**
     * 特性の持つ、効果の強さの単位
     * 特性はvalueが大きい順に優先される
     */
    public enum Type{
        SCALE("fbs.small", "fbs.medium", "fbs.large"),
        LENGTH("fbs.short", "fbs.medium", "fbs.long"),
        LOOK("fbs.look.beautiful", "fbs.look.good", "fbs.look.dirty", "fbs.look.strange"),
        WEIGHT("fbs.light", "fbs.heavy"),
        QUALITY("fbs.great", "fbs.good", "fbs.bad"),
        NONE();

        private String[] str;
        Type(String ... name){
            str=name;
        }
        public String getUnlocalizedName(int value){
            if(str==null || str.length==0) return "";

            value=getCorrectedValue(value);
            return "alchemy.effect."+str[value];
        }
        public int getCorrectedValue(int value){
            if(value<0 || value>=str.length) value=0;
            return value;
        }
    }
}
