package jp.plusplus.fbs.pottery;

import cpw.mods.fml.common.FMLLog;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.pottery.usable.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2016/03/30.
 */
public class PotteryRegistry {
    private static PotteryRegistry instance=new PotteryRegistry();

    private ArrayList<PotteryPair> potteries=new ArrayList<PotteryRegistry.PotteryPair>();
    private ArrayList<PotteryBase> potteryEffects=new ArrayList<PotteryBase>();
    private PotteryBase cachedPotteryEffect;
    public PotteryPair cachedPottery;


    public static void register(){

        //------------------------------------陶芸----------------------------------------------

        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)0, false), "","",""," c c ","  c  ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)1, false), "","",""," csc ","  c  ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)2, false), "","",""," cbc ","  c  ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)3, false), "","",""," cfc ","  c  ");

        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)0, true), "","",""," g g ","  g  ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)1, true), "","",""," gsg ","  g  ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)2, true), "","",""," gbg ","  g  ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.SMALL, (byte)3, true), "","",""," gfg ","  g  ");

        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)0, false), "",""," c c "," c c "," ccc ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)1, false), "",""," c c "," csc "," ccc ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)2, false), "",""," c c "," cbc "," ccc ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)3, false), "",""," c c "," cfc "," ccc ");

        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)0, true), "",""," g g "," g g "," ggg ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)1, true), "",""," g g "," gsg "," ggg ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)2, true), "",""," g g "," gbg "," ggg ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)3, true), "",""," g g "," gfg "," ggg ");

        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)0, false), " c c "," c c ","c   c","c   c"," ccc ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)1, false), " c c "," c c ","c   c","c s c"," ccc ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)2, false), " c c "," c c ","c   c","c b c"," ccc ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)3, false), " c c "," c c ","c   c","c f c"," ccc ");

        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)0, true), " g g "," g g ","g   g","g   g"," ggg ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)1, true), " g g "," g g ","g   g","g s g"," ggg ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)2, true), " g g "," g g ","g   g","g b g"," ggg ");
        RegisterPotteryCrafting(BlockCore.pot.getItemStack(IPottery.PotteryState.MOLDED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)3, true), " g g "," g g ","g   g","g f g"," ggg ");

        registerPotteryEffect(new PotteryKeep());
        registerPotteryEffect(new PotterySenaka());
        registerPotteryEffect(new PotteryVoid());
        registerPotteryEffect(new PotteryChange());
        registerPotteryEffect(new PotteryCrucible());
        registerPotteryEffect(new PotteryLottery());
        registerPotteryEffect(new PotteryTaboo());
        registerPotteryEffect(new PotteryUnbreakable());
        registerPotteryEffect(new PotteryEnchantment());
        registerPotteryEffect(new PotteryTaboo());
        registerPotteryEffect(new PotteryFurnace());
        registerPotteryEffect(new PotteryAppraisal());
    }

    /*
    ####################################################
                    Potter's Wheel

                    クラフト処理は現在クソ以外の何者でもない(Registry.PotteryPair.isMatch参照)ですが仮実装なので・・・
                    要望があればキチンとした感じに作り直します
    ####################################################
    */

    public static void RegisterPotteryCrafting(ItemStack prod, String ... str){
        instance.potteries.add(new PotteryRegistry.PotteryPair(prod, str));
    }
    public static ItemStack GetPotteryCrafting(ItemStack[] r){
        if(instance.cachedPottery!=null && instance.cachedPottery.isMatch(r)){
            return instance.cachedPottery.product;
        }

        for(PotteryRegistry.PotteryPair pp : instance.potteries){
            if(pp.isMatch(r)){
                instance.cachedPottery=pp;
                return pp.product;
            }
        }
        return null;
    }
    public static ArrayList<PotteryRegistry.PotteryPair> GetPotteryCrafting(){ return instance.potteries; }


    public static int registerPotteryEffect(PotteryBase p){
        instance.potteryEffects.add(p);
        return instance.potteryEffects.size()-1;
    }
    public static PotteryBase getPotteryEffect(int id){
        return instance.potteryEffects.get(id);
    }
    public static PotteryBase getPotteryEffect(String unlocalizedName){
        if(instance.cachedPotteryEffect!=null && instance.cachedPotteryEffect.getUnlocalizedName().equals(unlocalizedName)){
            return instance.cachedPotteryEffect;
        }

        for(PotteryBase pb : instance.potteryEffects){
            if(pb.getUnlocalizedName().equals(unlocalizedName)){
                instance.cachedPotteryEffect=pb;
                return pb;
            }
        }
        return null;
    }
    public static int getPotteryEffectId(String unlocalizedName){
        int s=instance.potteryEffects.size();
        for(int i=0;i<s;i++){
            PotteryBase pb=instance.potteryEffects.get(i);
            if(pb.getUnlocalizedName().equals(unlocalizedName)){
                instance.cachedPotteryEffect=pb;
                return i;
            }
        }
        return -1;
    }


    public static class PotteryPair{
        public ItemStack product;
        public boolean[] clays=new boolean[25];
        public char[] materials=new char[25];
        public boolean isEnable;

        public PotteryPair(ItemStack prod, String ... str){
            product=prod;
            isEnable=true;

            if(str==null || str.length!=5){
                FMLLog.severe("Failed to parse Pottery Recipe : Craft Matrix has invalid size");
                isEnable=false;
                return;
            }
            for(int k=0;k<str.length;k++){
                if(str[k].length()==0){
                    for(int i=0;i<5;i++) materials[k*5+i]=' ';
                    continue;
                }

                if(str[k].length()!=5){
                    FMLLog.severe("Failed to parse Pottery Recipe : Craft Matrix has invalid size");
                    isEnable=false;
                    return;
                }

                for(int i=0;i<str[k].length();i++){
                    char c=str[k].charAt(i);
                    clays[k*5+i]=(c!=' ');
                    materials[k*5+i]=c;
                }
            }
        }
        public boolean isMatch(ItemStack[] r){
            if(!isEnable || r.length!=25) return false;
            for(int i=0;i<25;i++){
                if(materials[i]==' '){
                    if(r[i]!=null) return false;
                    continue;
                }
                else{
                    //何か文句があれば変える
                    if(materials[i]=='c'){
                        if(r[i]==null) return false;
                        Item item=r[i].getItem();
                        if(item!= ItemCore.clayWet) return false;
                    }
                    else if(materials[i]=='g'){
                        if(r[i]==null) return false;
                        Item item=r[i].getItem();
                        if(item!=ItemCore.clayGlow) return false;
                    }
                    else if(materials[i]=='f'){
                        if(r[i]==null) return false;
                        Item item=r[i].getItem();
                        if(item!=Item.getItemFromBlock(Blocks.red_flower) && item!=Item.getItemFromBlock(Blocks.yellow_flower)) return false;
                    }
                    else if(materials[i]=='s'){
                        if(r[i]==null) return false;
                        Item item=r[i].getItem();
                        if(item!= Items.stick) return false;
                    }
                    else if(materials[i]=='b'){
                        if(r[i]==null) return false;
                        Item item=r[i].getItem();
                        if(item!=Item.getItemFromBlock(Blocks.iron_bars)) return false;
                    }

                }
                /*
                if(clays[i]){
                    if(r[i]==null) return false;
                    Item item=r[i].getItem();
                    if(item!=ItemCore.clayWet && item!=ItemCore.clayGlow) return false;
                }
                else if(r[i]!=null) return false;
                */
            }
            return true;
        }
    }
}
