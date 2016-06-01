package jp.plusplus.fbs.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.FMLLog;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.event.PlayerUseMagicEvent;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.api.IMagicEnchant;
import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class ItemBookSorcery extends ItemBookNoDecoded {
    Random rand=new Random();

    public ItemBookSorcery(){
        setMaxStackSize(1);
        setCreativeTab(null);
        setUnlocalizedName("bookDec");
        setTextureName("bookNoDecoded");
        setHasSubtypes(true);
    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int i){
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_){
        Registry.MagicData md=Registry.GetMagicDataFromItemStack(p_77626_1_);
        if(md==null) return 32;
        return md.ariaTick;
    }
    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return FBS.actionSpell;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if(getMagicMaxUse(p_77659_1_)>0) {
            if (p_77659_3_.isSneaking()) {
                executesMagic(p_77659_1_, p_77659_2_, p_77659_3_, false);
            } else {
                p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
            }
        }
        return p_77659_1_;
    }
    @Override
    public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
        if(getMagicMaxUse(itemStack)>0){
            executesMagic(itemStack, world, player, true);
        }
        return itemStack;
    }
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        Registry.BookData bd = Registry.GetBookDataFromItemStack(itemStack);
        Registry.MagicData md = Registry.GetMagicDataFromItemStack(itemStack);
        if (bd == null || md==null) return;

        //list.add("\"" + bd.getLocalizedTitle() + "\"");
        list.add(I18n.format("info.fbs.book.decoder")+":"+itemStack.getTagCompound().getString("decoder"));
        list.add(I18n.format("info.fbs.book.lv") + ":" + bd.lv);

        String s=I18n.format("magic.type.fbs.type") + ":" + I18n.format("magic.type."+md.type);
        if(IMagicEnchant.class.isAssignableFrom(md.magic)) s+=","+I18n.format("magic.type.fbs.enchant");
        list.add(s);

        for(int i=0;i<5;i++){
            String sss="flavor."+bd.title+"."+i;
            if(!StatCollector.canTranslate(sss)) break;
            list.add(ChatFormatting.ITALIC+StatCollector.translateToLocal(sss));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack item){
        return super.getItemStackDisplayName(item)+"("+StatCollector.translateToLocal("info.fbs.book.decoded")+")["+getMagicMaxUse(item)+"]";
    }

    public void executesMagic(ItemStack itemStack, World world, EntityPlayer player, boolean spell){
        if(!itemStack.hasTagCompound() || world.isRemote) return;

        Registry.MagicData md=Registry.GetMagicDataFromItemStack(itemStack);
        if(md==null) return;

        try {
            MagicBase mb = md.getMagic(world, player, spell);

            String cir=mb.getMagicCircleName();
            if(cir!=null && !cir.equals("null") && !mb.checkMagicCircle(cir)){
                player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.magic.circle.need"));
                return;
            }

            PlayerUseMagicEvent pume=new PlayerUseMagicEvent.Pre(player, mb, new ItemStack[]{itemStack});
            if(!MinecraftForge.EVENT_BUS.post(pume)){

                double exp=md.exp;
                if(mb.checkSuccess() || player.capabilities.isCreativeMode){
                    mb.success();
                }
                else{
                    mb.failureMessage();
                    mb.failure();
                    exp*=0.2;
                }
                if(!spell) exp*=0.1;
                SanityManager.addExp(player, exp, true);

                pume=new PlayerUseMagicEvent.Post(player, mb, new ItemStack[]{itemStack});
                MinecraftForge.EVENT_BUS.post(pume);
            }
        }
        catch (Exception e){
            FMLLog.severe("Error! magic:"+md.title);
            e.printStackTrace();
        }

        if(!player.capabilities.isCreativeMode){
            reduceMagicMaxUse(itemStack);
        }
    }
    public void executesMagicWithStaff(World world, EntityPlayer player, ItemStack[] staffItems, int bookNum) {
        if (world.isRemote) return;

        //共鳴しているかどうかの判定用リストを作成する
        LinkedList<ItemStack> books = new LinkedList<ItemStack>();
        for (int i = 0; i < bookNum; i++) {
            if (staffItems[i] != null && staffItems[i].getItem() == ItemCore.bookSorcery) {
                books.add(staffItems[i]);
            }
        }
        if (books.isEmpty()) return;

        //共鳴している魔法がデータとして存在するか確認
        Registry.MagicData md = Registry.GetResonanceMagicData(books);
        if (md == null) return;

        try {
            //魔法のインスタンスを作成
            MagicBase mb = md.getMagic(world, player, false);

            //その魔法が魔法陣を必要とする場合、要求された魔法陣の中心にいるかどうかの判定
            String cir=mb.getMagicCircleName();
            if(cir!=null && !cir.equals("null") && !mb.checkMagicCircle(cir)){
                player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.magic.circle.need"));
                return;
            }

            //魔法使用直前のイベントを発生させ、キャンセルされていない場合魔法を実行する
            if(!MinecraftForge.EVENT_BUS.post(new PlayerUseMagicEvent.Pre(player, mb, books.toArray(new ItemStack[books.size()])))){
                boolean sc = mb.checkSuccess();
                double exp = md.exp;
                mb.isSpelled = true;
                if (sc || player.capabilities.isCreativeMode) {
                    mb.success();
                } else {
                    //失敗
                    mb.failureMessage();

                    //スタッフにセットされているチャームを探す
                    int charm = -1;
                    for (int i = bookNum; i < staffItems.length; i++) {
                        if (staffItems[i] != null && staffItems[i].getItem() == ItemCore.charm) {
                            charm = i;
                            break;
                        }
                    }

                    //チャームがセットされている場合、それを消費して魔法失敗時のデメリット回避
                    if (charm != -1) {
                        staffItems[charm].stackSize--;
                        if (staffItems[charm].stackSize <= 0) staffItems[charm] = null;
                        player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("info.fbs.magic.charm")));
                    } else mb.failure();

                    exp *= 0.2;
                }

                exp *= 0.5;//スタッフ使用時、経験値に補正をかける
                SanityManager.addExp(player, exp, true);

                //魔法使用直後のイベント発生
                MinecraftForge.EVENT_BUS.post(new PlayerUseMagicEvent.Post(player, mb, books.toArray(new ItemStack[books.size()])));
            }
        } catch (Exception e) {
            FMLLog.severe("Error! magic:" + md.title);
            e.printStackTrace();
        }

        //魔導書の使用回数を減らす
        if (!player.capabilities.isCreativeMode) {
            for (ItemStack item : books) {
                reduceMagicMaxUse(item);
            }
        }
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList) {
        itemList.add(new ItemStack(this, 1, itemRand.nextInt(0xffffff+1)));
    }

    public static void setMagicMaxUse(ItemStack item, Registry.MagicData md){
        NBTTagCompound nbt=item.getTagCompound();
        if(nbt==null) return;
        if(nbt.hasKey("useAmount")) nbt.removeTag("useAmount");
        nbt.setInteger("useAmount", md.minUse+itemRand.nextInt(md.maxUse-md.minUse+1));
    }
    public static void setMagicMaxUse(ItemStack item, int num){
        NBTTagCompound nbt=item.getTagCompound();
        if(nbt==null) return;
        if(nbt.hasKey("useAmount")) nbt.removeTag("useAmount");
        nbt.setInteger("useAmount", num);
    }
    public static int getMagicMaxUse(ItemStack item){
        NBTTagCompound nbt=item.getTagCompound();
        if(nbt==null) return 0;
        return nbt.getInteger("useAmount");
    }
    public static void reduceMagicMaxUse(ItemStack item){
        NBTTagCompound nbt=item.getTagCompound();
        if(nbt==null) return;
        int c=nbt.getInteger("useAmount")-1;
        nbt.removeTag("useAmount");
        nbt.setInteger("useAmount", c);
    }
}
