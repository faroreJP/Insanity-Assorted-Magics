package jp.plusplus.fbs.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by plusplus_F on 2016/03/09.
 */
public class PlayerSanityRollEvent extends PlayerEvent {
    private int trial, max;
    public int newTrial, newMax;

    public PlayerSanityRollEvent(EntityPlayer player, int trial, int max) {
        super(player);
        this.trial=newTrial=trial;
        this.max=newMax=max;
    }

    @Override
    public boolean isCancelable(){ return true; }

    public int getTrial(){ return trial; }
    public int getMax(){ return max; }
}
