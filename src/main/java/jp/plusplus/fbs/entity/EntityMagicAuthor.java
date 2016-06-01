package jp.plusplus.fbs.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shift.mceconomy2.api.MCEconomyAPI;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class EntityMagicAuthor extends EntityVillager {
    public EntityMagicAuthor(World p_i1748_1_) {
        super(p_i1748_1_);

        //必要のないタスクを削除
        for(int i=0;i<tasks.taskEntries.size();i++){
            EntityAIBase ai=((EntityAITasks.EntityAITaskEntry)tasks.taskEntries.get(i)).action;
            if(ai instanceof EntityAIVillagerMate || ai instanceof EntityAIFollowGolem){
                tasks.removeTask(ai);
            }
        }


    }

    public void onLivingUpdate(){
        super.onLivingUpdate();
        setGrowingAge(100);
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack itemstack = player.inventory.getCurrentItem();
        boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;

        if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking()) {
            if (!this.worldObj.isRemote) {
                //this.setCustomer(player);
                //MCEconomyAPI.openShopGui(Registry.shopAuthorId,player,worldObj,(int)posX, (int)posY, (int)posZ);
                player.openGui(FBS.instance, FBS.GUI_SHOP_AUTHOR_ID, worldObj, (int)posX, (int)posY, (int)posZ);
            }

            return true;
        } else {
            return super.interact(player);
        }
    }
}
