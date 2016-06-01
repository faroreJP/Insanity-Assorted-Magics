package jp.plusplus.fbs.magic;

import jp.plusplus.fbs.api.MagicBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;

/**
 * Createdby pluslus_Fon 2015/06/14.
 */
public class MagicReturn extends MagicBase {
    @Override
    public boolean checkSuccess() {
        if(!isSpelled) return false;

        float prob=0.4f+0.02f*property.getMagicLevel();
        return rand.nextFloat()<=prob;
    }

    @Override
    public void success() {
        //EntityClientPlayerMP
        //player.respawnPlayer();
        ChunkCoordinates pos=player.getBedLocation(0);
        if(pos==null){
            pos=world.getSpawnPoint();
            pos.posY=world.getChunkFromBlockCoords(pos.posX, pos.posZ).getHeightValue(pos.posX%16, pos.posZ%16);
        }
        else{
            Block b0=world.getBlock(pos.posX+1, pos.posY, pos.posZ);
            Block b1=world.getBlock(pos.posX, pos.posY, pos.posZ+1);
            Block b2=world.getBlock(pos.posX-1, pos.posY, pos.posZ);
            Block b3=world.getBlock(pos.posX, pos.posY, pos.posZ-1);

            Block b4=world.getBlock(pos.posX, pos.posY+1, pos.posZ);

            if((b0!=Blocks.bed && b1!=Blocks.bed && b2!=Blocks.bed && b3!=Blocks.bed) || b4.getMaterial()!=Material.air){
                pos=world.getSpawnPoint();
                pos.posY=world.getChunkFromBlockCoords(pos.posX, pos.posZ).getHeightValue(pos.posX%16, pos.posZ%16);
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("tile.bed.notValid")));
            }
        }
        player.setPositionAndUpdate(pos.posX, pos.posY+0.2, pos.posZ);
    }

    @Override
    public void failure() {
        sanity(2, 6);
    }
}
