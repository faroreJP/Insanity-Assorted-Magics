package jp.plusplus.fbs.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class ContainerDummy extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return false;
    }
}
