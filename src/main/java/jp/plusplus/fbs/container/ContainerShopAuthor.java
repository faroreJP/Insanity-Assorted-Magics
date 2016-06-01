package jp.plusplus.fbs.container;

import jp.plusplus.fbs.entity.EntityMagicAuthor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.world.World;
import shift.mceconomy2.api.shop.IShop;
import shift.mceconomy2.gui.ContainerShop;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class ContainerShopAuthor extends ContainerShop {
    protected EntityMagicAuthor author;

    public ContainerShopAuthor(EntityPlayer entityPlayer, IShop par2IProductList, World par3World, EntityMagicAuthor author) {
        super(entityPlayer, par2IProductList, par3World);
        this.author=author;
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        if(author!=null){
            author.setCustomer(null);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return author==null || author.getCustomer() == entityplayer;
    }
}
