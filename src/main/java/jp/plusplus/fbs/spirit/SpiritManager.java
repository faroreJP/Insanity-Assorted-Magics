package jp.plusplus.fbs.spirit;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicBase;
import jp.plusplus.fbs.alchemy.characteristic.CharacteristicQuality;
import jp.plusplus.fbs.api.event.SpiritTalkEvent;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import shift.sextiarysector.SextiarySector;
import shift.sextiarysector.api.SextiarySectorAPI;
import shift.sextiarysector.packet.PacketPlayerData;
import shift.sextiarysector.packet.SSPacketHandler;
import shift.sextiarysector.player.CustomPlayerData;
import shift.sextiarysector.player.EntityPlayerManager;
import shift.sextiarysector.player.MoistureStats;
import shift.sextiarysector.player.StaminaStats;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/02.
 * 精霊の人格の管理、メッセージの表示などを扱う
 */
public class SpiritManager {
    private static SpiritManager instance=new SpiritManager();

    private Random rand=new Random();
    private ArrayList<String> characterMale=new ArrayList<String>();
    private ArrayList<String> characterFemale=new ArrayList<String>();
    private ArrayList<ToolEntry> tools=new ArrayList<ToolEntry>();

    private SpiritManager(){}

    public static void register(){
        registerTool(ItemCore.spiritSword, ItemSword.class);

        registerSpiritCharacter(false, "fbs.alice");
        registerSpiritCharacter(false, "fbs.tama");
        registerSpiritCharacter(false, "fbs.hiyo");
        registerSpiritCharacter(false, "fbs.kako");
        registerSpiritCharacter(true, "fbs.d");
        registerSpiritCharacter(true, "fbs.kouta");

        SkillManager.register();
    }

    public static void openGui(EntityPlayer player){
        int x= MathHelper.floor_double(player.posX);
        int y= MathHelper.floor_double(player.posY);
        int z= MathHelper.floor_double(player.posZ);

        player.openGui(FBS.instance, FBS.GUI_SPIRIT_MAIN_ID, player.worldObj, x,y,z);
    }

    public static int findSpiritToolIndex(EntityPlayer player){
        int size=player.inventory.getSizeInventory();
        for(int i=0;i<size;i++){
            ItemStack is=player.inventory.getStackInSlot(i);
            if(is!=null && is.getItem() instanceof ISpiritTool){
                return i;
            }
        }
        return -1;
    }
    public static ItemStack findSpiritTool(EntityPlayer player){
        int i=findSpiritToolIndex(player);
        return i==-1?null:player.inventory.getStackInSlot(i);
    }

    public static void registerTool(Item spiritTool, Class<? extends Item> tool){
        instance.tools.add(new ToolEntry(spiritTool, tool));
    }
    public static ToolEntry getTool(Item item){
        for(ToolEntry te : instance.tools){
            if(te.matches(item)){
                return te;
            }
        }
        return null;
    }
    public static boolean isTool(Item item){
        return getTool(item)!=null;
    }

    /**
     * 契約可能な精霊の人格を登録する
     * @param isMale trueの場合、それは男性であることを示す
     * @param character 精霊の内部名
     */
    public static void registerSpiritCharacter(boolean isMale, String character){
        if(isMale) instance.characterMale.add(character);
        else instance.characterFemale.add(character);
    }

    /**
     * 契約可能な精霊の名前をランダムで取得する
     * @param isMale trueの場合、それは男性であることを示す
     * @return 精霊の内部名
     */
    public static String getRandomCharacter(boolean isMale){
        if(isMale){
            return instance.characterMale.get(instance.rand.nextInt(instance.characterMale.size()));
        }
        else{
            return instance.characterFemale.get(instance.rand.nextInt(instance.characterFemale.size()));
        }
    }

    /**
     * 精霊武器アイテムスタックのNBTを更新する
     * @param itemStack
     * @param status
     */
    public static void updateNBT(ItemStack itemStack, SpiritStatus status){
        NBTTagCompound nbt=new NBTTagCompound();
        SpiritStatus.writeToNBT(status, nbt);
        itemStack.setTagCompound(nbt);
    }

    /**
     * ゲーム中に表示される精霊武器の名前を取得する
     * @param itemStack
     * @return
     */
    public static String getSpiritDisplayName(ItemStack itemStack){
        SpiritStatus ss=SpiritStatus.readFromNBT(itemStack.getTagCompound());
        if(ss==null) return "Unnamed";
        return StatCollector.translateToLocal("item.fbs.spirit.name").replaceAll("%owner%", ss.getOwnerName()).replaceAll("%spirit%", ss.getName());
    }

    /**
     * プレイヤーに対し精霊が話しかける。
     * メッセージは最大で10行(0-9)
     * @param player 対象プレイヤー
     * @param character 人格
     * @param event イベント
     * @param params その他引数
     */
    public static void talk(EntityPlayer player, String character, String event, @Nullable ItemStack spiritToolStack, Object ... params){
        if(player.worldObj.isRemote) return;

        ItemStack itemStack=null;
        SpiritStatus status=null;

        if(spiritToolStack!=null){
            itemStack=spiritToolStack;
        }
        else{
            itemStack=findSpiritTool(player);
        }
        if(itemStack==null) return;
        status=SpiritStatus.readFromNBT(itemStack.getTagCompound());
        if(status==null) return;

        if(!status.isOwner(player)) return;

        //設定を確認する
        if(event.equals("morning") || event.equals("noon") || event.equals("night")){
            if(!status.getConfiguration().get("enableTimeSignalMessage") && !status.getConfiguration().get("enableMessage")) return;
        }
        else if(!status.getConfiguration().get("enableMessage")){
            return;
        }

        SpiritTalkEvent ste=new SpiritTalkEvent(player, character, event, params);
        boolean isCanceled=MinecraftForge.EVENT_BUS.post(ste);
        if(!isCanceled){

            for(int i=0;i<10;i++){
                String m="spirit.talk."+character+"."+event+"."+i;
                if(!StatCollector.canTranslate(m)) break;

                m=translateString(m, status);
                player.addChatComponentMessage(new ChatComponentText("<"+spiritToolStack.getDisplayName()+">"+m));
            }
        }
    }

    /**
     * いい感じにローカライズする
     * @param key
     * @param status
     * @return
     */
    public static String translateString(String key, SpiritStatus status, Object ... params){
        String m=StatCollector.translateToLocal(key);
        m=m.replaceAll("%spirit%", status.getName());
        m=m.replaceAll("%owner%", status.getOwnerName());
        return m;
    }

    public static void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag){
        ISpiritTool ist=(ISpiritTool)itemStack.getItem();
        SpiritStatus ss=SpiritStatus.readFromNBT(itemStack.getTagCompound());

        list.add("[Status]");
        list.add("Level:"+ss.getLv());
        list.add(String.format("Damage:%.1f", ist.calcDamage(ss)+0.5));
        list.add("Harvest Level:"+ist.calcDigLv(ss));

        int t=ist.calcDurable(ss);
        list.add("Durability:"+(t-ss.getItemDamage())+"/"+t);
    }

    /**
     * 精霊の祝福
     * @param player
     */
    public static void bless(EntityPlayer player, ItemStack tool){
        SpiritStatus status=SpiritStatus.readFromNBT(tool.getTagCompound());
        //もうめんどくさいんでハードコード

        //ライフ・満腹度・水分・スタミナのいずれか
        int r=instance.rand.nextInt(FBS.cooperatesSS2?4:2);
        switch (r){
            case 0:
                player.heal(player.getMaxHealth());
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("spirit.bless.fbs.health")));
                break;

            case 1:
                FoodStats fs=player.getFoodStats();
                fs.addStats(20, 1.f);
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("spirit.bless.fbs.food")));
                break;

            case 2:
                instance.blessForSS2(player, 0);
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("spirit.bless.fbs.moisture")));
                break;

            case 3:
                instance.blessForSS2(player, 1);
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("spirit.bless.fbs.stamina")));
                break;
        }


        //さらに、マイナス効果を全て打ち消しランダムでエンチャント効果
        ItemStack dummy=new ItemStack(Items.milk_bucket);
        player.curePotionEffects(dummy);

        Potion p;
        switch (instance.rand.nextInt(13)){
            case 0: p=Potion.damageBoost; break;
            case 1: p=Potion.digSpeed; break;
            case 2: p=Potion.fireResistance; break;
            case 3: p=Potion.invisibility; break;
            case 4: p=Potion.moveSpeed; break;
            case 5: p=Potion.jump; break;
            case 6: p=Potion.nightVision; break;
            case 7: p=Potion.nightVision; break;
            case 8: p=Potion.regeneration; break;
            case 9: p=Potion.resistance; break;
            case 10: p=Potion.waterBreathing; break;
            case 11: p=Registry.potionCleverness; break;
            case 12: p=Registry.potionContract; break;
            default: p=Potion.heal; break;
        }

        player.addPotionEffect(new PotionEffect(p.getId(), 20*(60+3*status.getLv()), status.getLv()/15));
        status.updateLastBlessDate(player.worldObj.getCurrentDate().get(Calendar.DATE));

        NBTTagCompound nbt=new NBTTagCompound();
        SpiritStatus.writeToNBT(status, nbt);
        tool.setTagCompound(nbt);

        //精霊の涙
        ItemStack tear=AlchemyRegistry.getItemStack(ItemCore.alchemyMaterial, status.hasSkill("fbs.sentimental")?2:1, 40);
        if(player.inventory.addItemStackToInventory(tear) && tear.stackSize>0){
            player.entityDropItem(tear, 0);
        }

        talk(player, status.getCharacter(), "bless", tool);
    }

    /**
     * 精霊の修理
     * @param player
     * @param tool
     * @param potionIndex
     */
    public static void repair(EntityPlayer player, ItemStack tool, int potionIndex){
        if(potionIndex==-1){
            int s=player.inventory.getSizeInventory();
            for(int i=0;i<s;i++){
                ItemStack is=player.inventory.getStackInSlot(i);
                if(is==null) continue;
                if(is.getItem()==ItemCore.alchemyPotion && is.getItemDamage()==2){
                    potionIndex=i;
                    break;
                }
            }
        }
        if(potionIndex==-1) return;

        //回復量を得る
        float scale=1.f;
        ItemStack potion=player.inventory.getStackInSlot(potionIndex);
        ArrayList<CharacteristicBase> cbs=AlchemyRegistry.ReadCharacteristicFromNBT(potion.getTagCompound());
        for(CharacteristicBase cb :cbs){
            if(cb instanceof CharacteristicQuality){
                scale=cb.getMPScale();
                break;
            }
        }

        //回復する
        SpiritStatus status=SpiritStatus.readFromNBT(tool.getTagCompound());
        status.repair((int)(500*scale));

        //更新
        NBTTagCompound nbt1=new NBTTagCompound();
        SpiritStatus.writeToNBT(status, nbt1);
        tool.setTagCompound(nbt1);
    }

    public void blessForSS2(EntityPlayer player, int type){
        if(type==0){
            SextiarySectorAPI.addMoistureStats(player, 20, 20);
        }
        else{
            SextiarySectorAPI.addStaminaStats(player, 100, 50);
        }

        if(player instanceof EntityPlayerMP) SSPacketHandler.INSTANCE.sendTo(new PacketPlayerData(EntityPlayerManager.getCustomPlayerData(player)), (EntityPlayerMP)player);
    }

    /**
     * 使用した武器と精霊武器のクラスを関連付ける。
     */
    public static class ToolEntry{
        private Class<? extends Item> tool;
        private Item spiritTool;

        public ToolEntry(Item spiritTool, Class<? extends Item> tool){
            this.spiritTool=spiritTool;
            this.tool=tool;
        }

        public boolean matches(Item item){
            return tool.isAssignableFrom(item.getClass());
        }

        public ItemStack getSpiritToolStack(boolean isMale, String character, String name, EntityPlayer owner, ItemStack material){
            ItemStack ret=new ItemStack(spiritTool);
            SpiritStatus ss=new SpiritStatus(isMale, character, name, owner);

            //先天スキルの設定
            float r=new Random().nextFloat();
            if(r<0.05f) ss.setSkill("fbs.precocious", 0);
            else if(r<0.10f) ss.setSkill("fbs.lateBloomer", 0);
            else if(r<0.15f) ss.setSkill("fbs.hero", 0);
            else if(r<0.20f) ss.setSkill("fbs.skillful", 0);
            else if(r<0.25f) ss.setSkill("fbs.sentimental", 0);

            ((ISpiritTool) spiritTool).calcInitialValue(ss, material);
            updateNBT(ret, ss);

            SanityManager.setSpirit(owner, ss.getName(), ss.getLv());

            return ret;
        }
    }
}
