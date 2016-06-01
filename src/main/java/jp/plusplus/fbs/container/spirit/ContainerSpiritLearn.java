package jp.plusplus.fbs.container.spirit;

import jp.plusplus.fbs.container.slot.SlotShowOnly;
import jp.plusplus.fbs.spirit.ISpiritTool;
import jp.plusplus.fbs.spirit.SkillManager;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.spirit.SpiritStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by plusplus_F on 2015/11/29.
 */
public class ContainerSpiritLearn extends Container {
    public EntityPlayer player;
    public ItemStack spiritTool;
    public SpiritStatus status;

    public ContainerSpiritLearn(EntityPlayer player){
        this.player=player;
        spiritTool= SpiritManager.findSpiritTool(player);
        status=SpiritStatus.readFromNBT(spiritTool.getTagCompound());

        for (int j = 0; j < 9; ++j) {
            ItemStack is=player.inventory.getStackInSlot(j);
            if(is!=null && is.getItem() instanceof ISpiritTool) this.addSlotToContainer(new SlotShowOnly(player.inventory, j, 8 + j * 18, 142));
            else this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        ItemStack itemstack = null;
        return itemstack;
    }

    public void learn(String skill){
        int now=status.getSkillLevel(skill);
        status.reduceSkillPoint();
        status.setSkill(skill, now+1);
        SpiritManager.updateNBT(spiritTool, status);
    }
}
