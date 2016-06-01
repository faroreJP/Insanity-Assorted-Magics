package jp.plusplus.fbs.gui;

import jp.plusplus.fbs.container.ContainerShopAuthor;
import jp.plusplus.fbs.entity.EntityMagicAuthor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import shift.mceconomy2.api.shop.IShop;
import shift.mceconomy2.gui.GuiShop;

/**
 * Created by plusplus_F on 2016/02/24.
 */
public class GuiShopAuthor extends GuiShop {
    public GuiShopAuthor(EntityPlayer entityPlayer, IShop productList, int id, World world, EntityMagicAuthor author) {
        super(entityPlayer, productList, id, world);
        inventorySlots=new ContainerShopAuthor(entityPlayer, productList, world, author);
    }
}
