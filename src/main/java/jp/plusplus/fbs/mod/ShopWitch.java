package jp.plusplus.fbs.mod;

import jp.plusplus.fbs.api.FBSEntityPropertiesAPI;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemFoldingFan;
import jp.plusplus.fbs.item.ItemShovel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shift.mceconomy2.api.shop.IProduct;
import shift.mceconomy2.api.shop.IShop;

import java.util.ArrayList;

/**
 * Created by plusplus_F on 2015/11/02.
 */
public class ShopWitch implements IShop {
    @Override
    public String getShopName(World world, EntityPlayer player) {
        return "Witch's Shop";
    }

    @Override
    public void addProduct(IProduct product) {

    }

    @Override
    public ArrayList<IProduct> getProductList(World world, EntityPlayer player) {
        ArrayList<IProduct> list=new ArrayList<IProduct>();

        int mLv= FBSEntityPropertiesAPI.GetMagicLevelRaw(player);

        //レベルに合わせて商品を変える
        list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 2), 1500));
        list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 9), 1500));
        list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 10), 1500));
        list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 3), 3000));
        list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 0), 5000));
        if(mLv>=2) list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 4), 4000));
        if(mLv>=2) list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 5), 5000));
        if(mLv>=5) list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 1), 8000));
        if(mLv>=5) list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 8), 6000));
        if(mLv>=8) list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 6), 12000));
        if(mLv>=12) list.add(new TFKProductItem(new ItemStack(ItemCore.alchemyRecipe, 1, 7), 20000));

        //このへんは固定
        list.add(new TFKProductItem(new ItemStack(ItemCore.stick), 250));
        list.add(new TFKProductItem(new ItemStack(BlockCore.magicCore), 1000));
        list.add(new TFKProductItem(new ItemStack(ItemCore.instantMana), 3000));
        list.add(new TFKProductItem(ItemShovel.GetItemStack(), 3000));
        list.add(new TFKProductItem(ItemFoldingFan.GetItemStack(), 3000));
        list.add(new TFKProductItem(new ItemStack(ItemCore.potionSan), 6000));
        list.add(new TFKProductItem(new ItemStack(ItemCore.potionOblivion), 12000));
        list.add(new TFKProductItem(new ItemStack(ItemCore.stoneInactive), 50000));
        for(int i=0;i<16;i++) list.add(new TFKProductItem(new ItemStack(ItemCore.charm, 1, i), 200));

        return list;
    }
}
