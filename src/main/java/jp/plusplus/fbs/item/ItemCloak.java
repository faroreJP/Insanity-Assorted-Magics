package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/08/25.
 */
public class ItemCloak extends ItemArmor {
    public ItemCloak(ArmorMaterial p_i45325_1_) {
        super(p_i45325_1_, 0, 1);
        setCreativeTab(FBS.tab);
        setUnlocalizedName("fbs.cloak");
        setTextureName(FBS.MODID+":cloak");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String layer) {
            return FBS.MODID+":textures/armor/monocle.png";
    }
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.cloak.0"));
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.valuable"));
    }
}
