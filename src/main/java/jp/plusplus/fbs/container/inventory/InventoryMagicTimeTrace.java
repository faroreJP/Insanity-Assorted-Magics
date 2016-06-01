package jp.plusplus.fbs.container.inventory;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.container.ContainerMagic;
import jp.plusplus.fbs.container.slot.SlotMagic;
import jp.plusplus.fbs.container.slot.SlotMagicCopy;
import jp.plusplus.fbs.container.slot.SlotMagicTakeOnly;
import jp.plusplus.fbs.container.slot.SlotMagicTimeTrace;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.mod.ForIR3;
import jp.plusplus.fbs.mod.ForSS2;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by plusplus_F on 2016/02/28.
 */
public class InventoryMagicTimeTrace extends InventoryMagic {
    public static final ResourceLocation rl=new ResourceLocation(FBS.MODID, "textures/gui/magicProcessing.png");

    public InventoryMagicTimeTrace(TileEntityMagicCore te, EntityPlayer player) {
        super(4, te, player);
    }

    public float getRepairPercent(){
        return 0.5f*rand.nextFloat();
    }

    @Override
    public void work() {
        if(itemStacks[0]==null) return;

        //魔法が無かったら
        MagicBase mb=Registry.GetMagic("fbs.timeTrace").getMagic(this.player.worldObj, this.player, true);
        if(mb==null) return;

        boolean crm=player.capabilities.isCreativeMode;
        if(crm || mb.checkSuccess()){
            //クリエイティブか、魔法に成功すると
            SanityManager.loseSanity(player, 1, 10, true);

            ItemStack itemStack=itemStacks[0].copy();
            if(FBS.cooperatesIR3 && ForIR3.isCrystalUnit(itemStack)){
                ForIR3.repairCrystalUnit(itemStack, getRepairPercent());
            }
            else if(FBS.cooperatesSS2 && ForSS2.canTimeTrace(itemStack)){
                itemStack=ForSS2.getTimeTraced(itemStack);
            }
            else if(itemStack.getItem().isItemTool(itemStack)){
                int d=itemStack.getItemDamage();
                d=Math.max(0, d-(int)(itemStack.getMaxDamage()*getRepairPercent()));
                itemStack.setItemDamage(d);
            }

            if(itemStacks[1]==null){
                itemStacks[1]=itemStack;
            }
            else if(itemStacks[1].isItemEqual(itemStack) && itemStacks[1].stackSize+itemStack.stackSize<=itemStack.getMaxStackSize()){
                itemStacks[1].stackSize+=itemStack.stackSize;
            }
        }
        else {
            SanityManager.loseSanity(player, 1, 10, true);
            if(itemStacks[1]==null) itemStacks[1]=itemStacks[0];
            else if(itemStacks[1].isItemEqual(itemStacks[0])) itemStacks[1].stackSize++;
        }

        //魔導書の回数を減らす
        if(itemStacks[2]!=null && !crm) ItemBookSorcery.reduceMagicMaxUse(itemStacks[2]);

        itemStacks[0]=null;
        onInventoryChanged(0);
    }

    @Override
    public void onInventoryChanged(int index) {
        //増やしたいアイテムが無い場合、魔導書が無い場合進捗を無に帰す
        if (itemStacks[0] == null || itemStacks[2] == null) {
            magicCore.resetProgress();
            //FMLLog.info("item null");
            return;
        }

        //本がおかしくても進捗を無に帰す
        Registry.BookData bd = Registry.GetBookDataFromItemStack(itemStacks[2]);
        if (bd == null || !bd.title.equals("fbs.timeTrace") || ItemBookSorcery.getMagicMaxUse(itemStacks[2]) <= 0) {
            magicCore.resetProgress();
            return;
        }

        //処理できそうにない場合も進捗を無に帰す
        ItemStack get=null;
        ItemStack itemStack=itemStacks[0].copy();
        if(FBS.cooperatesIR3 && ForIR3.isCrystalUnit(itemStack)){
            get=itemStack;
        }
        else if(FBS.cooperatesSS2 && ForSS2.canTimeTrace(itemStack)){
            get=ForSS2.getTimeTraced(itemStack);
        }
        else if(itemStack.getItem().isItemTool(itemStack)){
            get=itemStack;
        }

        //変換できない・完成品スロットのアイテムが違う・完成品スロットのスタック数がmaxのとき、失敗
        if(get==null || (itemStacks[1]!=null && (!itemStacks[1].isItemEqual(get) || itemStacks[1].stackSize+get.stackSize>itemStacks[1].getMaxStackSize()))){
            magicCore.resetProgress();
            return;
        }

        //進捗の最大値を設定する
        magicCore.setProgressMax(20 * 3);
    }

    @Override
    public void addSlotToContainer(ContainerMagic cm) {
        cm.addSlotToContainer(new SlotMagicTimeTrace(this, 0, 44, 36));
        cm.addSlotToContainer(new SlotMagicTakeOnly(this, 1, 103, 36));
        cm.addSlotToContainer(new SlotMagic(this, 2, 72, 17));
    }

    @Override
    public ResourceLocation getResource() {
        return rl;
    }

    @Override
    public String getInventoryName() {
        return Registry.GetLocalizedBookTitle("fbs.timeTrace");
    }
}
