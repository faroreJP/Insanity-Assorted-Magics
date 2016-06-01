package jp.plusplus.fbs.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by plusplus_F on 2015/11/09.
 * 右クリックで収穫可能なブロック
 */
public interface IHarvestable {
    /**
     * 収穫可能な状態にあるか調べる
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean canHarvest(World world, int x, int y, int z);

    /**
     * 収穫可能な状態を目指して成長させる
     * @param world
     * @param x
     * @param y
     * @param z
     * @param rand
     */
    public void glow(World world, int x, int y, int z, Random rand);

    /**
     * 収穫可能なアイテムの一覧を入手する
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public ArrayList<ItemStack> getHarvestItems(World world, int x, int y, int z);

    /**
     * プレイヤーによって収穫する
     * @param world
     * @param x
     * @param y
     * @param z
     * @param player
     * @param rand
     * @return
     */
    public ArrayList<ItemStack> harvest(World world, int x, int y, int z, Random rand, EntityPlayer player);

    /**
     * 収穫機など、プレイヤー以外が収穫する
     * @param world
     * @param x
     * @param y
     * @param z
     * @param rand
     * @return
     */
    public ArrayList<ItemStack> harvest(World world, int x, int y, int z, Random rand);
}
