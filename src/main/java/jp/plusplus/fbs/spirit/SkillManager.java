package jp.plusplus.fbs.spirit;

import net.minecraft.util.StatCollector;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/11/02.
 * 精霊武器のスキルを管理するクラス
 */
public class SkillManager {
    private static SkillManager instance=new SkillManager();

    private int uId=0;
    private ArrayList<Skill> skills=new ArrayList<Skill>();
    private ArrayList<SkillEntry> skillEntries=new ArrayList<SkillEntry>();

    private SkillManager(){}

    public static void register(){
        //congenital
        registerSkill("fbs.precocious", 1);
        registerSkill("fbs.lateBloomer", 1);

        //demerit
        registerSkill("fbs.blood", 1);
        registerSkill("fbs.end", 1);
        registerSkill("fbs.insanity", 1);

        //passive
        registerSkill("fbs.soulBind", 1);
        registerSkill("fbs.contract", 1);
        registerSkill("fbs.knowledge", 1);

        //battle
        registerSkill("fbs.fire", 5);
        registerSkill("fbs.vampire", 5);
        registerSkill("fbs.poison", 5);
        registerSkill("fbs.sonic", 5);
        registerSkill("fbs.knockback", 5);
        registerSkill("fbs.infinity", 1);
        registerSkill("fbs.critical", 5);
        registerSkill("fbs.hero", 5);
        registerSkill("fbs.lucky", 5);
        registerSkill("fbs.headhunt", 5);
        registerSkill("fbs.smite", 3);
        registerSkill("fbs.arthropods", 3);

        //harvest

        //-------------------------------------------------------------------------------------------------

        addSkillEntry(new SkillEntry(getSkill("fbs.soulBind"), new int[]{10}));
        addSkillEntry(new SkillEntry(getSkill("fbs.contract"), new int[]{15}));
        addSkillEntry(new SkillEntry(getSkill("fbs.knowledge"), new int[]{8}));

        addSkillEntry(new SkillEntry(getSkill("fbs.fire"), new int[]{5,8,11,14,17}));
        addSkillEntry(new SkillEntry(getSkill("fbs.blood"), new int[]{10,14,18,22,26}));
        addSkillEntry(new SkillEntry(getSkill("fbs.poison"), new int[]{5,8,11,14,17}));
        addSkillEntry(new SkillEntry(getSkill("fbs.sonic"), new int[]{10,14,18,22,26}));
        addSkillEntry(new SkillEntry(getSkill("fbs.knockback"), new int[]{5,8,11,14,17}));
        addSkillEntry(new SkillEntry(getSkill("fbs.infinity"), new int[]{35}));
        addSkillEntry(new SkillEntry(getSkill("fbs.critical"), new int[]{5,8,11,14,17}));
        addSkillEntry(new SkillEntry(getSkill("fbs.hero"), new int[]{10,14,18,22,26}));
        addSkillEntry(new SkillEntry(getSkill("fbs.lucky"), new int[]{10,14,18,22,26}));
        addSkillEntry(new SkillEntry(getSkill("fbs.headhunt"), new int[]{20,25,30,35,40}));
        addSkillEntry(new SkillEntry(getSkill("fbs.smite"), new int[]{3,6,9,12,15}));
        addSkillEntry(new SkillEntry(getSkill("fbs.arthropods"), new int[]{3,6,9,12,15}));
    }

    /**
     * 精霊武器のスキルのデータを登録する。
     * @param name スキル名
     * @param max 最大レベル
     * @return
     */
    public static int registerSkill(String name, int max){
        instance.skills.add(new Skill(++instance.uId, name, max));
        return instance.uId;
    }

    /**
     * IDからスキルのデータを取得する
     * @param id
     * @return
     */
    public static Skill getSkill(int id){
        if(id<=0 || id>instance.skills.size()) return null;
        return instance.skills.get(id-1);
    }
    public static Skill getSkill(String name){
        for(Skill s : instance.skills){
            if(s.getName().equals(name)) return s;
        }
        return null;
    }

    public static void addSkillEntry(SkillEntry se){
        instance.skillEntries.add(se);
    }

    public static ArrayList<SkillEntry> getSkillEntiries(){
        return instance.skillEntries;
    }

    /**
     * スキルのデータのクラス
     */
    public static class Skill{
        private int id;
        private String name;
        private int lvMax;

        public Skill(int id, String name, int max){
            this.id=id;
            this.name=name;
            lvMax=max;
        }



        public String getLocalizedName(){
            return StatCollector.translateToLocal("spirit.skill."+name);
        }

        public int getId() {
            return id;
        }

        public int getLvMax() {
            return lvMax;
        }

        public String getName(){
            return name;
        }
    }

    public static class SkillEntry{
        private Skill skill;
        private int[] minLv;
        private SkillData[] parents;

        public SkillEntry(Skill skill, int[] minLv){
            this(skill, minLv, new SkillData[0]);
        }
        public SkillEntry(Skill skill, int[] minLv, SkillData[] parents){
            this.skill=skill;
            this.minLv=minLv;
            this.parents=parents;
        }

        public boolean canLearn(SpiritStatus status){
            //精霊レベル
            int nowLv=status.getSkillLevel(skill.getId())+1;
            if(nowLv>=skill.getLvMax()) return false;
            if(status.getLv()<minLv[nowLv]) return false;

            //前提スキルの判定
            for(SkillData sd : parents){
                int now=status.getSkillLevel(sd.getSkill().getName());
                if(sd.lv<now) return false;
            }

            return true;
        }

        public int getSkillId(){ return skill.getId(); }
        public String getString(int lv){
            String ret="Lv"+(lv+1)+" "+skill.getLocalizedName();
            ret+="\n"+StatCollector.translateToLocal("spirit.gui.fbs.need")+":Lv"+minLv[lv];
            if(parents.length>0){
                for(int i=0;i<parents.length;i++){
                    ret+=","+parents[i].getString();
                }
            }
            return ret;
        }
    }

    /**
     * 精霊武器が持つスキル情報
     */
    public static class SkillData{
        public int id;
        public int lv;

        public SkillData(int id, int lv){
            this.id=id;
            this.lv=lv;
        }

        public Skill getSkill(){
            return SkillManager.getSkill(id);
        }

        public boolean isSkillEqual(Skill skill){
            return id==skill.getId();
        }
        public boolean isSkillEqual(int id){
            return this.id==id;
        }

        public String getString() {
            return "Lv"+(lv + 1) + " " + getSkill().getLocalizedName();
        }
    }
}
