package jp.plusplus.fbs;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.command.*;
import jp.plusplus.fbs.event.FBSEventHandler;
import jp.plusplus.fbs.event.wish.WishHandler;
import jp.plusplus.fbs.gui.GuiHandler;
import jp.plusplus.fbs.mod.ForIR3;
import jp.plusplus.fbs.render.RendererGameOverlay;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.packet.PacketHandler;
import jp.plusplus.fbs.spirit.SpiritManager;
import jp.plusplus.fbs.tab.*;
import jp.plusplus.fbs.trouble.TroubleDamage;
import jp.plusplus.fbs.trouble.TroubleDry;
import jp.plusplus.fbs.trouble.TroubleHunger;
import jp.plusplus.fbs.trouble.TroubleTiredness;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraft.item.EnumAction;
import net.minecraft.util.Timer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


/**
 * Created by plusplus_F on 2015/01/31.
 */
@Mod(modid = FBS.MODID, version = FBS.VERSION, name = FBS.NAME, dependencies = "required-after:Forge@[10.13.4.1448,);required-after:mceconomy2@[2.5.0,)")
public class FBS {
    public static final String NAME="Insanity";
    public static final String MODID = "jp-plusplus-fbs";
    public static final String VERSION = "1.2.0";

    @Mod.Instance(FBS.MODID)
    public static FBS instance;

    public static final CreativeTabs tab =new Tab("tab-"+FBS.MODID);
    public static final CreativeTabs tabBook=new TabBooks("tabBook-"+FBS.MODID);
    public static final CreativeTabs tabPottery =new TabPottery("tabPottery-"+FBS.MODID);
    public static final CreativeTabs tabAlchemy =new TabAlchemy("tabAlchemy-"+FBS.MODID);
    public static final CreativeTabs tabSpirit =new TabSpirit("tabSpirit-"+FBS.MODID);

    public static final int GUI_STAFF_ID =1;
    public static final int GUI_MAGIC_COPY_ID =2;
    public static final int GUI_ENCHANTMENT_ID=3;
    public static final int GUI_MAGIC_WARP_ID =4;
    public static final int GUI_MAGIC_CONTRACT_ID =5;
    public static final int GUI_SPIRIT_MAIN_ID =6;
    public static final int GUI_BASKET_ID =7;
    public static final int GUI_SPIRIT_CONFIG_ID =8;
    public static final int GUI_SPIRIT_LEARN_ID =9;
    public static final int GUI_SPIRIT_SKILL_ID =10;
    public static final int GUI_SHOP_AUTHOR_ID =11;
    public static final int GUI_MAGIC_TIME_TRACE_ID =12;
    public static final int GUI_MAGIC_POT_ID =13;
    public static final int GUI_WISH_ID=14;

    public static int dimensionCrackId =-10;
    public static int dimensionAutumnId=-11;

    public static Logger logger= LogManager.getLogger("Insanity");

    public static EnumAction actionDecode;
    public static EnumAction actionSpell;

    public static int renderDirectionalId;
    public static int renderCharmId;
    public static int renderMirrorId;
    public static int renderJarId;
    public static int renderPottersWheelId;
    public static int renderAlchemyCauldronId;
    public static int renderDecorationId;
    public static int renderAlchemyTableId;
    public static int renderBarrierId;
    public static int renderPortalWarpId;
    public static int renderHerbId;
    public static int renderMealId;
    public static int renderMealInletId;
    public static int renderMealTerminalId;

    public static int sanityRecoveryRatio;

    public static boolean insanityFromMobs;
    public static boolean generatesOre;
    public static boolean enableDescription;

    public static boolean enableRecipeOak;
    public static boolean enableRecipeLavender;
    public static boolean enableRecipeRedLily;

    public static boolean cooperatesAMT2;
    public static boolean cooperatesSS2;
    public static boolean cooperatesIC2;
    public static boolean cooperatesIR3;


    @SidedProxy(clientSide = "jp.plusplus.fbs.ProxyClient", serverSide = "jp.plusplus.fbs.ProxyServer")
    public static ProxyServer proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        LoadConfiguration();

        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new RendererGameOverlay());
        }

        BlockCore.Init();
        ItemCore.Init();

        Registry.RegisterBooks();
        Registry.RegisterMagics();


        actionDecode=EnumHelper.addAction(FBS.MODID+"-decode");
        actionSpell=EnumHelper.addAction(FBS.MODID+"-spell");

        //AchievementChecker.init();
        AchievementRegistry.register();
        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        proxy.register();
        proxy.loadNEI();

        FBSEventHandler epeh=new FBSEventHandler();
        MinecraftForge.EVENT_BUS.register(epeh);
        MinecraftForge.ORE_GEN_BUS.register(epeh);
        FMLCommonHandler.instance().bus().register(epeh);
        GameRegistry.registerFuelHandler(epeh);

        AlchemyRegistry.RegisterAlchemy();
        SpiritManager.register();
        Registry.RegisterMP();

        Registry.RegisterTrouble(new TroubleHunger(), 10);
        Registry.RegisterTrouble(new TroubleDamage(), 20);
        if(FBS.cooperatesSS2){
            Registry.RegisterTrouble(new TroubleDry(), 10);
            Registry.RegisterTrouble(new TroubleTiredness(), 10);
        }
        if(FBS.cooperatesIR3){
            ForIR3.setup();
        }

        WishHandler.register();

        /*
        VillagerRegistry.instance().registerVillageTradeHandler(0, new VillagerTradeHandler());
        Recipes.RegisterFilledCan();
        Recipes.RegisterBuildingItems();
        Recipes.SetUpCooperation();
        MinecraftForge.addGrassSeed(new ItemStack(ItemCore.seedCotton), 5);
        */

        //chest
        /*
        ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(ItemCore.screw, 0, 1, 2, 2));
        */

    }

    public void LoadConfiguration(){
        Configuration cfg=new Configuration(new File("./config/Insanity.cfg"));
        cfg.load();

        Registry.LoadIdFromCfg(cfg);

        sanityRecoveryRatio=cfg.getInt("SanityRecoveryRatio", "General", 100, 0, 100, "");
        enableDescription=cfg.getBoolean("EnableDescription", "General", true, "");
        insanityFromMobs=cfg.getBoolean("InsanityFromMobs", "General", true, "");
        generatesOre=cfg.getBoolean("GeneratesOre", "General", true, "");
        cooperatesAMT2=(cfg.getBoolean("CooperatesAMT2", "General", true, "") && Loader.isModLoaded("DCsAppleMilk"));
        cooperatesSS2=(cfg.getBoolean("CooperatesSS2", "General", true, "") && Loader.isModLoaded("SextiarySector"));
        cooperatesIR3=(cfg.getBoolean("CooperatesIR3", "General", true, "") && Loader.isModLoaded("jp-plusplus-ir2"));

        enableRecipeOak=cfg.getBoolean("EnableRecipeOak", "Recipe", false, "");
        enableRecipeLavender=cfg.getBoolean("EnableRecipeLavender", "Recipe", false, "");
        enableRecipeRedLily=cfg.getBoolean("EnableRecipeRedLily", "Recipe", false, "");

        cfg.save();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @EventHandler
    public void receiveIMCE(FMLInterModComms.IMCEvent event){
        //IMCEventReceiver.receive(event);
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandSanityPoint());
        event.registerServerCommand(new CommandMagicLevel());
        event.registerServerCommand(new CommandMagicExp());
        event.registerServerCommand(new CommandGetBook());
        event.registerServerCommand(new CommandGetSpirit());
        event.registerServerCommand(new CommandWish());
    }
}
