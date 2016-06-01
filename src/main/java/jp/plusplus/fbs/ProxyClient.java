package jp.plusplus.fbs;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import jp.plusplus.fbs.block.render.*;
import jp.plusplus.fbs.entity.*;
import jp.plusplus.fbs.entity.render.*;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.model.ModelMagicBall;
import jp.plusplus.fbs.model.ModelMagicBase;
import jp.plusplus.fbs.nei.NEILoader;
import jp.plusplus.fbs.pottery.RenderPottersWheel;
import jp.plusplus.fbs.pottery.RenderPottery;
import jp.plusplus.fbs.pottery.TileEntityPottery;
import jp.plusplus.fbs.render.RendererBook;
import jp.plusplus.fbs.spirit.render.RenderAlice;
import jp.plusplus.fbs.storage.*;
import jp.plusplus.fbs.tileentity.*;
import jp.plusplus.fbs.tileentity.render.RenderMagicCircle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.HashMap;


/**
 * Created by plusplus_F on 2015/01/31.
 */
public class ProxyClient extends ProxyServer {
    public Timer timer=new Timer(20);

    private HashMap<String, RendererLivingEntity> spiritModels=new HashMap<String, RendererLivingEntity>();

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void register() {
        super.register();

        MinecraftForgeClient.registerItemRenderer(ItemCore.bookNoDecoded, new RendererBook());
        MinecraftForgeClient.registerItemRenderer(ItemCore.bookSorcery, new RendererBook());

        RenderingRegistry.registerEntityRenderingHandler(EntityButterfly.class, new RenderButterfly());
        RenderingRegistry.registerEntityRenderingHandler(EntityTableware.class, new RenderTableware());
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicDig.class, new RenderMagicBase(new ModelMagicBase(0, 0)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicArrow.class, new RenderMagicBase(new ModelMagicBase(16, 0)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicWedge.class, new RenderMagicBase(new ModelMagicBase(0, 8)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicFireBolt.class, new RenderMagicBase(new ModelMagicBase(16, 8)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicHealingBall.class, new RenderMagicBase(new ModelMagicBall(48, 24)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicArrowFlexible.class, new RenderMagicBase(new ModelMagicBase(16, 0)));
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicAuthor.class, new RenderAuthor());
        RenderingRegistry.registerEntityRenderingHandler(EntityMagicHailstorm.class, new RenderMagicHailstorm());

        FBS.renderDirectionalId=registerRenderer(new RenderDirectional());
        FBS.renderCharmId=registerRenderer(new RenderCharm());
        FBS.renderPottersWheelId=registerRenderer(new RenderPottersWheel());

        ClientRegistry.registerTileEntity(TileEntityMagicCore.class, FBS.MODID+"-magicCoreR", new RenderMagicCircle());

        TileEntitySpecialRenderer tesr=new RenderPottery();
        FBS.renderJarId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPottery.class, tesr);

        tesr=new RenderMirror();
        FBS.renderMirrorId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMirror.class, tesr);

        tesr=new RenderAlchemyCauldron();
        FBS.renderAlchemyCauldronId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlchemyCauldron.class, tesr);

        tesr=new RenderDecorations();
        FBS.renderDecorationId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForRender.class, tesr);

        tesr=new RenderHarvestable();
        FBS.renderHerbId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHavestable.class, tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHavestableMushroom.class, tesr);

        tesr=new RenderMealCrystal();
        FBS.renderMealId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMeal.class, tesr);

        tesr=new RenderMealInlet();
        FBS.renderMealInletId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMealInlet.class, tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMealOutletSingle.class, tesr);

        tesr=new RenderMealTerminal();
        FBS.renderMealTerminalId=registerRenderer((ISimpleBlockRenderingHandler)tesr);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMealTerminal.class, tesr);

        FBS.renderBarrierId=registerRenderer(new RenderBarrier());
        FBS.renderPortalWarpId=registerRenderer(new RenderPortalWarp());

        registerSpiritModel("fbs.alice", new RenderAlice());
    }

    @Override
    public int registerRenderer(ISimpleBlockRenderingHandler renderer){
        int id=RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, renderer);
        //FMLLog.severe(renderer.toString());
        return id;
    }

    public void registerSpiritModel(String character, RendererLivingEntity renderer){
        spiritModels.put(character, renderer);
    }
    public RendererLivingEntity getSpiritModel(String character){
        return spiritModels.get(character);
    }

    @Override
    public void loadNEI(){
        if(Loader.isModLoaded("NotEnoughItems")){
            NEILoader.LoadNEI();
        }
    }
    @Override
    public EntityPlayer getEntityPlayerInstance(){
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public float getRenderPartialTicks(){
        return timer.renderPartialTicks;
    }
    @Override
    public void setRenderPartialTicks(float f){
        timer.renderPartialTicks=f;
    }
    @Override
    public void updateTimer(){
        timer.updateTimer();
    }
}
