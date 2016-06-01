package jp.plusplus.fbs.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by pluslus_F on 2015/06/17.
 * 精霊武器との会話イベント
 */
public class SpiritTalkEvent extends PlayerEvent {
    private String character;
    private String event;
    private Object[] params;
    public SpiritTalkEvent(EntityPlayer player, String character, String event, Object ... params) {
        super(player);
        this.character=character;
        this.event=event;
        this.params=params;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public String getCharacter(){ return character; }
    public String getEvent(){ return event; }
    public Object[] getParams(){ return params; }
}
