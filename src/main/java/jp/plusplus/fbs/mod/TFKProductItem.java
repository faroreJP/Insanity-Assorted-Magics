package jp.plusplus.fbs.mod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shift.mceconomy2.api.shop.IProduct;
import shift.mceconomy2.api.shop.IShop;

/**
 * Created by plusplus_F on 2015/11/02.
 */
public class TFKProductItem implements IProduct {
    protected ItemStack product;
    protected int cost;

    public TFKProductItem(ItemStack p, int c){
        product=p;
        cost=c;
    }

    @Override
    public ItemStack getItem(IShop shop, World world, EntityPlayer player) {
        return product;
    }

    @Override
    public int getCost(IShop shop, World world, EntityPlayer player) {
        return cost;
    }

    @Override
    public boolean canBuy(IShop shop, World world, EntityPlayer player) {
        return true;
    }
}
