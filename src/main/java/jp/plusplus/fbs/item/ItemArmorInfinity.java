package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/11/22.
 */
public class ItemArmorInfinity extends ItemArmor {
    public ItemArmorInfinity(ArmorMaterial p_i45325_1_, int p_i45325_3_) {
        super(p_i45325_1_, 0, p_i45325_3_);
        setCreativeTab(FBS.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.infinity.0"));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String layer) {
        if(slot==0 || slot==1 || slot==3) return FBS.MODID+":textures/armor/infinity1.png";
        else return FBS.MODID+":textures/armor/infinity2.png";
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        Item item=itemStack.getItem();

        if(item==ItemCore.infinityHelm){
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), 10, 0));
            //player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 10, 0));
        }
        else if(item==ItemCore.infinityArmor){
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 10, 0));
            player.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 10, 0));
        }
        else if(item==ItemCore.infinityLegs){
            player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 10, 1));
        }
        else if(item==ItemCore.infinityBoots){
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 10, 0));
            player.addPotionEffect(new PotionEffect(Potion.jump.getId(), 10, 0));
        }

    }
}
