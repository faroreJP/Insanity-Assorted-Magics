package jp.plusplus.fbs.event.wish.entry;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class WishEntryGeneric implements IWishEntry {
    private static Integer[] itemIds;
    private static String[] itemNames;
    private Random rand=new Random();

    @Override
    public float priority() {
        return 0.0f;
    }

    @Override
    public boolean matches(String string) {
        return true;
    }

    @Override
    public ItemStack get(EntityPlayer player, String string) {
        if(itemIds==null){
            Map<String,Integer> idMapping = Maps.newHashMap();
            GameData.itemRegistry.serializeInto(idMapping);

            itemIds=new Integer[idMapping.size()];
            idMapping.values().toArray(itemIds);
            itemNames=new String[idMapping.size()];
            idMapping.keySet().toArray(itemNames);
        }


        int maxMatchIndex=-1;
        int maxMatchValue=0;
        ItemStack maxMatchItemStack=null;

        //------------------------------------------------------------------------------------
        // 一致度の判定
        //------------------------------------------------------------------------------------
        if(string.contains(":")){ //ドメイン指定の名前かどうか判定して処理を分ける
            String[] domainAndName=string.split(":");
            FBS.logger.info("<Wish>"+domainAndName[0]+":"+domainAndName[1]);

            ArrayList<Character>[] tokens=new ArrayList[2];
            for(int i=0;i<2;i++){
                tokens[i]=new ArrayList<Character>(); // 名前を1文字ずつ区切ったトークン列
                for(char c : domainAndName[i].toCharArray()){
                    if(c!=' ') tokens[i].add((Character)c);
                }
            }

            for(int i=0;i<itemIds.length;i++){
                int tmp=0;
                String[] itemDomainAndName=itemNames[i].split(":");
                itemDomainAndName[0]=itemDomainAndName[0].trim();
                itemDomainAndName[1]=itemDomainAndName[1].trim();

                for(int m=0;m<2;m++){
                    if(domainAndName[m].length()>0){
                        boolean perfect=(domainAndName[m].length()==itemDomainAndName[m].length());
                        int length=itemDomainAndName[m].length();

                        ArrayList<Character> t=(ArrayList<Character>)tokens[m].clone();
                        for(int k=0;k<length && !t.isEmpty();k++){
                            char c=itemDomainAndName[m].charAt(k);
                            if(c==' ') continue;

                            if(t.contains(c)){
                                tmp+=50+rand.nextInt(15);
                                t.remove((Character)c);
                            }
                            else perfect=false;
                        }
                        if(perfect){
                            tmp+=10000; //完全に一致している場合、メチャ一致度を上げる
                        }
                    }
                }

                if(maxMatchValue<tmp){
                    maxMatchIndex=i;
                    maxMatchValue=tmp;
                }
            }

            // ItemStack生成
            if(maxMatchIndex!=-1){
                Item item=GameData.getItemRegistry().getObjectById(itemIds[maxMatchIndex]);
                ArrayList<ItemStack> subItems=new ArrayList<ItemStack>();
                item.getSubItems(item, item.getCreativeTab(), subItems);

                maxMatchItemStack=subItems.get(rand.nextInt(subItems.size()));
            }
        }
        else{
            ArrayList<Character> tokens=new ArrayList<Character>(); // 名前を1文字ずつ区切ったトークン列
            // Java死ね
            for(char c : string.toCharArray()){
                if(c!=' ') tokens.add((Character)c);
            }

            for(int i=0;i<itemIds.length;i++){
                Item item=GameData.getItemRegistry().getObjectById(itemIds[i]);
                if(item==null) continue;

                ArrayList<ItemStack> subItems=new ArrayList<ItemStack>();
                item.getSubItems(item, item.getCreativeTab(), subItems);

                // CreativeTabsに見える全てのアイテムについて調べる
                for(ItemStack itemStack : subItems){
                    int tmp=0;
                    String name=itemStack.getDisplayName();

                    boolean perfect=(name.length()==string.length());
                    int length=name.length();
                    ArrayList<Character> t=(ArrayList<Character>)tokens.clone();
                    for(int k=0;k<length && !t.isEmpty();k++){
                        char c=name.charAt(k);
                        if(c==' ') continue;

                        if(t.contains(c)){
                            tmp+=50+rand.nextInt(15);
                            t.remove((Character)c);
                        }
                        else perfect=false;
                    }
                    if(perfect) tmp+=10000; //名前が完全に一致している場合、メチャ一致度を上げる

                    if(maxMatchValue<tmp){
                        maxMatchIndex=i;
                        maxMatchValue=tmp;
                        maxMatchItemStack=itemStack;
                    }
                }
            }
        }

        // アイテムを得る
        return maxMatchItemStack!=null?maxMatchItemStack.copy():new ItemStack(Blocks.dirt);
    }
}
