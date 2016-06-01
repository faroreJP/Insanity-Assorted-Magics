package jp.plusplus.fbs.item;

import jp.plusplus.fbs.block.BlockAlchemistTable;
import jp.plusplus.fbs.block.BlockAlchemyCauldron;
import jp.plusplus.fbs.block.BlockCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plusplus_F on 2015/09/25.
 * 死ね
 */
public class ItemAlchemyCauldron extends ItemBlock {
    public ItemAlchemyCauldron(Block p_i45328_1_) {
        super(p_i45328_1_);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer p_77624_2_, List list, boolean flag){
        list.add(StatCollector.translateToLocal("info.fbs.alchemyCauldron.0"));
        list.add("Size:W2H1D2");
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int pSide=0;

        /*
        //プレイヤーの向きの決定
        switch (l){
            case 0: pSide=2; break;
            case 1: pSide=5; break;
            case 2: pSide=3; break;
            case 3: pSide=4; break;
        }
        */
        pSide=(l==0||l==2)?0:8;

        ArrayList<BlockAlchemyCauldron.BlockPos> pos= ((BlockAlchemyCauldron)field_150939_a).getBlocks(world, x, y, z);
        for(BlockAlchemyCauldron.BlockPos bp : pos){
            if(!bp.getBlock(world).isReplaceable(world, bp.x, bp.y, bp.z)) return false;
        }

        return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, pSide);
    }
}
