package jp.plusplus.fbs.item;

import jp.plusplus.fbs.AchievementRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.api.event.CheckingMonocleEvent;
import jp.plusplus.fbs.api.event.DamageMonocleEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class ItemMonocle extends ItemArmor{
    public ItemMonocle(ArmorMaterial p_i45325_1_) {
        super(p_i45325_1_, 0, 0);
        setCreativeTab(FBS.tab);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String layer) {
        Item item=stack.getItem();
        if(item==ItemCore.monocleWood) return FBS.MODID+":textures/armor/monocleWood.png";
        if(item==ItemCore.monocleGold) return FBS.MODID+":textures/armor/monocleGold.png";
        return FBS.MODID+":textures/armor/monocle.png";
    }
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.monocle.0"));
        if(p_77624_1_.getItem()==ItemCore.monocleGold) p_77624_3_.add(StatCollector.translateToLocal("info.fbs.monocle.1"));
    }


    public static ItemStack findMonocle(EntityPlayer player){
        //まずモノクル
        ItemStack helm = player.getCurrentArmor(3);
        if (helm == null || !(helm.getItem() instanceof ItemMonocle)){
            helm=null;
        }

        //だめならイベントで
        CheckingMonocleEvent event=new CheckingMonocleEvent(player, helm);
        MinecraftForge.EVENT_BUS.post(event);

        if(event.hasMonocle()){
            return event.getMonocle();
        }
        return null;
    }
    public static void damageMonocle(EntityPlayer player, ItemStack monocle){
        DamageMonocleEvent event=new DamageMonocleEvent(player, monocle);
        if(!MinecraftForge.EVENT_BUS.post(event)){
            monocle.damageItem(1, player);
            if(monocle.stackSize<=0 || monocle.getItemDamage()>monocle.getMaxDamage()){
                player.playSound("random.break", 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
                for(int i=0;i<player.inventory.getSizeInventory();i++){
                    ItemStack itemStack=player.inventory.getStackInSlot(i);
                    if( itemStack!=null && monocle.isItemEqual(itemStack)){
                        player.inventory.setInventorySlotContents(i, null);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(!world.isRemote) player.triggerAchievement(AchievementRegistry.monocle);
    }
}
