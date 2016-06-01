package jp.plusplus.fbs.api.event;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Createdby pluslus_Fon 2015/06/05.
 *
 * newChangeLVは機能しないので注意
 */
public class PlayerSanityEvent extends PlayerEvent {
    private int changeSanity, changeLv;
    private double changeExp;

    public int newChangeSanity, newChangeLv;
    public double newChangeExp;

    public PlayerSanityEvent(EntityPlayer player, int ds, double de, int dl) {
        super(player);
        newChangeSanity=changeSanity=ds;
        newChangeExp=changeExp=de;
        newChangeLv=changeLv=dl;
    }

    public boolean isCancelable() {
        return true;
    }

    public int getChangeSanity(){ return changeSanity; }
    public int getChangeLv(){ return changeLv; }
    public double getChangeEXP(){ return changeExp; }
}
