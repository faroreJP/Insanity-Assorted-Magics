package jp.plusplus.fbs.container.inventory;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.container.ContainerMagic;
import jp.plusplus.fbs.container.slot.SlotMagic;
import jp.plusplus.fbs.container.slot.SlotMagicCopy;
import jp.plusplus.fbs.container.slot.SlotMagicTakeOnly;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemBookSorcery;
import jp.plusplus.fbs.api.MagicBase;
import jp.plusplus.fbs.tileentity.TileEntityMagicCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by pluslus_F on 2015/06/18.
 */
public class InventoryMagicCopy extends InventoryMagic {
    public static final ResourceLocation rl=new ResourceLocation(FBS.MODID, "textures/gui/magicCopy.png");

    public InventoryMagicCopy(TileEntityMagicCore te, EntityPlayer player) {
        super(4, te, player);
    }

    @Override
    public void work() {
        if(itemStacks[0]==null) return;

        //コピー魔法が無かったら
        MagicBase mb=Registry.GetMagic("fbs.copy").getMagic(this.player.worldObj, this.player, true);
        if(mb==null) return;

        boolean crm=player.capabilities.isCreativeMode;

        if(crm || mb.checkSuccess()){
            //クリエイティブか、魔法に成功すると
            for(int i=1;i<3;i++){
                if(itemStacks[i]==null){
                    itemStacks[i]=itemStacks[0].copy();
                }
                else if(itemStacks[i].isItemEqual(itemStacks[0])){
                    itemStacks[i].stackSize++;
                }
            }
        }
        else {
            SanityManager.loseSanity(player, 2, 10, true);
            if(itemStacks[1]==null) itemStacks[1]=itemStacks[0];
            else if(itemStacks[1].isItemEqual(itemStacks[0])) itemStacks[1].stackSize++;
        }

        //魔導書の回数を減らす
        if(itemStacks[3]!=null && !crm) ItemBookSorcery.reduceMagicMaxUse(itemStacks[3]);

        itemStacks[0]=null;
        onInventoryChanged(0);
    }

    @Override
    public void onInventoryChanged(int index) {
        //FMLLog.info("changed");

        //増やしたいアイテムが無い場合、魔導書が無い場合進捗を無に帰す
        if (itemStacks[0] == null || itemStacks[3] == null) {
            magicCore.resetProgress();
            //FMLLog.info("item null");
            return;
        }

        //本がおかしくても進捗を無に帰す
        Registry.BookData bd = Registry.GetBookDataFromItemStack(itemStacks[3]);
        if (bd == null || !bd.title.equals("fbs.copy") || ItemBookSorcery.getMagicMaxUse(itemStacks[3]) <= 0) {
            magicCore.resetProgress();
            return;
        }

        //増やせそうにない場合も進捗を無に帰す
        for (int i = 1; i < 3; i++) {
            if (itemStacks[i] == null) continue;
            if (!itemStacks[i].isItemEqual(itemStacks[0]) || itemStacks[i].stackSize + 1 > itemStacks[i].getMaxStackSize()) {
                magicCore.resetProgress();
                return;
            }
        }

        //進捗の最大値を設定する
        magicCore.setProgressMax(20 * 10);
    }

    @Override
    public void addSlotToContainer(ContainerMagic cm) {
        cm.addSlotToContainer(new SlotMagicCopy(this, 0, 44, 36));
        cm.addSlotToContainer(new SlotMagicTakeOnly(this, 1, 103, 36));
        cm.addSlotToContainer(new SlotMagicTakeOnly(this, 2, 129, 36));
        cm.addSlotToContainer(new SlotMagic(this, 3, 72, 17));
    }

    @Override
    public ResourceLocation getResource() {
        return rl;
    }

    @Override
    public String getInventoryName() {
        return Registry.GetLocalizedBookTitle("fbs.copy");
    }
}
