package jp.plusplus.fbs.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.entity.EntityButterfly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by plusplus_F on 2015/08/23.
 * 虫取り網
 */
public class ItemNet extends ItemBase {
    public ItemNet(){
        setUnlocalizedName("net");
        setTextureName("net");
        setFull3D();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World w, EntityPlayer p) {
        if(w.isRemote) return item;

        FBS.logger.info("net!");

        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(p.posX-0.5, p.posY+p.getEyeHeight()-0.5, p.posZ-0.5, p.posX+0.5, p.posY+p.getEyeHeight()+0.5, p.posZ+0.5);
        aabb=aabb.expand(4,2,4);
        List list=w.getEntitiesWithinAABB(EntityButterfly.class, aabb);
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                EntityButterfly eb=(EntityButterfly)list.get(i);
                eb.setDead();
                eb.entityDropItem(new ItemStack(ItemCore.butterfly), 0);
                /*
                p.inventory.addItemStackToInventory(new ItemStack(ItemCore.butterfly));
                p.inventory.markDirty();
                */
            }
        }

        return item;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if(!FBS.enableDescription) return;
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.net.0"));
    }
}
