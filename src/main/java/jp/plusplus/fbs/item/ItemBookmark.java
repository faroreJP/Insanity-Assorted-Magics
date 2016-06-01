package jp.plusplus.fbs.item;

import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.world.structure.MapGenSealdLib;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.MapGenStructureIO;

import java.util.List;

/**
 * Created by pluslus_F on 2015/06/23.
 */
public class ItemBookmark extends ItemBase {
    public ItemBookmark() {
        setUnlocalizedName("bookmark");
        setTextureName("bookmark");
        setMaxDamage(64);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player){
        if(!world.isRemote){
            /*
            まー言いたい事はいろいろあるけどさ、
            なんでfindClosestStructureが実質Stronghold限定の実装なのよ。
            わざわざ引数に構造物名とっといてさ。

            ＠もやん
            　　　　fﾆヽ
            　　　　|_||
            　　　　|= |
            　　　　|　|
            　　　　|= |
            　　 i⌒|　|⌒i_
            　 ／|　|　|　| ヽ
            　｜ (　(　(　(　|
            　｜/　　　　　　|
            　｜　　　　　　 |
            　 ＼　　　　　 ノ
            　　 ＼　　　　/
            　　　｜　　　｜
            */
            item.damageItem(1, player);

            int x=MathHelper.floor_double(player.posX), z=MathHelper.floor_double(player.posZ);
            int dx=(x/16)-1, dz=(z/16);

            //FBS.logger.info("at:"+dx+","+dz);

            //強硬手段
            for(int i=10;i<50;i++){
                if(world.getBlock(x, i, z)== BlockCore.plank){
                    player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.shining"));
                    break;
                }
            }

            if(MapGenSealdLib.isLibraryChunk(world, dx, dz)){
                player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.success"));
            }
            else{
                boolean found=false;
                for(int i=0;i<3 && !found;i++){
                    for(int k=0;k<3 && !found;k++){
                        if(MapGenSealdLib.isLibraryChunk(world, dx+(i-1), dz+(k-1))){
                            found=true;
                        }
                    }
                }
                if(found){
                    player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.found"));
                }
                else{
                    player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.failure"));
                }
            }
        }


        return item;
    }

    /*
    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        if(world.isRemote) return true;

        item.damageItem(1, player);

        world.findClosestStructure("Sealed Library", )

        if(world.provider.dimensionCrackId!=0){
            player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.failure"));
            return true;
        }

        int dx=(x/16)-1, dz=(z/16);

        if(MapGenSealdLib.isLibraryChunk(world, dx, dz)){
            player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.success"));
        }
        else{
            boolean found=false;
            for(int i=0;i<3 && !found;i++){
                for(int k=0;k<3 && !found;k++){
                    if(MapGenSealdLib.isLibraryChunk(world, dx+(i-1), dz+(k-1))){
                        found=true;
                    }
                }
            }
            if(found){
                player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.found"));
            }
            else{
                player.addChatComponentMessage(new ChatComponentTranslation("info.fbs.bookmark.failure"));
            }
        }

        return true;
    }
    */

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        p_77624_3_.add(StatCollector.translateToLocal("info.fbs.bookmark.0"));
    }
}
