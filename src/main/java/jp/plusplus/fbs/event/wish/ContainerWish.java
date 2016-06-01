package jp.plusplus.fbs.event.wish;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by plusplus_F on 2016/03/31.
 */
public class ContainerWish extends Container {

    public ContainerWish(){}

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
