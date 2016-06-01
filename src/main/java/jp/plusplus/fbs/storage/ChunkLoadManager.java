package jp.plusplus.fbs.storage;

import com.google.common.collect.ImmutableSetMultimap;
import jp.plusplus.fbs.FBS;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by plusplus_F on 2016/03/07.
 */
public class ChunkLoadManager implements ForgeChunkManager.LoadingCallback{
    private static ChunkLoadManager obj=new ChunkLoadManager();
    private static HashMap<Pos, ForgeChunkManager.Ticket> tickets=new HashMap<Pos, ForgeChunkManager.Ticket>();

    //インスタンスの取得
    public static ChunkLoadManager instance(){
        return obj;
    }

    //チャンクローダの登録
    public static void setChunkLoader(World w, int x, int y, int z){
        if(w==null){
            FBS.logger.error("Error! World is null.");
            return;
        }

        Pos p=new Pos(x,y,z);
        for(Pos pos : tickets.keySet()){
            if(pos.equals(p)){
                FBS.logger.error("Error! Already registered.");
                return;
            }
        }

        //チケットの要求
        ForgeChunkManager.Ticket t=ForgeChunkManager.requestTicket(FBS.instance, w, ForgeChunkManager.Type.NORMAL);
        if(t==null){
            FBS.logger.error("Error! Couldn't get ticket.");
            return;
        }

        //チケットに情報書き込み
        t.getModData().setString("type", "block");
        t.getModData().setInteger("x", x);
        t.getModData().setInteger("y", y);
        t.getModData().setInteger("z", z);

        //チケットをどうたら
        tickets.put(p, t);
        ForgeChunkManager.forceChunk(t, w.getChunkFromBlockCoords(x, z).getChunkCoordIntPair());
        FBS.logger.info("Added ChunkLoader at "+w.provider.getDimensionName()+"("+x+","+y+","+z+")");
    }

    //チャンクローダの削除
    public static void removeChunkLoader(World world, int x, int y, int z){
        Pos p=new Pos(x,y,z);

        //チケットが存在するか確認
        for(Pos pos : tickets.keySet()){
            if(pos.equals(p)){
                //チャンクロードの停止
                ForgeChunkManager.Ticket t=tickets.get(pos);

                World w=t.world;
                if(w!=null){
                    ImmutableSetMultimap<ChunkCoordIntPair, ForgeChunkManager.Ticket> map=ForgeChunkManager.getPersistentChunksFor(t.world);
                    if(map!=null && !map.isEmpty()){
                        ForgeChunkManager.unforceChunk(t, world.getChunkFromBlockCoords(x, z).getChunkCoordIntPair());
                        FBS.logger.info("Removed chunk loader at "+world.provider.getDimensionName()+"("+x+","+y+","+z+")");
                    }
                }

                //削除
                tickets.remove(p);
                return;
            }
        }

        FBS.logger.error("Error! Couldn't found chunk loader at "+world.provider.getDimensionName()+"("+x+","+y+","+z+")");
    }

    @Override
    public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
        ChunkLoadManager.tickets.clear();

        //チケット全部見てなんか処理してる
        for(ForgeChunkManager.Ticket t : tickets){
            if(t.getModData().getString("type").equals("block")){
                int x = t.getModData().getInteger("x");
                int y = t.getModData().getInteger("y");
                int z = t.getModData().getInteger("z");
                Block b=world.getBlock(x,y,z);

                //チャンクローダか判定してそれぞれ処理
                if(b instanceof IChunkLoader){
                    if(((IChunkLoader)b).canLoad(world, x, y, z)){
                        setChunkLoader(world, x, y, z);
                    }
                }
            }
        }
    }

    public static HashMap<Pos, ForgeChunkManager.Ticket> getTickets(){
        return tickets;
    }

    public static World getWorld(int dId){
        for(ForgeChunkManager.Ticket t : tickets.values()){
            if(t.world.provider.dimensionId==dId){
                return t.world;
            }
        }
        return null;
    }

    public interface IChunkLoader{
        public boolean canLoad(World w, int x, int y, int z);
    }

    private static class Pos{
        int x,y,z;

        public Pos(int x, int y, int z){
            this.x=x;
            this.y=y;
            this.z=z;
        }

        @Override
        public boolean equals(Object obj){
            if(!(obj instanceof Pos)) return false;
            Pos p=(Pos)obj;
            return x==p.x && y==p.y && z==p.z;
        }
    }
}
