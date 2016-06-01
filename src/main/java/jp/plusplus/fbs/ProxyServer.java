package jp.plusplus.fbs;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import jp.plusplus.fbs.storage.ChunkLoadManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;

/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ProxyServer {

    public World getClientWorld() {
        return null;
    }
    public void register() {

        Registry.RegisterOreDictionary();
        Registry.RegisterCraftingRecipes();
        Registry.RegisterChestContents();
        Registry.RegisterEntities();
        Registry.RegisterTileEntities();
        Registry.RegisterPotion();
        Registry.RegisterWorldGen();

        registerAchievement();

        ForgeChunkManager.setForcedChunkLoadingCallback(FBS.instance, ChunkLoadManager.instance());
    }
    public int registerRenderer(ISimpleBlockRenderingHandler renderer){
        return -1;
    }
    public void registerAchievement(){
        //AchievementChecker.register();
    }

    public void loadNEI(){}
    public EntityPlayer getEntityPlayerInstance(){
        return null;
    }

    // なんでクラスそのものにCLIENT限定とかつけてんの？
    public float getRenderPartialTicks(){
        return 0;
    }
    public void setRenderPartialTicks(float f){
    }
    public void updateTimer(){
    }
}
