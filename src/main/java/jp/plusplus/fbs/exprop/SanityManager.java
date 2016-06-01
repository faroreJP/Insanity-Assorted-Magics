package jp.plusplus.fbs.exprop;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.event.PlayerSanityEvent;
import jp.plusplus.fbs.api.event.PlayerSanityRollEvent;
import jp.plusplus.fbs.packet.MessagePlayerProperties;
import jp.plusplus.fbs.packet.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/05.
 * 正気度および魔術レベル・経験値の管理クラス
 * 外部からこのクラスを呼び出してプレイヤーをいじる
 * 基本的に正気度は XdY で変化する。Xはダイスを振る回数を、Yはダイスの面数を表す。
 */
public class SanityManager {
    private static SanityManager instance = new SanityManager();
    public static final String STATUS = "san";
    private static Random rand=new Random();

    private static void addSAN(EntityPlayer player, FBSEntityProperties prop, int san){
        prop.setSanity(prop.getSanity()+san);
        player.addChatComponentMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("info.fbs.san.0"), san)));
    }
    private static void loseSAN(EntityPlayer player, FBSEntityProperties prop, int san){
        int pre=prop.getSanity();
        prop.setSanity(pre-san);
        player.addChatComponentMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("info.fbs.san.1"), san)));
        player.triggerAchievement(AchievementRegistry.insanity);

        if(prop.getSanity()<=0){
            //死亡判定
            player.attackEntityFrom(new DamageSource("fbs.madness."+rand.nextInt(3)), 10000);
            player.triggerAchievement(AchievementRegistry.death);
        }
        else if(san>=2 && 100*san/pre>=20){
            //発狂判定
            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("info.fbs.san.2")));
            player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 15, 2));
            player.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 20*15, 1));
            player.triggerAchievement(AchievementRegistry.madness);
        }
    }

    /**
     * 正気度を与える
     * @param player
     * @param trial ダイスを振る回数
     * @param max 何面ダイス？
     * @param sim 実際に結果を反映させるか否か
     * @return 変化量
     */
    public static int addSanity(EntityPlayer player, int trial, int max, boolean sim){
        if(max<=0 || trial<=0 || player==null) return 0;
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        if(prop==null) return 0;

        int as=0;
        if(player instanceof EntityPlayerMP && !player.worldObj.isRemote){
            PlayerSanityRollEvent psre=new PlayerSanityRollEvent(player, trial, max);
            if(MinecraftForge.EVENT_BUS.post(psre) || psre.newTrial<=0 || psre.newMax==0) return 0;

            //符号の反転
            boolean isReversed=psre.newMax<0;
            trial=psre.newTrial;
            max=isReversed?-psre.newMax:psre.newMax;
            for(int i=0;i<trial;i++){
                as+=1+rand.nextInt(max);
            }

            if(sim){
                PlayerSanityEvent ev=new PlayerSanityEvent(player, isReversed?-as:as, 0, 0);
                boolean hc= MinecraftForge.EVENT_BUS.post(ev);
                if(!hc){
                    as=ev.newChangeSanity;
                    if(as>0){
                        addSAN(player, prop, as);
                    }
                    else{
                        loseSAN(player, prop, as);
                    }

                    sendPacket(player);
                }
            }
        }
        return as;
    }

    /**
     * 正気度を失わせる
     * @param player
     * @param trial ダイスを振る回数
     * @param max 何面ダイス？
     * @param sim 実際に結果を反映させるか否か
     * @return 変化量
     */
    public static int loseSanity(EntityPlayer player, int trial, int max, boolean sim){
        //FMLLog.info("called lose!");
        if(max<=0 || trial<=0 || player==null) return 0;
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        if(prop==null) return 0;

        int as=0;
        if(player instanceof EntityPlayerMP && !player.worldObj.isRemote){
            PlayerSanityRollEvent psre=new PlayerSanityRollEvent(player, trial, -max);
            if(MinecraftForge.EVENT_BUS.post(psre) || psre.newTrial<=0 || psre.newMax==0) return 0;

            //符号の反転
            boolean isReversed=psre.newMax>0;
            trial=psre.newTrial;
            max=isReversed?psre.newMax:-psre.newMax;
            for(int i=0;i<trial;i++){
                as+=1+rand.nextInt(max);
            }

            if(sim){
                PlayerSanityEvent ev=new PlayerSanityEvent(player, isReversed?as:-as, 0, 0);
                boolean hc=MinecraftForge.EVENT_BUS.post(ev);
                if(!hc){
                    as=-ev.newChangeSanity; //新しい変化量
                    if(as>0){
                        loseSAN(player, prop, as);
                    }
                    else if(as<0){
                        //SAN値が増える場合
                        addSAN(player, prop, as);
                    }

                    sendPacket(player);
                }
            }
        }
        return as;
    }

    /**
     * 魔術経験値を与える。レベルアップの判定と処理もここ
     * @param player
     * @param exp
     * @param sim 実際に結果を反映させるか否か
     * @return 経験値の変化量
     */
    public static double addExp(EntityPlayer player, double exp, boolean sim){
        if(exp<=0 || player==null) return 0;
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        if(prop==null) return 0;

        if(player instanceof EntityPlayerMP && !player.worldObj.isRemote){
            if(sim){
                PlayerSanityEvent ev=new PlayerSanityEvent(player, 0, exp, 0);
                boolean hc= MinecraftForge.EVENT_BUS.post(ev);
                if(!hc){
                    int lv=prop.getMagicLevel();
                    exp=ev.newChangeExp;
                    if(exp>0){
                        //経験値が増える場合のみ
                        prop.addEXP(exp);
                        if(lv!=prop.getMagicLevel()) {
                            player.addChatComponentMessage(new ChatComponentText(String.format(StatCollector.translateToLocal("info.fbs.lv.0"), prop.getMagicLevel())));
                        }
                    }
                    sendPacket(player);
                }
            }
        }
        return exp;
    }

    public static void addDestination(EntityPlayer player, int dimId, int x, int y, int z){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        if(prop.addDestination(dimId, x, y,z)){
            //狭間生成用が追加されたとき，名前の変更
            if(dimId==FBS.dimensionCrackId && x==-1 && y==-1 && z==-1){
                ArrayList<FBSEntityProperties.WarpPosition> list=prop.getDestinations();
                list.get(list.size()-1).setName("???");
            }
        }
        sendPacket(player);
    }

    public static void setSpirit(EntityPlayer player, String name, int lv){
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        prop.setSpiritToolName(name);
        prop.setSpiritToolLevel(lv);
        sendPacket(player);
    }
    public static void setSpiritName(EntityPlayer entityPlayer, String name){
        FBSEntityProperties prop=FBSEntityProperties.get(entityPlayer);
        prop.setSpiritToolName(name);
        sendPacket(entityPlayer);
    }
    public static void setSpiritLevel(EntityPlayer entityPlayer, int lv){
        FBSEntityProperties prop=FBSEntityProperties.get(entityPlayer);
        prop.setSpiritToolLevel(lv);
        sendPacket(entityPlayer);
    }

    public static void loadProxyData(EntityPlayer player) {
        PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), (EntityPlayerMP)player);
    }
    public static void sendPacket(EntityPlayer player) {
        PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), (EntityPlayerMP)player);
    }
}
