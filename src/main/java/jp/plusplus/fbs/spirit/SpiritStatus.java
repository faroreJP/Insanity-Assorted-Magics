package jp.plusplus.fbs.spirit;

import jp.plusplus.fbs.exprop.FBSEntityProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/02.
 * 精霊武器のステータスクラス
 * NBTで読み書きする
 */
public class SpiritStatus {
    private static Random random=new Random();
    private static float log255=2.40654f;

    public static final short LEVEL_MAX=50;
    public static final short STATUS_MAX=255;
    public static final short FOOD_LEVEL_MAX=128;

    //基本
    private boolean isMale;
    private String character; //人格名
    private String name; //プレイヤーのつけた名前
    private String owner; //所有者の名前
    private short level=1;
    private double next;
    private double exp;
    private int itemDamage; //減ってる耐久値
    private short skillPoint;
    private int lastBlessDate=-1;
    private short foodLevel=FOOD_LEVEL_MAX;

    /**
     * ダメージとか採掘レベルに関係する
     */
    private short strength;
    private float maxDamage;
    private float maxHarvestLevel;

    /**
     * 最大耐久に関係
     */
    private short toughness;
    private float maxDurability;

    /**
     * 精霊の所持スキル
     */
    private ArrayList<SkillManager.SkillData> skills=new ArrayList<SkillManager.SkillData>();

    /**
     * 精霊の設定
     */
    private Configuration configuration=new Configuration();

    public SpiritStatus(boolean isMale, String character, String name, EntityPlayer owner){
        this();
        this.isMale=isMale;
        this.character=character;
        this.name=name;
        this.owner=owner.getDisplayName();
    }
    public SpiritStatus(){
        level=0;
        levelUp();
    }

    public static SpiritStatus readFromNBT(NBTTagCompound nbt){
        if(nbt==null) return null;
        SpiritStatus ss=new SpiritStatus();

        ss.isMale=nbt.getBoolean("isMale");
        ss.character=nbt.getString("character");
        ss.name=nbt.getString("name");
        ss.owner=nbt.getString("owner");
        ss.level=nbt.getShort("level");
        ss.next=nbt.getDouble("next");
        ss.exp=nbt.getDouble("exp");
        ss.itemDamage=nbt.getInteger("itemDamage");
        ss.skillPoint=nbt.getShort("skillPt");
        ss.lastBlessDate=nbt.getInteger("lastBlessDate");
        ss.foodLevel=nbt.getShort("FoodLevel");

        ss.strength=nbt.getShort("strength");
        ss.toughness=nbt.getShort("toughness");
        ss.maxDamage=nbt.getFloat("maxDamage");
        ss.maxHarvestLevel=nbt.getFloat("maxHarvestLevel");
        ss.maxDurability=nbt.getFloat("maxDurability");

        NBTTagList list=(NBTTagList)nbt.getTag("skills");
        for(int i=0;i<list.tagCount();i++){
            NBTTagCompound n=list.getCompoundTagAt(i);
            int id=n.getInteger("id");
            if(SkillManager.getSkill(id)!=null){
                ss.skills.add(new SkillManager.SkillData(id, n.getInteger("lv")));
            }
        }

        ss.configuration.readFromNBT(nbt.getCompoundTag("config"));

        return ss;
    }
    public static void writeToNBT(SpiritStatus ss, NBTTagCompound nbt){
        nbt.setBoolean("isMale", ss.isMale);
        nbt.setString("character", ss.character);
        nbt.setString("name", ss.name);
        nbt.setString("owner", ss.owner);
        nbt.setShort("level", ss.level);
        nbt.setDouble("next", ss.next);
        nbt.setDouble("exp", ss.exp);
        nbt.setInteger("itemDamage", ss.itemDamage);
        nbt.setShort("skillPt", ss.skillPoint);
        nbt.setInteger("lastBlessDate", ss.lastBlessDate);
        nbt.setShort("FoodLevel", ss.foodLevel);

        nbt.setShort("strength", ss.strength);
        nbt.setShort("toughness", ss.toughness);
        nbt.setFloat("maxDamage", ss.maxDamage);
        nbt.setFloat("maxHarvestLevel", ss.maxHarvestLevel);
        nbt.setFloat("maxDurability", ss.maxDurability);

        NBTTagList list=new NBTTagList();
        for(SkillManager.SkillData se : ss.skills){
            NBTTagCompound n=new NBTTagCompound();
            n.setInteger("id", se.id);
            n.setInteger("lv", se.lv);
            list.appendTag(n);
        }
        nbt.setTag("skills", list);

        NBTTagCompound nbt1=new NBTTagCompound();
        ss.configuration.writeToNBT(nbt1);
        nbt.setTag("config", nbt1);
    }

    public boolean isOwner(EntityPlayer player){
        if(!player.getDisplayName().equals(getOwnerName())) return false;
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        String t=prop.getSpiritToolName();
        if(t==null || !t.equals(getName())) return false;
        return true;
    }

    public boolean addExp(double e){
        if(e<=0) return false;

        boolean flag=false;
        exp+=e;
        while(exp>=next && level<LEVEL_MAX){
            exp-=next;
            levelUp();
            flag=true;
        }
        return flag;
    }
    public void levelUp(){
        level++;
        next+= MathHelper.ceiling_double_int(8 * level + 0.5f * next);
        //next+=1;

        //ここで器用の効果発動
        if(level%(hasSkill("fbs.skillful")?2:3)==0){
            skillPoint++;
        }

        addStrength(3+random.nextInt(3));
        addToughness(3 + random.nextInt(3));

        //ここでデメリット判定
        if(level>0 && level%10==0 && random.nextFloat()<0.125f){
            setSkill("fbs.blood", 0);
        }
    }

    public void addStrength(int p){
        if(p<0) return;
        strength+=p;
        if(strength>STATUS_MAX) strength=STATUS_MAX;
    }
    public void addToughness(int p){
        if(p<0) return;
        toughness+=p;
        if(toughness>STATUS_MAX) toughness=STATUS_MAX;
    }
    public void addItemDamage(int p) {
        if (p < 0) return;
        itemDamage += p;
    }
    public void setItemDamage(int p){
        if (p < 0) return;
        itemDamage=p;
    }

    public void reduceSkillPoint(){
        if(skillPoint>0) skillPoint--;
    }

    public void updateLastBlessDate(int d){ lastBlessDate=d; }
    public void repair(int r){
        if(r<0) return;
        itemDamage-=r;
        if(itemDamage<0) itemDamage=0;
    }

    /**
     * 各種最大値を設定する。
     * 先天スキルの影響を受ける
     * @param maxDamage
     * @param maxHarvestLevel
     * @param maxDurability
     */
    public void setMaxStatus(float maxDamage, float maxHarvestLevel, float maxDurability){
        float rate=1.f;
        float rateDamage=1.f;
        float rateHL=1.f;
        float rateDurability=1.f;
        for(SkillManager.SkillData se : getSkills()){
            if(se.getSkill().getName().equals("fbs.precocious")){
                rate=0.75f;
                break;
            }
            else if(se.getSkill().getName().equals("fbs.lateBloomer")){
                rate=1.25f;
                break;
            }
            else if(se.getSkill().getName().equals("fbs.skillful")){
                rate=0.80f;
                break;
            }
            else if(se.getSkill().getName().equals("fbs.hero")){
                rateDamage=1.5f;
                rateHL=1.25f;
                rateDurability=0.75f;
                break;
            }
            else if(se.getSkill().getName().equals("fbs.patient")){
                rateDamage=0.75f;
                rateDurability=1.5f;
                break;
            }
            else if(se.getSkill().getName().equals("fbs.sentimental")){
                rate=0.8f;
                break;
            }
        }

        this.maxDamage=maxDamage*rate*rateDamage;
        this.maxHarvestLevel=maxHarvestLevel*rate*rateHL;
        this.maxDurability=maxDurability*rate*rateDurability;
    }

    /**
     * ステータス最大値(255)との割合を計算する。
     * このとき、各種先天スキルの影響を受ける。
     * @param param
     * @return
     */
    public float calcRatio(int param){
        int type=0;
        for(SkillManager.SkillData se : getSkills()){
            if(se.getSkill().getName().equals("fbs.precocious")){
                type=1;
                break;
            }
            else if(se.getSkill().getName().equals("fbs.lateBloomer")){
                type=2;
                break;
            }
        }

        float rate=0;
        switch (type){
            case 1:
                rate=(param==0?0:(float)Math.log(param)/log255);
                break;

            case 2:
                rate=(float)(param*param)/(float)(STATUS_MAX*STATUS_MAX);
                break;

            default:
                rate=(float)param/STATUS_MAX;
        }

        return rate;
    }

    public short getLv(){ return level; }
    public short getStrength(){ return strength; }
    public float getMaxDamage(){ return maxDamage; }
    public float getMaxHarvestLevel(){ return maxHarvestLevel; }
    public short getToughness(){ return toughness; }
    public float getMaxDurability(){ return maxDurability; }
    public String getName(){ return name; }
    public String getOwnerName(){ return owner; }
    public String getCharacter(){ return character; }
    public int getItemDamage(){ return itemDamage; }
    public int getLastBlessDate(){ return lastBlessDate; }
    public short getSkillPoint(){ return skillPoint; }
    public short getFoodLevel(){ return foodLevel; }

    public void setFoodLevel(int food){
        this.foodLevel=(short)food;
    }

    /**
     * スキルをセットする
     * @param name
     * @param lv
     */
    public void setSkill(String name, int lv){
        SkillManager.Skill skill=SkillManager.getSkill(name);
        if(skill==null) return;
        if(lv>=skill.getLvMax()) lv=skill.getLvMax()-1;

        //すでにそのスキルを所持している場合上書き
        for(SkillManager.SkillData se : skills){
            if(se.isSkillEqual(skill)){
                se.lv=lv;
                return;
            }
        }

        //そうでないなら新しく
        skills.add(new SkillManager.SkillData(skill.getId(), lv));
    }

    /**
     * そのスキルを所持しているか判定する
     * @param name
     * @return
     */
    public boolean hasSkill(String name){
        SkillManager.Skill skill=SkillManager.getSkill(name);
        if(skill==null) return false;
        for(SkillManager.SkillData se : skills){
            if(se.isSkillEqual(skill)){
                return true;
            }
        }
        return false;
    }

    /**
     * そのスキルのレベルを得る
     * @param name
     * @return スキルを所持していない場合-1
     */
    public int getSkillLevel(String name){
        SkillManager.Skill skill=SkillManager.getSkill(name);
        if(skill==null) return -1;
        for(SkillManager.SkillData se : skills){
            if(se.isSkillEqual(skill)){
                return se.lv;
            }
        }
        return -1;
    }
    public int getSkillLevel(int id){
        for(SkillManager.SkillData se : skills){
            if(se.isSkillEqual(id)){
                return se.lv;
            }
        }
        return -1;
    }

    public ArrayList<SkillManager.SkillData> getSkills(){ return skills; }
    public Configuration getConfiguration(){ return configuration; }

    /**
     * 精霊武器の設定
     */
    public static class Configuration{
        private ArrayList<Pair> configs=new ArrayList<Pair>();

        public Configuration(){
            add("enableMessage", true);
            add("enableTimeSignalMessage", true);
        }

        public void add(String key){
            add(key, false);
        }
        public void add(String key, boolean value){
            configs.add(new Pair(key, value));
        }
        public void update(String key, boolean value){
            for(Pair p : configs){
                if(p.key.equals(key)){
                    p.value=value;
                    break;
                }
            }
        }
        public boolean get(String key){
            for(Pair p : configs){
                if(p.key.equals(key)){
                    return p.value;
                }
            }
            return false;
        }
        public ArrayList<String> getKeys(){
            ArrayList<String> ret=new ArrayList<String>();
            for(Pair p : configs){
                ret.add(p.key);
            }
            return ret;
        }

        public void writeToNBT(NBTTagCompound nbt){
            for(Pair p : configs){
                nbt.setBoolean(p.key, p.value);
            }
        }
        public void readFromNBT(NBTTagCompound nbt){
            for(Pair p :configs){
                update(p.key, nbt.getBoolean(p.key));
            }
        }

        private class Pair{
            private String key;
            private boolean value;
            public Pair(String key, boolean value){
                this.key=key;
                this.value=value;
            }
        }
    }
}
