package jp.plusplus.fbs.api;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/08/26.
 * 陶芸品。ブロックに実装する事。
 */
public interface IPottery {

    /**
     * 指定の値のItemStackを返す
     * @param state
     * @param grade
     * @param size
     * @param pattern
     * @param hasEffect
     * @return
     */
    public ItemStack getItemStack(PotteryState state, PotteryGrade grade, PotterySize size, byte pattern, boolean hasEffect);

    /**
     * Sizeを設定する
     * @param itemStack
     * @param size
     */
    public void setSize(ItemStack itemStack, PotterySize size);

    /**
     * サイズを返す
     * @param nbt
     * @return
     */
    public PotterySize getSize(NBTTagCompound nbt);


    /**
     * 品質を設定する
     * @param itemStack
     * @param grade
     */
    public void setGrade(ItemStack itemStack, PotteryGrade grade);

    /**
     * 品質を*ランダムに*設定する
     * @param itemStack
     * @param rand
     */
    public void setGrade(ItemStack itemStack, Random rand);

    /**
     * 品質を返す
     * @param nbt
     * @return
     */
    public PotteryGrade getGrade(NBTTagCompound nbt);

    /**
     * 模様を設定する
     * @param itemStack
     * @param pattern
     */
    public void setPattern(ItemStack itemStack, byte pattern);

    /**
     * 模様を返す
     * @param nbt
     * @return
     */
    public byte getPattern(NBTTagCompound nbt);

    /**
     * 状態を設定する
     * 特殊効果持ちの陶芸品を作る場合、ここでBAKEDに遷移する際にアイテムスタックを弄ること
     * @param itemStack
     * @param state
     */
    public void setState(ItemStack itemStack, PotteryState state);
    /**
     * 状態を返す
     * @param nbt
     * @return
     */
    public PotteryState getState(NBTTagCompound nbt);

    /**
     * 効果持ちフラグを設定する
     * @param itemStack
     * @param eff
     * @return
     */
    public void setEffect(ItemStack itemStack, boolean eff);

    /**
     * 魔法の効果を持つか
     * @param nbt
     * @return
     */
    public boolean hasEffect(NBTTagCompound nbt);

    /**
     * hasEffect():true時に、窯で焼きあがる際に呼ばれる
     * 効果持ちのItemStackを返す
     * @param itemStack
     * @return
     */
    public ItemStack getEnchantedItemStack(ItemStack itemStack, Random rand);

    /**
     * 描画用メタデータを得る
     *  2211 0000
     *  0:模様
     *  1:大きさ
     *  2:状態
     * @param nbt
     * @return
     */
    public int getMetadata(NBTTagCompound nbt);

    /**
     * モデル描画に使うRLを返す
     * @param metadata　
     * @return
     */
    public ResourceLocation getResourceLocation(int metadata);

    /**
     * モデル描画に使うMBを返す
     * @param metadata
     * @return
     */
    public ModelBase getModel(int metadata);

    /**
     * ブロックを返す。基本はthisを返す
     * @return
     */
    public Block getBlockType();

    /**
     * 乾燥にかかる*秒数*を返す
     * @param nbt
     * @return
     */
    public int getDrySec(NBTTagCompound nbt);

    /**
     * そのアイテムを売却した際の価格を返す
     * @param itemStack
     * @return
     */
    public int getMP(ItemStack itemStack);

    /**
     * 表示されるローカライズされた名前を得る
     * @param nbt
     * @return
     */
    public String getLocalizedName(NBTTagCompound nbt);

    /**
     * 「完成品の」取り得る全てのパターンを返す
     * 返り値はMPやCreativeTabの登録に使われる
     * @return
     */
    public ArrayList<ItemStack> getAllPattern();

    /**
     * 被弾時に壺が割れる確率を得る
     * @param itemStack
     * @return
     */
    public float getCrashProbability(ItemStack itemStack);

    public static enum PotteryState{
        MOLDED(0), DRY(1), BAKED(2), INVALID_VALUE(-1);

        PotteryState(int v){ value=(byte)v; }

        private byte value;
        public byte getValue(){ return value; }

        public static PotteryState Get(int v){
            if(v==MOLDED.getValue()) return MOLDED;
            if(v==DRY.getValue()) return DRY;
            if(v==BAKED.getValue()) return BAKED;
            return INVALID_VALUE;
        }
        public static String GetLocalizedPrefix(PotteryState state){
            if(state==MOLDED) return StatCollector.translateToLocal("pottery.fbs.molded");
            if(state==DRY) return StatCollector.translateToLocal("pottery.fbs.dry");
            return "";
        }
    }
    public static enum PotterySize{
        SMALL(0), MEDIUM(1), LARGE(2);

        PotterySize(int v){ value=(byte)v; }

        private byte value;
        public byte getValue(){ return value; }

        public static PotterySize Get(int v){
            if(v==SMALL.getValue()) return SMALL;
            if(v==LARGE.getValue()) return LARGE;
            return MEDIUM;
        }
        public static String GetLocalizedPrefix(PotterySize size){
            String str;
            switch (size){
                case SMALL: str= StatCollector.translateToLocal("pottery.fbs.small"); break;
                case LARGE: str=StatCollector.translateToLocal("pottery.fbs.large"); break;
                default: str=""; break;
            }
            return str;
        }
    }
    public static enum PotteryGrade{
        BAD(0), NORMAL(1), GOOD(2), GREAT(3), SOULFUL(4);

        PotteryGrade(int v){ value=(byte)v; }

        private byte value;
        public byte getValue(){ return value; }

        public static PotteryGrade Get(int v){
            if(v==BAD.getValue()) return BAD;
            if(v==GOOD.getValue()) return GOOD;
            if(v==GREAT.getValue()) return GREAT;
            if(v==SOULFUL.getValue()) return SOULFUL;
            return NORMAL;
        }
        public static String GetLocalizedPrefix(PotteryGrade grade){
            String str;
            switch (grade){
                case BAD: str=StatCollector.translateToLocal("pottery.fbs.bad"); break;
                case GOOD: str=StatCollector.translateToLocal("pottery.fbs.good"); break;
                case GREAT: str=StatCollector.translateToLocal("pottery.fbs.great"); break;
                case SOULFUL: str=StatCollector.translateToLocal("pottery.fbs.soulful"); break;
                default: str=""; break;
            }
            return str;
        }
    }
}
