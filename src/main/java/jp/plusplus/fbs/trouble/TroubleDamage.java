package jp.plusplus.fbs.trouble;

import jp.plusplus.fbs.Registry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shift.sextiarysector.player.CustomPlayerData;
import shift.sextiarysector.player.EntityPlayerManager;
import shift.sextiarysector.player.MoistureStats;

/**
 * Created by plusplus_F on 2015/09/17.
 */
public class TroubleDamage extends TroubleBase {
    public TroubleDamage() {
        super(10);
    }

    @Override
    public void done(World world, EntityPlayer player, Registry.BookData bd) {
        int amount=2+(int)(18.0*bd.lv/50.0);
        player.attackEntityFrom(new DamageSource("evil.0"), amount);
    }

    @Override
    public String getMessage(){ return super.getMessage()+".damage"; }
}
