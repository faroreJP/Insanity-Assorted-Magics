package jp.plusplus.fbs;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.CoreModManager;
import jp.plusplus.fbs.api.*;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.block.BlockBlock;
import jp.plusplus.fbs.entity.*;
import jp.plusplus.fbs.api.event.PlayerDecodedBookEvent;
import jp.plusplus.fbs.exprop.FBSEntityProperties;
import jp.plusplus.fbs.exprop.SanityManager;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.item.ItemGem;
import jp.plusplus.fbs.magic.*;
import jp.plusplus.fbs.magic.enchant.*;
import jp.plusplus.fbs.magic.resonance.FlexibleResonance;
import jp.plusplus.fbs.magic.resonance.ShapedResonance;
import jp.plusplus.fbs.mod.ShopAuthor;
import jp.plusplus.fbs.mod.ShopWitch;
import jp.plusplus.fbs.potion.PotionCleverness;
import jp.plusplus.fbs.potion.PotionContract;
import jp.plusplus.fbs.potion.PotionHailstorm;
import jp.plusplus.fbs.potion.PotionMagnet;
import jp.plusplus.fbs.pottery.PotteryRegistry;
import jp.plusplus.fbs.pottery.TileEntityKiln;
import jp.plusplus.fbs.pottery.TileEntityPottersWheel;
import jp.plusplus.fbs.pottery.TileEntityPottery;
import jp.plusplus.fbs.storage.TileEntityMeal;
import jp.plusplus.fbs.storage.TileEntityMealInlet;
import jp.plusplus.fbs.storage.TileEntityMealOutletSingle;
import jp.plusplus.fbs.storage.TileEntityMealTerminal;
import jp.plusplus.fbs.tileentity.*;
import jp.plusplus.fbs.trouble.*;
import jp.plusplus.fbs.world.autumn.WorldProviderAutumn;
import jp.plusplus.fbs.world.biome.BiomeAutumn;
import jp.plusplus.fbs.world.crack.WorldProviderCrack;
import jp.plusplus.fbs.world.crack.biome.BiomeGenCrack;
import jp.plusplus.fbs.world.crack.structure.MapGenSchool;
import jp.plusplus.fbs.world.structure.MapGenStudy;
import jp.plusplus.fbs.world.structure.StructureSealedLib1;
import jp.plusplus.fbs.world.structure.StructureSealedLibStart;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import shift.mceconomy2.api.MCEconomyAPI;
import shift.sextiarysector.SextiarySector;

import java.util.*;

/**
 * Createdby pluslus_Fon 2015/06/06.
 * レシピやらなんやらを一括で管理しているクラス
 * なにか追加したかったらとりあえずここのstaticなメソッドをみるといいよ
 */
public class Registry {
    private static Registry instance=new Registry();

    private Random rand=new Random();
    private HashMap<String, BookData> books=new HashMap<String, BookData>();
    private HashMap<String, MagicData> magics=new HashMap<String, MagicData>();
    private ArrayList<IResonance> resonances=new ArrayList<IResonance>();
    private ArrayList<ChestContent> chestContents=new ArrayList<ChestContent>();
    private ArrayList<RecipePair> craftingRecipes=new ArrayList<RecipePair>();
    private ArrayList<MagicCircle> magicCircles=new ArrayList<MagicCircle>();
    private ArrayList<ItemSanity> sanityItems=new ArrayList<ItemSanity>();
    private ArrayList<ItemManaContainer> manaItems=new ArrayList<ItemManaContainer>();
    private ArrayList<MobSanity> sanityMobs=new ArrayList<MobSanity>();
    public ArrayList<PotteryRegistry.PotteryPair> potteries=new ArrayList<PotteryRegistry.PotteryPair>();
    private ArrayList<WeightedTrouble> troubles=new ArrayList<WeightedTrouble>();
    private ArrayList<String> fortuneCookies=new ArrayList<String>();
    private int troublesWeightSum;

    public static int eIdButterfly;
    public static int eIdProjectileBase;
    public static int eIdDig;
    public static int eIdArrow;
    public static int eIdWedge;
    public static int eIdFireBolt;
    public static int eIdIceSpear;
    public static int eIdCanon;
    public static int eIdHealingBall;
    public static int eIdHealingRain;
    public static int eIdTeleport;
    public static int eIdArrowResona;
    public static int eIdTableware;
    public static int eIdAuthor;
    public static int eIdDummy;
    public static int eIdHailstrom;

    public static int shopMCE2Id;
    public static int shopAuthorId;

    public static int bIdAutumn=42;
    public static BiomeGenBase biomeAutumn;
    public static int bIdCrack=43;
    public static BiomeGenBase biomeCrack;

    public static int pIdContract=80;
    public static Potion potionContract;
    public static int pIdCleverness=81;
    public static Potion potionCleverness;
    public static int pIdHailstorm=82;
    public static Potion potionHailstorm;
    public static int pIdMagnet=82;
    public static Potion potionMagnet;

    public static void RegisterOreDictionary() {
        String[] dyes = {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};

        for(int i=0;i<3;i++){
            OreDictionary.registerOre(ItemGem.NAMES[i], new ItemStack(ItemCore.gem, 1, i));
            OreDictionary.registerOre("ore"+BlockBlock.NAMES[i], new ItemStack(BlockCore.ore, 1, i));
            OreDictionary.registerOre("block"+BlockBlock.NAMES[i], new ItemStack(BlockCore.block, 1, i));
        }

        for (int i = 0; i < ItemGem.NAMES.length; i++) {
            if(i!=3) OreDictionary.registerOre(ItemGem.NAMES[i], new ItemStack(ItemCore.gem, 1, i));
            OreDictionary.registerOre("fbs.gem", new ItemStack(ItemCore.gem, 1, i));
        }
        OreDictionary.registerOre("fbs.gem", Items.diamond);
        OreDictionary.registerOre("fbs.gem", Items.emerald);

        for (int i = 0; i < 16; i++) {
            String s = ItemDye.field_150921_b[i];
            OreDictionary.registerOre("dye" + dyes[i], new ItemStack(ItemCore.charm, 1, i));
        }
        OreDictionary.registerOre("fbs.charm", new ItemStack(ItemCore.charm, 1, OreDictionary.WILDCARD_VALUE));


        RegisterItemSanity(new ItemStack(ItemCore.lavender), 1, 2);
        RegisterItemSanity(new ItemStack(ItemCore.potionSan), 3, 10);
        RegisterItemSanity(new ItemStack(Items.apple), 1, 2);
        RegisterItemSanity(new ItemStack(Items.golden_carrot), 2, 4);
        RegisterItemSanity(new ItemStack(Items.golden_apple, 1, 0), 2, 6);
        RegisterItemSanity(new ItemStack(Items.golden_apple, 1, 1), 3, 10);
        RegisterItemSanity(new ItemStack(Items.rotten_flesh), 1, -2);
        RegisterItemSanity(new ItemStack(Items.spider_eye), 1, -2);
    }
    public static void RegisterCraftingRecipes(){
        FluidContainerRegistry.registerFluidContainer(BlockCore.mana, new ItemStack(ItemCore.bucketMana), new ItemStack(Items.bucket));

        GameRegistry.addRecipe(new RecipeBladeSpice());
        GameRegistry.addRecipe(new RecipePotionBless());

        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.seedRedLily, 2), new ItemStack(ItemCore.redLily));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.seedRedLily, 2), new ItemStack(ItemCore.redLilyDirty));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.gunpowder), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.cookieFortune), new ItemStack(Items.cookie), new ItemStack(Items.paper));

        for(int i=0;i< BlockBlock.NAMES.length;i++){
            GameRegistry.addRecipe(new ItemStack(BlockCore.block, 1, i), "xxx","xxx","xxx", 'x',new ItemStack(ItemCore.gem, 1, i));
            GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.gem, 9, i), new ItemStack(BlockCore.block, 1, i));
            GameRegistry.addSmelting(new ItemStack(BlockCore.ore, 1, i),new ItemStack(ItemCore.gem, 1, i), 1.0f);
        }

        GameRegistry.addRecipe(new ItemStack(ItemCore.bookmark), "  s"," p ","p  ", 's',new ItemStack(Items.string), 'p',new ItemStack(Items.paper));
        GameRegistry.addRecipe(new ItemStack(ItemCore.monocleWood), "ii ","i i"," i ", 'i', new ItemStack(BlockCore.plank));
        GameRegistry.addRecipe(new ItemStack(ItemCore.monocle), "ii ","iei"," i ", 'i',new ItemStack(Items.iron_ingot), 'e',new ItemStack(Items.ender_pearl));
        GameRegistry.addRecipe(new ItemStack(ItemCore.monocleGold), "ii ","iei"," i ", 'i',new ItemStack(Items.gold_ingot), 'e',new ItemStack(Items.ender_eye));
        GameRegistry.addRecipe(new ItemStack(BlockCore.workbench), "ww","ww", 'w',new ItemStack(BlockCore.plank));
        GameRegistry.addRecipe(new ItemStack(BlockCore.extractingFurnace), "sss","fwb","sss", 'w',new ItemStack(BlockCore.plank), 's',new ItemStack(Blocks.cobblestone), 'f',new ItemStack(Blocks.furnace), 'b',new ItemStack(Items.bucket));
        GameRegistry.addRecipe(new ItemStack(BlockCore.fillingTable), "sss","sws","sss", 'w',new ItemStack(BlockCore.plank), 's',new ItemStack(Blocks.cobblestone));
        GameRegistry.addRecipe(new ItemStack(BlockCore.bookshelf), "www","bbb","www", 'w',new ItemStack(BlockCore.plank), 'b',new ItemStack(Items.book));
        GameRegistry.addRecipe(new ItemStack(ItemCore.net), "  w"," s ","s  ", 'w',new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 's',new ItemStack(ItemCore.stick));
        GameRegistry.addRecipe(new ShapedOreRecipe(BlockCore.bonfire, "w w"," w ","w w", 'w',"logWood"));

        GameRegistry.addRecipe(new ItemStack(BlockCore.pottersWheel),   "ccc","sws", 'c',new ItemStack(Blocks.stone_slab), 's',new ItemStack(Blocks.cobblestone), 'w',Blocks.crafting_table);
        GameRegistry.addRecipe(new ItemStack(BlockCore.kiln),           "sss","s s","fff", 's',new ItemStack(Blocks.cobblestone), 'f',Blocks.furnace);
        GameRegistry.addRecipe(new ItemStack(ItemCore.clayWet, 8),      "ccc","cwc","ccc", 'c',new ItemStack(Items.clay_ball), 'w',new ItemStack(Items.water_bucket));
        GameRegistry.addRecipe(new ItemStack(ItemCore.clayGlow, 8),     "ccc","cwc","ccc", 'c',new ItemStack(ItemCore.clayWet), 'w',new ItemStack(Items.glowstone_dust));

        GameRegistry.addRecipe(new ItemStack(BlockCore.tableAlchemist), "bbb","www","w w", 'b',Items.book, 'w', BlockCore.plank);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.alchemyCauldron), "gjg","ggg","b b", 'b',Blocks.brick_block, 'g',Items.gold_ingot, 'j', "fbs.gem"));
        GameRegistry.addRecipe(new ItemStack(ItemCore.basket), "sss","scs","sss", 's',Items.stick, 'c', Blocks.chest);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCore.schoolTable, 4), "www", "i i", 'w',"plankWood", 'i',Items.iron_ingot));

        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.stoneActive), new ItemStack(ItemCore.stoneInactive), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 4));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.stoneActiveMale), new ItemStack(ItemCore.stoneInactive), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 4), new ItemStack(ItemCore.gem, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(ItemCore.stoneActiveFemale), new ItemStack(ItemCore.stoneInactive), new ItemStack(ItemCore.alchemyIntermediateMaterial, 1, 4), new ItemStack(ItemCore.gem, 1, 0));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemCore.gem, 1, 3), " t ","tgt"," t ", 't',new ItemStack(ItemCore.alchemyMaterial, 1, 40), 'g',"fbs.gem"));
        GameRegistry.addShapedRecipe(new ItemStack(ItemCore.infinityHelm), "ggg", "g g", 'g', new ItemStack(ItemCore.gem, 1, 3));
        GameRegistry.addShapedRecipe(new ItemStack(ItemCore.infinityArmor), "g g","ggg","ggg", 'g',new ItemStack(ItemCore.gem,1,3));
        GameRegistry.addShapedRecipe(new ItemStack(ItemCore.infinityLegs), "ggg","g g","g g", 'g',new ItemStack(ItemCore.gem,1,3));
        GameRegistry.addShapedRecipe(new ItemStack(ItemCore.infinityBoots), "g g","g g", 'g',new ItemStack(ItemCore.gem,1,3));

        //救済措置
        if(FBS.enableRecipeOak) GameRegistry.addRecipe(new ItemStack(BlockCore.plank, 8), "www","wew","www", 'w',new ItemStack(Blocks.planks, 1, 0), 'e',new ItemStack(Items.ender_pearl));
        if(FBS.enableRecipeLavender) GameRegistry.addRecipe(new ItemStack(ItemCore.seedLavender, 4), " s ","sps"," s ", 's',new ItemStack(Items.wheat_seeds), 'p',new ItemStack(Items.dye, 1, 5));
        if(FBS.enableRecipeLavender) GameRegistry.addRecipe(new ItemStack(ItemCore.seedRedLily, 4), " s ", "sps", " s ", 's', new ItemStack(Items.wheat_seeds), 'p', new ItemStack(Items.dye, 1, 1));

        RegisterShapedRecipe(new ItemStack(ItemCore.monocle), 400, "ii ", "i i", " i ", 'i', new ItemStack(Items.iron_ingot));
        RegisterShapedRecipe(new ItemStack(ItemCore.monocleGold), 650, "ii ", "i i", " i ", 'i', new ItemStack(Items.gold_ingot));
        RegisterShapedRecipe(new ItemStack(ItemCore.potionOblivion), 800, "nln", "lpl", "nln", 'n', new ItemStack(Items.nether_wart), 'l', new ItemStack(ItemCore.lavender), 'p', new ItemStack(Items.potionitem));
        RegisterShapedRecipe(new ItemStack(ItemCore.potionSan), 500, "lll", "lpl", "lll", 'l', new ItemStack(ItemCore.lavender), 'p', new ItemStack(Items.potionitem));
        RegisterShapedOreRecipe(new ItemStack(BlockCore.magicCore), 250, "ccc", "c c", "ccc", 'c', "fbs.charm");
        RegisterShapedRecipe(new ItemStack(ItemCore.membership), 100, "ppp", "pip", "ppp", 'p', new ItemStack(Items.paper), 'i', new ItemStack(Items.dye, 1, 0));
        RegisterShapedRecipe(new ItemStack(BlockCore.mirror), 800, "wgw", "wmw", "wgw", 'w', new ItemStack(BlockCore.plank), 'g',new ItemStack(Blocks.glass), 'm',new ItemStack(ItemCore.membership));
        RegisterShapedOreRecipe(new ItemStack(ItemCore.bookWhite), 100, " i ", "cbc", 'i', new ItemStack(Items.dye, 1, 0), 'c', "fbs.charm", 'b', new ItemStack(ItemCore.bookBroken, 1, OreDictionary.WILDCARD_VALUE));
        RegisterShapedRecipe(new ItemStack(BlockCore.portal2), 250, "www", "wbw", "www", 'w', new ItemStack(BlockCore.plank), 'b', new ItemStack(ItemCore.butterfly));
        RegisterShapedOreRecipe(new ItemStack(BlockCore.mealCrystal), 2000, " r ", " d ", "s a", 'r', "blockRuby", 's', "blockSapphire", 'a', "blockAmethyst", 'd', new ItemStack(Blocks.diamond_block));
        RegisterShapedOreRecipe(new ItemStack(BlockCore.mealInlet), 250, "g","p", 'g',"fbs.gem", 'p',"plankWood");
        RegisterShapedOreRecipe(new ItemStack(BlockCore.mealOutletSingle), 250, "p","g", 'g',"fbs.gem", 'p',"plankWood");
        RegisterShapedOreRecipe(new ItemStack(BlockCore.mealTerminal), 500, "g","c","g", 'g',"fbs.gem", 'c',new ItemStack(Blocks.chest));

        RegisterShapedRecipe(new ItemStack(ItemCore.tableware, 2, 0), 10, "i  ","   ","   ", 'i',new ItemStack(Items.iron_ingot));
        RegisterShapedRecipe(new ItemStack(ItemCore.tableware, 2, 1), 10, " i ","   ","   ", 'i',new ItemStack(Items.iron_ingot));
        RegisterShapedRecipe(new ItemStack(ItemCore.tableware, 2, 2), 10, "  i","   ","   ", 'i',new ItemStack(Items.iron_ingot));

        RegisterShapelessRecipe(new ItemStack(Blocks.mossy_cobblestone), 10, new ItemStack(Blocks.cobblestone), new ItemStack(Items.wheat_seeds));
        RegisterShapelessRecipe(new ItemStack(Blocks.stonebrick,1,1), 10, new ItemStack(Blocks.stonebrick, 1, 0), new ItemStack(Items.wheat_seeds));
        RegisterShapelessRecipe(new ItemStack(Blocks.grass), 10, new ItemStack(Blocks.dirt, 1, 0), new ItemStack(Items.wheat_seeds));
        RegisterShapelessRecipe(new ItemStack(Blocks.mycelium), 10, new ItemStack(Blocks.dirt, 1, 0), new ItemStack(Blocks.red_mushroom));
        RegisterShapelessRecipe(new ItemStack(Blocks.web), 50, new ItemStack(Items.string));
        RegisterShapelessRecipe(new ItemStack(Blocks.ice), 25, new ItemStack(Items.water_bucket));
        RegisterShapelessRecipe(new ItemStack(ItemCore.stick), 50, new ItemStack(Items.stick));

        for(int i=0;i<16;i++){
            RegisterShapelessRecipe(new ItemStack(ItemCore.charm, 1, i), 50, new ItemStack(Items.dye, 1, i));
        }

        RegisterShapelessOreRecipe(new ItemStack(ItemCore.staffHead1), 600, "fbs.charm", "fbs.gem");
        RegisterShapelessOreRecipe(new ItemStack(ItemCore.staffHead2), 1200, "fbs.charm", "fbs.gem", "fbs.gem");
        RegisterShapelessOreRecipe(new ItemStack(ItemCore.staffHead3), 1800, "fbs.charm", "fbs.gem", "fbs.gem", "fbs.gem");
        RegisterShapelessOreRecipe(new ItemStack(ItemCore.staffHead4), 2400, "fbs.charm", "fbs.gem", "fbs.gem", "fbs.gem", "fbs.gem");
        RegisterShapelessOreRecipe(new ItemStack(ItemCore.staffHead5), 3000, "fbs.charm", "fbs.gem", "fbs.gem", "fbs.gem", "fbs.gem", "fbs.gem");

        RegisterShapedRecipe(new ItemStack(ItemCore.staff1_1), 100, "  h", " s ", "s  ", 'h', new ItemStack(ItemCore.staffHead1), 's', new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff1_2), 100, "  h"," s ","s  ", 'h',new ItemStack(ItemCore.staffHead2), 's',new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff1_3), 100, "  h", " s ", "s  ", 'h', new ItemStack(ItemCore.staffHead3), 's', new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff1_4), 100, "  h"," s ","s  ", 'h',new ItemStack(ItemCore.staffHead4), 's',new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff1_5), 100, "  h", " s ", "s  ", 'h', new ItemStack(ItemCore.staffHead5), 's', new ItemStack(ItemCore.stick));

        RegisterShapedRecipe(new ItemStack(ItemCore.staff2_1), 200, " h ", " sh", "s  ", 'h', new ItemStack(ItemCore.staffHead1), 's', new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff2_2), 200, " h ", " sh", "s  ", 'h', new ItemStack(ItemCore.staffHead2), 's', new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff2_3), 200, " h ", " sh", "s  ", 'h', new ItemStack(ItemCore.staffHead3), 's', new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff2_4), 200, " h ", " sh", "s  ", 'h', new ItemStack(ItemCore.staffHead4), 's', new ItemStack(ItemCore.stick));
        RegisterShapedRecipe(new ItemStack(ItemCore.staff2_5), 200, " h ", " sh", "s  ", 'h', new ItemStack(ItemCore.staffHead5), 's', new ItemStack(ItemCore.stick));

        //------------------------------------マナ抽出----------------------------------------------

        RegisterManaContainer(new ItemStack(Items.ender_pearl), 75);
        RegisterManaContainer(new ItemStack(Items.ender_eye), 125);
        RegisterManaContainer(new ItemStack(Items.dye, 1, 4), 50);
        RegisterManaContainer(new ItemStack(Items.nether_star), 2000);
        RegisterManaContainer(new ItemStack(ItemCore.stick), 50);
        RegisterManaContainer(new ItemStack(ItemCore.charm, 1, 32767), 50);
        RegisterManaContainer(new ItemStack(BlockCore.magicCore), 100);
        RegisterManaContainer(new ItemStack(ItemCore.butterfly), 100);
        RegisterManaContainer(new ItemStack(ItemCore.instantMana), 500);
        RegisterManaContainer(new ItemStack(ItemCore.redLilyDirty), 25);

        if(Loader.isModLoaded("Thaumcraft")){
            RegisterManaContainer("Thaumcraft:ItemResource", 11, 100);
            RegisterManaContainer("Thaumcraft:ItemResource", 12, 100);
            RegisterManaContainer("Thaumcraft:blockCustomPlant", 5, 50);
        }

        //
        PotteryRegistry.register();

        //
        RegisterFortuneCookieMessage("item");
        RegisterFortuneCookieMessage("message");
        RegisterFortuneCookieMessage("trivia");
    }
    public static void RegisterBooks(){
        RegisterBook("fbs.voini",        1, false, 0.7f,     75,      1, 4, 10);
        RegisterBook("fbs.nether",       3, false, 0.85f,   81,      1, 3, 10);
        RegisterBook("fbs.zombiePig",    5, false, 0.75f,   88,     1, 8, 10);
        RegisterBook("fbs.hero",         8, false, 0.7f,     96,     2, 6, 10);
        RegisterBook("fbs.necro",        10, false, 0.65f,   110,     2, 4, 10);
        RegisterBook("fbs.creeper",      13, false, 0.6f,    130,     3, 3, 10);
        RegisterBook("fbs.sera",         15, false, 0.6f,    145,     2, 6, 10);
        RegisterBook("fbs.rlyeh",        20, false, 0.5f,    180,     2, 8, 10);
        RegisterBook("fbs.hastur",       23, false, 0.5f,    210,    2, 10, 10);
        RegisterBook("fbs.ponape",       25, false, 0.5f,    230,    3, 8, 10);
        RegisterBook("fbs.blaze",        28, false, 0.58f,   250,     2, 10, 10);
        RegisterBook("fbs.dragon",       30, false, 0.55f,   280,   3, 10, 10);
        RegisterBook("fbs.cassandra",   30, false, 0.55f,   280,   3, 10, 10);
        RegisterBook("fbs.ender",        32, false, 0.60f,   300,   3, 8, 10);
        RegisterBook("fbs.unauss",       35, false, 0.48f,   360,   4, 10, 10);
        RegisterBook("fbs.villager",     38, false, 0.52f,   390,   5, 10, 10);
        RegisterBook("fbs.eibon",        40, false, 0.4f,    430,  1, 100, 10);
    }
    public static void RegisterMagics() {
        FBSRecipeAPI.AddMagic("fbs.touch", 3, 0.6f, 60, 2, 6, 10, "fbs.touch", 10, 5, 16, 64, MagicTouch.class);
        FBSRecipeAPI.AddMagic("fbs.arrow", 3, 0.6f, 60, 2, 6, 10, "fbs.projectile", 10, 5, 16, 64, MagicArrow.class);
        FBSRecipeAPI.AddMagic("fbs.cleverness", 3, 0.6f, 60, 2, 6, 10, "fbs.self", 3*20, 5, 8, 32, MagicCleverness.class);

        FBSRecipeAPI.AddMagic("fbs.digTouch", 5, 0.5f, 70, 2, 6, 10, "fbs.touch", 20, 8, 16, 64, MagicDigTouch.class);
        FBSRecipeAPI.AddMagic("fbs.dig", 5, 0.5f, 70, 2, 6, 10, "fbs.projectile", 20, 8, 16, 64, MagicDig.class);
        FBSRecipeAPI.AddMagic("fbs.wedge", 5, 0.5f, 70, 2, 6, 10, "fbs.projectile", 20, 8f, 10, 32, MagicWedge.class);
        FBSRecipeAPI.AddMagic("fbs.healingBall", 5, 0.35f, 75, 2, 6, 10, "fbs.utility", 2*20, 20f, 8, 16, MagicHealingBall.class);
        FBSRecipeAPI.AddMagic("fbs.damageBoost", 5, 0.35f, 75, 2, 6, 10, "fbs.self", 5*20, 8f, 15, 18, MagicDamageBoost.class);
        FBSRecipeAPI.AddMagic("fbs.poison", 5, 0.38f, 70, 2, 6, 10, "fbs.touch", 20*2, 5, 10, 32, MagicPoison.class);

        FBSRecipeAPI.AddMagic("fbs.return", 8, 0.45f, 80, 2, 6, 10, "fbs.self", 20 * 8, 25, 6, 18, MagicReturn.class);
        FBSRecipeAPI.AddMagic("fbs.regeneration", 8, 0.45f, 80, 2, 6, 10, "fbs.self", 20 * 5, 25, 6, 18, MagicRegeneration.class);
        FBSRecipeAPI.AddMagic("fbs.vortex", 8, 0.45f, 80, 2, 6, 10, "fbs.range", 20, 10, 8, 32, MagicVortex.class);

        FBSRecipeAPI.AddMagic("fbs.fireBolt", 10, 0.45f, 100, 2, 6, 10, "fbs.projectile", 20, 10, 16, 64, MagicFireBolt.class);
        FBSRecipeAPI.AddMagic("fbs.butterfly", 10, 0.38f, 100, 2, 4, 10, "fbs.self", 3*20, 15, 16, 64, MagicButterfly.class);
        FBSRecipeAPI.AddMagic("fbs.contract", 10, 0.40f, 120, 2, 6, 10, "fbs.utility", 6 * 20, 20, 1, 4, MagicContract.class);
        RegisterMagicCircle("fbs.contract", "fffff", "f1 4f", "f   f", "fb 2f", "fffff");

        FBSRecipeAPI.AddMagic("fbs.jump", 13, 0.45f, 115, 2, 4, 10, "fbs.self", 20, 20, 12, 32, MagicJump.class);
        FBSRecipeAPI.AddMagic("fbs.barrier", 13, 0.35f, 120, 2, 4, 10, "fbs.utility", 4*20, 30, 8, 16, MagicBarrier.class);
        RegisterMagicCircle("fbs.barrier","555","5 5","555");

        FBSRecipeAPI.AddMagic("fbs.speed", 15, 0.35f, 120, 3, 10, 10, "fbs.self", 5 * 20, 25, 6, 18, MagicSpeed.class);
        FBSRecipeAPI.AddMagic("fbs.contractEffect", 15, 0.35f, 120, 3, 10, 10, "fbs.self", 5 * 20, 25, 6, 18, MagicContractEffect.class);

        FBSRecipeAPI.AddMagic("fbs.hailstorm", 18, 0.35f, 145, 2, 8, 10, "fbs.self", 6 * 20, 35, 4, 16, MagicHailstorm.class);

        FBSRecipeAPI.AddMagic("fbs.dagon", 20, 0.35f, 150, 2, 6, 10, "fbs.self", 5 * 20, 30, 6, 18, MagicDagon.class);
        FBSRecipeAPI.AddMagic("fbs.fireArmor", 20, 0.35f, 150, 3, 6, 10, "fbs.self", 5 * 20, 30, 6, 18, MagicFireArmor.class);
        FBSRecipeAPI.AddMagic("fbs.warp", 20, 0.40f, 160, 2, 6, 10, "fbs.utility", 3*20, 35, 8, 32, MagicWarp.class);
        RegisterMagicCircle("fbs.warp", "01234", "5   6", "7   8", "9   a", "bcdef");

        FBSRecipeAPI.AddMagic("fbs.timeAcc", 30, 0.35f, 240, 1, 50, 10, "fbs.utility", 3 * 20, 50, 4, 12, MagicTimeAcc.class);
        FBSRecipeAPI.AddMagic("fbs.timeTrace", 30, 0.35f, 240, 1, 50, 10, "fbs.utility", 3 * 20, 50, 4, 12, MagicTimeTrace.class);
        RegisterMagicCircle("fbs.timeTrace", "444", "4 4", "444");
        FBSRecipeAPI.AddMagic("fbs.copy", 30, 0.2f, 250, 4, 10, 10, "fbs.utility", 3*20, 60, 6, 18, MagicCopy.class);
        RegisterMagicCircle("fbs.copy", "01234", "5edc6", "7b 58", "9421a", "bcdef");

        FBSRecipeAPI.AddMagic("fbs.evKing", 31, 0.2f, 245, 4, 10, 10, "fbs.utility", 6 * 20, 50, 6, 18, MagicEvolutionKing.class);

        FBSRecipeAPI.AddMagic("fbs.invisible", 35, 0.35f, 270, 4, 10, 10, "fbs.self", 6 * 20, 55, 4, 16, MagicInvisible.class);

        FBSRecipeAPI.AddMagic("fbs.contemporary", 40, 0.35f, 320, 4, 10, 10, "fbs.self", 6*20, 58, 2, 8, MagicContemporary.class);
        FBSRecipeAPI.AddMagic("fbs.summonVillager", 40, 0.35f, 320, 4, 10, 10, "fbs.self", 8 * 20, 60, 2, 8, MagicSummonVillager.class);
        RegisterMagicCircle("fbs.summonVillager", "a2a", "2 2", "a2a");
        FBSRecipeAPI.AddMagic("fbs.harvest", 40, 0.2f, 330, 4, 10, 10, "fbs.self", 3 * 20, 65, 2, 4, MagicHarvest.class);
        RegisterMagicCircle("fbs.harvest", "bbbbb", "bf fb", "b   b", "bf fb", "bbbbb");
        FBSRecipeAPI.AddMagic("fbs.wish", 40, 0.1f, 450, 3, 8, 5, "fbs.utility", 15 * 20, 200, 1, 3, MagicWish.class);

        //---------------------------レゾナンス-----------------------------------
        RegisterBook("fbs.failure", 1, true, 1.0f, 1, 1, 6, 0);
        RegisterMagic("fbs.failure", "fbs.self", 20, 1, 1, 1, MagicFailure.class);

        RegisterBook("fbs.healingSelf", 10, true, 1.0f, 1, 1, 6, 0);
        RegisterMagic("fbs.healingSelf", "fbs.self", 20*2, 150, 1, 1, MagicHealingSelf.class);
        RegisterResonance("fbs.healingSelf", "fbs.healingBall", "fbs.regeneration");

        RegisterBook("fbs.hurricane", 15, true, 1.0f, 1, 1, 6, 0);
        RegisterMagic("fbs.hurricane", "fbs.range", 20, 200, 1, 1, MagicLoveHurricane.class);
        RegisterResonance("fbs.hurricane", "fbs.vortex", "fbs.jump");

        RegisterBook("resonance.fbs.touch", 10, true, 1.0f, 1, 1, 6, 0);
        RegisterMagic("resonance.fbs.touch", "fbs.touch", 20*2, 80, 1, 1, MagicTouchFlexible.class);
        RegisterResonance(new FlexibleResonance("fbs.touch"));

        RegisterBook("resonance.fbs.arrow", 12, true, 1.0f, 1, 1, 6, 0);
        RegisterMagic("resonance.fbs.arrow", "fbs.projectile", 20*2, 80, 1, 1, MagicArrowFlexible.class);
        RegisterResonance(new FlexibleResonance("fbs.arrow"));

        RegisterBook("resonance.fbs.vortex", 15, true, 1.0f, 1, 1, 6, 0);
        RegisterMagic("resonance.fbs.vortex", "fbs.range", 20*2, 80, 1, 1, MagicVortexFlexible.class);
        RegisterResonance(new FlexibleResonance("fbs.vortex"));
    }
    public static void RegisterEntities(){
        EntityRegistry.registerModEntity(EntityButterfly.class, FBS.MODID+"-butterfly", eIdButterfly, FBS.instance, 256, 1, false);
        EntityRegistry.registerModEntity(EntityTableware.class, FBS.MODID+"-tableware", eIdTableware, FBS.instance, 256, 1, true);
        EntityRegistry.registerModEntity(EntityMagicAuthor.class, FBS.MODID+"-author", eIdAuthor, FBS.instance, 256, 1, false);
        EntityRegistry.registerModEntity(EntityLivingDummy.class, FBS.MODID+"-dummy", eIdDummy, FBS.instance, 256, 1, false);

        EntityRegistry.registerModEntity(EntityMagicProjectileBase.class, FBS.MODID+"-base", eIdProjectileBase, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicDig.class, FBS.MODID+"-dig", eIdDig, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicArrow.class, FBS.MODID + "-arrow", eIdArrow, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicWedge.class, FBS.MODID + "-wedge", eIdWedge, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicFireBolt.class, FBS.MODID + "-fireBolt", eIdFireBolt, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicHealingBall.class, FBS.MODID + "-healingBall", eIdHealingBall, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicArrowFlexible.class, FBS.MODID + "-arrowFlexible", eIdArrowResona, FBS.instance, 128, 5, true);
        EntityRegistry.registerModEntity(EntityMagicHailstorm.class, FBS.MODID + "-hailstorm", eIdHailstrom, FBS.instance, 128, 5, true);

        RegisterMobSanity(EntityZombie.class, 1, -2);
        RegisterMobSanity(EntityPigZombie.class, 1, -2);
        RegisterMobSanity(EntityEnderman.class, 1, -6);
        RegisterMobSanity(EntityWither.class, 3, -8);
    }
    public static void RegisterTileEntities(){
        GameRegistry.registerTileEntity(TileEntityExtractingFurnace.class, FBS.MODID + "-extractingFurnace");
        GameRegistry.registerTileEntity(TileEntityFillingTable.class, FBS.MODID + "-fillingTable");
        GameRegistry.registerTileEntity(TileEntityFBSWorkbench.class, FBS.MODID + "-workbench");
        GameRegistry.registerTileEntity(TileEntityMagicCore.class, FBS.MODID + "-magicCore");
        GameRegistry.registerTileEntity(TileEntityPottery.class, FBS.MODID+"-pottery");
        GameRegistry.registerTileEntity(TileEntityPottersWheel.class, FBS.MODID+"-pottersWheel");
        GameRegistry.registerTileEntity(TileEntityKiln.class, FBS.MODID+"-kiln");
        GameRegistry.registerTileEntity(TileEntityAlchemyCauldron.class, FBS.MODID+"-alchemyCauldron");
        GameRegistry.registerTileEntity(TileEntityForRender.class, FBS.MODID+"-forRender");
        GameRegistry.registerTileEntity(TileEntityPortalWarp.class, FBS.MODID+"-portal1");
        GameRegistry.registerTileEntity(TileEntityHavestable.class, FBS.MODID+"-harvestable1");
        GameRegistry.registerTileEntity(TileEntityHavestableMushroom.class, FBS.MODID+"-harvestable2");
        GameRegistry.registerTileEntity(TileEntityHavestableGrass.class, FBS.MODID+"-harvestable3");
        GameRegistry.registerTileEntity(TileEntityMirror.class, FBS.MODID+"-mirror");
        GameRegistry.registerTileEntity(TileEntityMeal.class, FBS.MODID+"-meal");
        GameRegistry.registerTileEntity(TileEntityMealInlet.class, FBS.MODID+"-mealInlet");
        GameRegistry.registerTileEntity(TileEntityMealOutletSingle.class, FBS.MODID+"-mealOutletSingle");
        GameRegistry.registerTileEntity(TileEntityMealTerminal.class, FBS.MODID+"-mealTerminal");
    }
    public static void RegisterPotion(){
        potionContract=new PotionContract(pIdContract);
        potionCleverness=new PotionCleverness(pIdCleverness);
        potionHailstorm=new PotionHailstorm(pIdHailstorm);
        potionMagnet=new PotionMagnet(pIdMagnet);
    }
    public static void RegisterChestContents(){
        int recipeBookNum=11;

        //------------------- 封印された図書館 --------------------------------
        RegisterChestContent(0, new ItemStack(ItemCore.lavender, 5), 10);
        RegisterChestContent(0, new ItemStack(ItemCore.seedLavender, 5), 10);
        RegisterChestContent(0, new ItemStack(ItemCore.enchantScroll, 1), 10);
        RegisterChestContent(0, new ItemStack(ItemCore.potionBless, 3), 10);
        RegisterChestContent(0, new ItemStack(Items.dye, 3, 0), 10);
        RegisterChestContent(0, new ItemStack(Items.leather, 3), 8);
        RegisterChestContent(0, new ItemStack(ItemCore.redLily, 5), 5);
        RegisterChestContent(0, new ItemStack(ItemCore.seedRedLily, 5), 5);
        for(int i=0;i<recipeBookNum;i++) RegisterChestContent(0, new ItemStack(ItemCore.alchemyRecipe, 1, i), 5);
        RegisterChestContent(0, new ItemStack(Items.iron_ingot, 3), 5);
        RegisterChestContent(0, new ItemStack(Items.gold_ingot, 3), 3);
        RegisterChestContent(0, new ItemStack(Items.ghast_tear, 2), 3);
        RegisterChestContent(0, new ItemStack(ItemCore.cloak), 2);
        RegisterChestContent(0, new ItemStack(ItemCore.luckyDagger), 2);
        //RegisterChestContent(0, new ItemStack(Items.enchanted_book), 3);

        //------------------- 魔術師の収穫 --------------------------------
        RegisterChestContent(1, new ItemStack(ItemCore.mpCoin, 1, 500), 30);
        RegisterChestContent(1, new ItemStack(Items.iron_ingot), 10);
        RegisterChestContent(1, new ItemStack(Items.gold_nugget), 10);
        RegisterChestContent(1, new ItemStack(Items.gold_ingot), 10);
        RegisterChestContent(1, new ItemStack(Items.golden_carrot), 10);
        RegisterChestContent(1, new ItemStack(Items.golden_apple, 1, 0), 8);
        RegisterChestContent(1, new ItemStack(Items.iron_sword), 5);
        RegisterChestContent(1, new ItemStack(Items.iron_helmet), 5);
        RegisterChestContent(1, new ItemStack(Items.iron_chestplate), 5);
        RegisterChestContent(1, new ItemStack(Items.iron_leggings), 5);
        RegisterChestContent(1, new ItemStack(Items.iron_boots), 5);
        RegisterChestContent(1, new ItemStack(Items.iron_horse_armor), 5);
        RegisterChestContent(1, new ItemStack(Items.diamond), 5);
        RegisterChestContent(1, new ItemStack(Items.emerald), 5);
        RegisterChestContent(1, new ItemStack(ItemCore.mpCoin, 1, 2000), 5);
        RegisterChestContent(1, new ItemStack(ItemCore.gem, 1, 0), 5);
        RegisterChestContent(1, new ItemStack(ItemCore.gem, 1, 1), 5);
        RegisterChestContent(1, new ItemStack(ItemCore.gem, 1, 2), 5);
        RegisterChestContent(1, new ItemStack(Items.golden_apple, 1, 1), 3);
        RegisterChestContent(1, new ItemStack(Items.diamond_sword), 3);
        RegisterChestContent(1, new ItemStack(Items.diamond_helmet), 3);
        RegisterChestContent(1, new ItemStack(Items.diamond_chestplate), 3);
        RegisterChestContent(1, new ItemStack(Items.diamond_leggings), 3);
        RegisterChestContent(1, new ItemStack(Items.diamond_boots), 3);
        RegisterChestContent(1, new ItemStack(Items.diamond_horse_armor), 3);
        RegisterChestContent(1, new ItemStack(ItemCore.luckyDagger), 2);
        RegisterChestContent(1, new ItemStack(Items.nether_star), 1);

        //------------------- 学校のチェスト --------------------------------
        RegisterChestContent(2, new ItemStack(ItemCore.lavender, 5), 10);
        RegisterChestContent(2, new ItemStack(ItemCore.seedLavender, 5), 10);
        RegisterChestContent(2, new ItemStack(ItemCore.potionBless, 3), 10);
        RegisterChestContent(2, new ItemStack(ItemCore.redLily, 5), 10);
        RegisterChestContent(2, new ItemStack(ItemCore.seedRedLily, 5), 10);
        RegisterChestContent(2, new ItemStack(Items.book, 1), 10);
        for(int i=0;i<recipeBookNum;i++) RegisterChestContent(2, new ItemStack(ItemCore.alchemyRecipe, 1, i), 8);
        RegisterChestContent(2, new ItemStack(ItemCore.stoneInactive), 8);
        RegisterChestContent(2, new ItemStack(ItemCore.stoneInactive), 5);
        RegisterChestContent(2, new ItemStack(ItemCore.gem, 1, 0), 5);
        RegisterChestContent(2, new ItemStack(ItemCore.gem, 1, 1), 5);
        RegisterChestContent(2, new ItemStack(ItemCore.gem, 1, 2), 5);
        RegisterChestContent(2, new ItemStack(Items.diamond), 5);
        RegisterChestContent(2, new ItemStack(ItemCore.cloak), 2);
        RegisterChestContent(2, new ItemStack(ItemCore.luckyDagger), 2);

        for(int i=0;i<recipeBookNum;i++){
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
            ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
            ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, new WeightedRandomChestContent(new ItemStack(ItemCore.alchemyRecipe, 1, i), 1, 1, 5));
        }
    }
    public static void RegisterWorldGen(){
        biomeAutumn=new BiomeAutumn(bIdAutumn);
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(biomeAutumn, 13));
        biomeCrack=new BiomeGenCrack(bIdCrack);
        //BiomeManager.addBiome(BiomeManager.BiomeType, new BiomeManager.BiomeEntry(biomeCrack, 10));

        MapGenStructureIO.registerStructure(StructureSealedLibStart.class, FBS.MODID + "-SealedLibStart");
        MapGenStructureIO.func_143031_a(StructureSealedLib1.class, FBS.MODID+"-SealedLib1");

        MapGenStructureIO.registerStructure(MapGenStudy.Start.class, FBS.MODID+"-StudyStart");
        MapGenStructureIO.func_143031_a(MapGenStudy.Structure.class, FBS.MODID + "-Study1");

        MapGenStructureIO.registerStructure(MapGenSchool.Start.class, FBS.MODID+"-SchoolStart");
        MapGenStructureIO.func_143031_a(MapGenSchool.Hall1.class, FBS.MODID + "-School-Hall1");;
        MapGenStructureIO.func_143031_a(MapGenSchool.Hall2.class, FBS.MODID + "-School-Hall2");
        MapGenStructureIO.func_143031_a(MapGenSchool.Entrance.class, FBS.MODID + "-School-Entrance");
        MapGenStructureIO.func_143031_a(MapGenSchool.Stairway.class, FBS.MODID + "-School-Stairway");
        MapGenStructureIO.func_143031_a(MapGenSchool.RoomBase.class, FBS.MODID + "-School-RoomBase");
        MapGenStructureIO.func_143031_a(MapGenSchool.RoomStudy.class, FBS.MODID + "-School-RoomStudy");
        MapGenStructureIO.func_143031_a(MapGenSchool.RoomClassroom.class, FBS.MODID + "-School-RoomClassroom");
        MapGenStructureIO.func_143031_a(MapGenSchool.RoomZombie.class, FBS.MODID + "-School-RoomZombie");

        DimensionManager.registerProviderType(FBS.dimensionCrackId, WorldProviderCrack.class, false);
        DimensionManager.registerDimension(FBS.dimensionCrackId, FBS.dimensionCrackId);

        DimensionManager.registerProviderType(FBS.dimensionAutumnId, WorldProviderAutumn.class, false);
        DimensionManager.registerDimension(FBS.dimensionAutumnId, FBS.dimensionAutumnId);


        /*
        BiomeManager.addBiome(BiomeManager.BiomeType.DESERT, new BiomeManager.BiomeEntry(biomeAutumn, 30));
        BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(biomeAutumn, 30));
        BiomeManager.addBiome(BiomeManager.BiomeType.ICY, new BiomeManager.BiomeEntry(biomeAutumn, 30));
        */
    }
    public static void RegisterMP(){
        //Registry.RegisterMP((IPottery) BlockCore.pot);
        //Registry.RegisterMP((IPottery) BlockCore.jar);

        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.alchemyPotion, 1, 0), 100);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.alchemyPotion, 1, 1), 150);

        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.pot, 1, OreDictionary.WILDCARD_VALUE), -1);

        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.workbench), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.extractingFurnace), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.fillingTable), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.bookshelf), 2);
        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.plank), 2);
        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.magicCore), 100);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.instantMana), 30);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.membership), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.butterfly), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.enchantScroll), -1);

        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.gem, 1, 32767), 800);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.charm, 1, 32767), 10);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.monocleWood), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.monocle), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.monocleGold), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bucketMana), 20);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff1_1), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff1_2), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff1_3), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff1_4), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff1_5), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff2_1), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff2_2), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff2_3), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff2_4), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.staff2_5), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.stick), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.lavender), 2);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.seedLavender), 1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bookOld, 1, 32767), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bookNoDecoded, 1, 32767), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.bookSorcery, 1, 32767), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.potionSan), 200);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.potionOblivion), 350);

        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.alchemyCauldron), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(BlockCore.tableAlchemist), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.mushroomUnknown), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.herbUnknown), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.grassUnknown), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.seedsUnknown), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.flowerUnknown), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.fruitsUnknown), 0);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.alchemyMaterial, 1, OreDictionary.WILDCARD_VALUE), -1);
        MCEconomyAPI.addPurchaseItem(new ItemStack(ItemCore.alchemyRecipe, 1, OreDictionary.WILDCARD_VALUE), -1);

        shopMCE2Id=MCEconomyAPI.registerShop(new ShopWitch());
        shopAuthorId=MCEconomyAPI.registerShop(new ShopAuthor());
    }

    public static void LoadIdFromCfg(Configuration cfg) {
        eIdButterfly = cfg.get("Entity", "IdButterfly", 15).getInt();
        eIdProjectileBase = cfg.get("Entity", "IdProjBase", 16).getInt();
        eIdDig = cfg.get("Entity", "IdDig", 17).getInt();
        eIdArrow = cfg.get("Entity", "IdArrow", 18).getInt();
        eIdWedge = cfg.get("Entity", "IdWedge", 19).getInt();
        eIdFireBolt = cfg.get("Entity", "IdFireBolt", 20).getInt();
        eIdHealingBall = cfg.get("Entity", "IdHealingBall", 21).getInt();
        eIdArrowResona = cfg.get("Entity", "IdArrowResona", 22).getInt();
        eIdTableware = cfg.get("Entity", "IdTableware", 23).getInt();
        eIdAuthor = cfg.get("Entity", "IdAuthor", 24).getInt();
        eIdDummy = cfg.get("Entity", "IdDummy", 25).getInt();
        eIdHailstrom = cfg.get("Entity", "IdHailstorm", 26).getInt();

        ItemCore.eIdSanity = cfg.get("Enchantment", "IdSanityProtect", 65).getInt();
        ItemCore.eIdCleverness = cfg.get("Enchantment", "IdSanityCleverness", 66).getInt();
        ItemCore.eIdWealth = cfg.get("Enchantment", "IdSanityWealth", 67).getInt();

        bIdAutumn = cfg.getInt("BiomeIdAutumn", "WorldGen", Registry.bIdAutumn, 0, 256, "");
        bIdCrack = cfg.getInt("BiomeIdCrack", "WorldGen", Registry.bIdCrack, 0, 256, "");

        FBS.dimensionAutumnId=cfg.getInt("DimensionIdAutumn", "WorldGen", FBS.dimensionAutumnId, -1024, 1023, "");
        FBS.dimensionCrackId=cfg.getInt("DimensionIdCrack", "WorldGen", FBS.dimensionCrackId, -1024, 1023, "");

        boolean flag = false;
        for (String s : CoreModManager.getLoadedCoremods()) {
            if (s.equalsIgnoreCase("PotionExtension-1.7.10-1.1.0.jar")) {
                flag = true;
                break;
            }
        }
        pIdContract = cfg.get("Potion", "Contract", flag ? 80 : 25).getInt();
        pIdCleverness = cfg.get("Potion", "Cleverness", flag ? 81 : 26).getInt();
        pIdHailstorm = cfg.get("Potion", "Hailstorm", flag ? 82 : 27).getInt();
        pIdMagnet = cfg.get("Potion", "Magnet", flag ? 83 : 28).getInt();
    }

    public static ArrayList<RecipePair> getCraftingRecipes(){
        return instance.craftingRecipes;
    }

    /*
    ####################################################
                       Book
    ####################################################
    */

    /**
     * 書物を登録する。
     * @param title
     * @param lv
     * @param isMagic 魔導書であるかどうか。trueの場合MagicDataも登録必須となる
     * @param prob 適正時の解読確率
     * @param exp 解読成功時の魔術経験値
     * @param sanTrial 解読失敗時正気度喪失のXdYのX
     * @param sanMax 解読失敗時正気度喪失のXdYのY
     * @param weight GetRandomBookでの出現の重み
     */
    public static void RegisterBook(String title, int lv, boolean isMagic, float prob, double exp, int sanTrial, int sanMax, int weight){
        BookData bd=new BookData();
        bd.title=title;
        bd.lv=lv;
        bd.isMagic=isMagic;
        bd.scProb=prob;
        bd.exp=exp;
        bd.sanTrial=sanTrial;
        bd.sanMax=sanMax;
        bd.weight=weight;

        instance.books.put(title, bd);
    }

    private static BookData cashBook;
    public static BookData GetBook(String title){
        if(cashBook!=null && cashBook.title.equals(title)) return cashBook;
        cashBook=instance.books.get(title);
        return cashBook;
    }
    public static BookData GetBookDataFromItemStack(ItemStack itemStack){
        if(!itemStack.hasTagCompound()) return null;
        NBTTagCompound nbt=itemStack.getTagCompound();
        return Registry.GetBook(nbt.getString("title"));
    }
    public static String GetLocalizedBookTitle(String title){
        return StatCollector.translateToLocal("book." + title + ".title");
    }
    public static String GetUnlocalizedBookTitleFromItemStack(ItemStack itemStack){
        if(!itemStack.hasTagCompound()) return null;
        NBTTagCompound nbt=itemStack.getTagCompound();
        return nbt.getString("title");
    }

    /**
     * 本の解読を試みる
     * @param title
     * @param player
     * @param sim
     * @return
     */
    public static boolean TryDecodingBook(String title, EntityPlayer player, boolean sim){
        if(!instance.books.containsKey(title)) return false;

        BookData bd=GetBook(title);
        FBSEntityProperties prop=FBSEntityProperties.get(player);
        if(bd==null || prop==null) return false;
        int mLv=FBSEntityPropertiesAPI.GetMagicLevel(player);

        float prob=GetDecodingProbability(mLv, bd.lv, bd.scProb);
        if(instance.rand.nextFloat()<prob || player.capabilities.isCreativeMode){
            //success
            if(sim){
                PlayerDecodedBookEvent ev=new PlayerDecodedBookEvent(player, bd, true);
                boolean hc=MinecraftForge.EVENT_BUS.post(ev);
                if(!hc){
                    String tmp=String.format(StatCollector.translateToLocal("info.fbs.book.success"), bd.getLocalizedTitle());
                    tmp=tmp+String.format("(EXP+%.2f)", bd.exp);
                    player.addChatComponentMessage(new ChatComponentText(tmp));

                    prop.addDecodedBook(bd.title);
                    SanityManager.addExp(player, bd.exp, true);
                }
            }
            return true;
        }
        else{
            //failure
            if(sim){
                PlayerDecodedBookEvent ev=new PlayerDecodedBookEvent(player, bd, false);
                boolean hc=MinecraftForge.EVENT_BUS.post(ev);
                if(!hc){
                    double e=0;
                    if(prob>0) e=bd.exp*0.2;

                    String tmp=String.format(StatCollector.translateToLocal("info.fbs.book.failure"), bd.getLocalizedTitle());
                    tmp=tmp+String.format("(EXP+%.2f)", e);
                    player.addChatComponentMessage(new ChatComponentText(tmp));

                    SanityManager.addExp(player, e, true);
                    SanityManager.loseSanity(player, bd.sanTrial, bd.sanMax, true);

                    //いたずらの発生確率(%):(書物のレベル)*1.5+(書物とプレイヤーのレベル差)*10
                    //魔導書の場合1.5倍
                    //最大80%
                    if(bd.lv>=5){
                        int sub=bd.lv-mLv;
                        float prob2=1.5f*bd.lv+(sub>0?sub*0.05f:0);
                        if(bd.isMagic) prob2*=1.5f;
                        if(prob2>0.8f) prob2=0.8f;
                        if(instance.rand.nextFloat()<prob2){
                            randomTrouble(player.worldObj, player, bd);
                        }
                    }
                }
            }
            return false;
        }
    }
    public static float GetDecodingProbability(int playerLv, int bookLv, float baseProb){
        float probExt;

        //書物より10レベル以上低ければ ... 解読率0%
        //書物よりレベルが低ければ ... 差1レベルにつき解読率10%マイナス補正
        //書物よりレベルが高ければ ... 差1レベルにつき解読率10%プラス補正
        //書物より10レベル以上高ければ ... 解読率200%

        int d=bookLv-playerLv;

        if(d>=10) probExt=0.0f;
        else if(d>0) probExt=1.0f-0.1f*d;
        else if(d>-10) probExt=1.0f-0.1f*d;
        else probExt=2.0f;

        return probExt*baseProb;
    }
    public static ItemStack GetRandomBook(int lv){
        //Generate Available Book List
        int weightSum=0;
        LinkedList<BookData> available=new LinkedList<BookData>();
        Iterator<Map.Entry<String,BookData>> itAv=instance.books.entrySet().iterator();
        while(itAv.hasNext()){
            BookData p=itAv.next().getValue();
            if(p.weight>0 && (p.lv<=lv+5 || lv==-1)) {
                available.add(p);
                weightSum+=p.weight;
            }
        }

        //get
        Iterator<BookData> it=available.iterator();
        int r=instance.rand.nextInt(weightSum);
        int sum=0;
        while(it.hasNext()){
            BookData f=it.next();
            sum+=f.weight;
            if(r<=sum){
                ItemStack result = new ItemStack(ItemCore.bookNoDecoded, 1, instance.rand.nextInt(0xfff+1));
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("title", f.title);
                result.setTagCompound(nbt);

                return result;
            }
        }
        return null;
    }
    public static ItemStack GetBookItemStack(String title){
        ItemStack result = new ItemStack(ItemCore.bookNoDecoded, 1, instance.rand.nextInt(0xfff+1));
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("title", title);
        result.setTagCompound(nbt);

        return result;
    }

    public static Map.Entry<String, BookData>[] GetBooks(){
        Map.Entry<String, BookData>[] ret=new Map.Entry[instance.books.size()];
        instance.books.entrySet().toArray(ret);
        return ret;
    }
    public static String GetDecoderNameFromItemStack(ItemStack itemStack){
        if(!itemStack.hasTagCompound()) return null;
        NBTTagCompound nbt=itemStack.getTagCompound();
        return nbt.getString("decoder");
    }


    /*
    ####################################################
                       Magic
    ####################################################
    */
    public static void RegisterMagic(String title, String type, int aria, double exp, int min, int max, Class<? extends MagicBase> magic){
        MagicData md=new Registry.MagicData();
        md.title=title;
        md.ariaTick=aria;
        md.magic=magic;
        md.exp=exp;
        md.minUse=min;
        md.maxUse=max;
        md.type=type;

        instance.magics.put(title, md);
    }

    private static MagicData cashMagic;
    public static MagicData GetMagic(String title){
        if(cashMagic!=null && cashMagic.title.equals(title)) return cashMagic;
        cashMagic=instance.magics.get(title);
        return cashMagic;
    }
    public static MagicData GetMagicDataFromItemStack(ItemStack itemStack){
        if(!itemStack.hasTagCompound()) return null;
        NBTTagCompound nbt=itemStack.getTagCompound();
        return Registry.GetMagic(nbt.getString("title"));
    }


    /*
    ####################################################
                       Trouble
    ####################################################
    */
    public static void RegisterTrouble(TroubleBase tb, int w){
        instance.troubles.add(new WeightedTrouble(tb, w));
        instance.troublesWeightSum+=w;
    }
    public static void randomTrouble(World world, EntityPlayer player, BookData bd){
        int ws=0;
        ArrayList<WeightedTrouble> availabes=new ArrayList<WeightedTrouble>();
        for(WeightedTrouble wt : instance.troubles){
            if(wt.get().getMinimumMagicLv()<=bd.lv){
                availabes.add(wt);
                ws+=wt.weight();
            }
        }
        if(availabes.isEmpty()) return;

        int wws=0;
        int r=instance.rand.nextInt(ws);
        for(WeightedTrouble wt : availabes){
            if(r<wws+wt.weight()){
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal(wt.get().getMessage())));
                wt.get().done(world, player, bd);
                return;
            }
            wws+=wt.weight();
        }
    }


    /*
    ####################################################
                       Resonance
    ####################################################
    */
    private static IResonance cashResona;
    public void __rgResona(IResonance r){
        MagicData md=GetMagic(r.getResonanceMagicName());
        if(md!=null) md.isResonance=true;

        if(instance.resonances.isEmpty()){
            instance.resonances.add(r);
        }
        else{
            //優先度ソートしつつ入れる
            for(int i=0;i<instance.resonances.size();i++){
                if(r.priority()>=instance.resonances.get(i).priority()){
                    instance.resonances.add(i, r);
                    return;
                }
            }
            instance.resonances.add(r);
        }

    }
    public static void RegisterResonance(String title, String... books) {
        instance.__rgResona(new ShapedResonance(title, books));
    }
    public static void RegisterResonance(IResonance r){
        instance.__rgResona(r);
    }
    public static MagicData GetResonanceMagicData(LinkedList<ItemStack> items){
        if(items.isEmpty()) return null;

        //共鳴判定用リストの作成
        MagicData[] magics=new MagicData[items.size()];
        for(int i=0;i<magics.length;i++){
            magics[i]=GetMagicDataFromItemStack(items.get(i));
            if(magics[i]==null && magics.length>=2) return GetMagic("fbs.failure");
        }

        if(items.size()>=2){
            if(cashResona!=null && cashResona.isMatch(magics)) return GetMagic(cashResona.getResonanceMagicName());

            for(IResonance r : instance.resonances){
                if(r.isMatch(magics)){
                    cashResona=r;
                    return GetMagic(r.getResonanceMagicName());
                }
            }
            return GetMagic("fbs.failure");
        }
        return GetMagicDataFromItemStack(items.get(0));
    }
    public static boolean IsResonance(String title){
        if(cashResona!=null && cashResona.getResonanceMagicName().equals(title)) return true;
        for(IResonance r : instance.resonances){
            if(r.getResonanceMagicName().equals(title)){
                cashResona=r;
                return true;
            }
        }
        return false;
    }
    public static IResonance GetResonance(String title){
        if(cashResona!=null && cashResona.getResonanceMagicName().equals(title)) return cashResona;

        for(IResonance r : instance.resonances){
            if(r.getResonanceMagicName().equals(title)){
                cashResona=r;
                return r;
            }
        }
        return null;
    }


    /*
    ####################################################
                       Chest
    ####################################################
    */
    public static void RegisterChestContent(int type, ItemStack item, int weight){
        ChestContent cc=new ChestContent();
        cc.type=type;
        cc.itemStack=item;
        cc.weight=weight;
        instance.chestContents.add(cc);
    }
    public static void GetChestContents(int type, ItemStack[] inventory, float prob){
        //available
        LinkedList<ChestContent> available=new LinkedList<ChestContent>();
        int weightSum=0;
        for(ChestContent cc : instance.chestContents){
            if(cc.type==type){
                available.add(cc);
                weightSum+=cc.weight;
            }
        }

        //generate
        for(int i=0;i<inventory.length;i++){
            if(instance.rand.nextFloat()>=prob) continue;

            int r=instance.rand.nextInt(weightSum);
            int s=0;
            for(ChestContent cc : available){
                s+=cc.weight;
                if(r<s){
                    inventory[i]=cc.get();
                    break;
                }
            }
        }
    }
    public static ArrayList<ChestContent> GetChestContents(int type){
        ArrayList<ChestContent> ret=new ArrayList<ChestContent>();
        for(ChestContent cc : instance.chestContents){
            if(cc.type==type) ret.add(cc);
        }
        return ret;
    }


    /*
    ####################################################
                       Crafting
    ####################################################
    */
    public void _rgCrafting(IRecipe r, int mana){
        craftingRecipes.add(new RecipePair(r, mana));
    }
    public static void RegisterRecipe(IRecipe recipe, int mana){ instance._rgCrafting(recipe, mana);}
    public static void RegisterShapedRecipe(ItemStack out, int mana, Object ... params) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (params[i] instanceof String[]) {
            String[] astring = (String[]) ((String[]) params[i++]);

            for (int l = 0; l < astring.length; ++l) {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        } else {
            while (params[i] instanceof String) {
                String s2 = (String) params[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < params.length; i += 2) {
            Character character = (Character) params[i];
            ItemStack itemstack1 = null;

            if (params[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item) params[i + 1]);
            } else if (params[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block) params[i + 1], 1, 32767);
            } else if (params[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack) params[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];
        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).copy();
            } else {
                aitemstack[i1] = null;
            }
        }

        ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, out);
        instance._rgCrafting(shapedrecipes, mana);
    }
    public static void RegisterShapelessRecipe(ItemStack out, int mana,  Object ... params) {
        ArrayList arraylist = new ArrayList();
        Object[] aobject = params;
        int i = params.length;

        for (int j = 0; j < i; ++j) {
            Object object1 = aobject[j];

            if (object1 instanceof ItemStack) {
                arraylist.add(((ItemStack) object1).copy());
            } else if (object1 instanceof Item) {
                arraylist.add(new ItemStack((Item) object1));
            } else {
                if (!(object1 instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new ItemStack((Block) object1));
            }
        }


        instance._rgCrafting(new ShapelessRecipes(out, arraylist), mana);
    }
    public static void RegisterShapedOreRecipe(ItemStack out, int mana, Object ... params){
        instance._rgCrafting(new ShapedOreRecipe(out, params), mana);
    }
    public static void RegisterShapelessOreRecipe(ItemStack out, int mana, Object ... params){
        instance._rgCrafting(new ShapelessOreRecipe(out, params), mana);
    }
    public static RecipePair FindMatchingRecipe(InventoryCrafting p_82787_1_, World p_82787_2_) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;
        int j;

        for (j = 0; j < p_82787_1_.getSizeInventory(); ++j) {
            ItemStack itemstack2 = p_82787_1_.getStackInSlot(j);

            if (itemstack2 != null) {
                if (i == 0) {
                    itemstack = itemstack2;
                }

                if (i == 1) {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        for (j = 0; j < instance.craftingRecipes.size(); ++j) {
            RecipePair rp = instance.craftingRecipes.get(j);
            if (rp.recipe.matches(p_82787_1_, p_82787_2_)) {
                return rp;
            }
        }

        return null;
    }



    /*
    ####################################################
                       Sanity
    ####################################################
    */
    private static ItemSanity cachedItemSanity;
    public static void RegisterItemSanity(ItemStack item, int trial, int max){
        instance.sanityItems.add(new ItemSanity(item, trial, max));
    }
    public static ItemSanity GetItemSanity(ItemStack item){
        if(cachedItemSanity !=null && cachedItemSanity.equals(item)) return cachedItemSanity;
        for(ItemSanity it : instance.sanityItems){
            if(it.equals(item)){
                cachedItemSanity =it;
                return cachedItemSanity;
            }
        }
        return null;
    }

    private static MobSanity cachedMobSanity;
    public static void RegisterMobSanity(Class<? extends IMob> type, int trial, int max){
        instance.sanityMobs.add(new MobSanity(type, trial, max));
    }
    public static MobSanity GetMobSanity(Entity e){
        if(cachedMobSanity !=null && cachedMobSanity.equals(e)) return cachedMobSanity;
        for(MobSanity it : instance.sanityMobs){
            if(it.equals(e)){
                cachedMobSanity=it;
                return cachedMobSanity;
            }
        }
        return null;
    }


    /*
    ####################################################
                    Magic Circle
    ####################################################
    */

    /**
     * 魔法陣を登録する
     * @param name
     * @param charms クラフトレシピのように、0-fの16個の値を文字列で、3*3以上の正方形になるように並べる。
     *               この値はアイテム時のチャームのメタ値を表す。半角スペースはそこにチャームが必要ないことを示す。
     *               また、中心には必ず魔法陣の核が置かれるため、そこは不問となる。
     */
    public static void RegisterMagicCircle(String name, String ... charms){
        MagicCircle mc=new MagicCircle();
        if(!mc.parse(name, charms)) return;

        instance.magicCircles.add(mc);
    }
    public static String FindMatchingMagicCircle(World world, int cx, int cy, int cz, int radius){
        for(MagicCircle mc : instance.magicCircles){
            if(mc.isMatch(world, cx-radius, cy, cz-radius, radius*2+1)) return mc.name;
        }
        return null;
    }

    /*
    ####################################################
                    Mana Container
    ####################################################
    */
    private static ItemManaContainer cashMana;
    public static void RegisterManaContainer(ItemStack item, int amount){
        if(item==null || amount<=0) return;
        instance.manaItems.add(new ItemManaContainer(item, amount));
    }
    public static void RegisterManaContainer(String itemName, int meta, int amount){
        Item item=(Item)Item.itemRegistry.getObject(itemName);
        if(item!=null){
            RegisterManaContainer(new ItemStack(item, 1, meta), amount);
        }
    }
    public static int GetContainsMana(ItemStack item){
        if(cashMana!=null && cashMana.item.isItemEqual(item)) return cashMana.amount;
        for(ItemManaContainer im : instance.manaItems){
            if(im.item.isItemEqual(item)){
                cashMana=im;
                return im.amount;
            }
        }
        return 0;
    }
    public static ArrayList<ItemManaContainer> GetManaContainers(){
        return instance.manaItems;
    }

    /*
    ####################################################
                    Fortune Cookie
    ####################################################
    */
    public static void RegisterFortuneCookieMessage(String message){
        instance.fortuneCookies.add(message);
    }
    public static String GetRandomMessage(){
        return instance.fortuneCookies.get(instance.rand.nextInt(instance.fortuneCookies.size()));
    }
    public static int GetRandomMessageVariant(String message){
        int num;

        try{
            num=Integer.parseInt(StatCollector.translateToLocal("info.fbs.lottery."+message));
        }
        catch (Exception e){
            num=1;
        }

        return instance.rand.nextInt(num);
    }
    public static String GetLocalizedFortuneCookieMessage(String message, int variant){
        return StatCollector.translateToLocal("info.fbs.lottery."+message+"."+variant);
    }

    public static class BookData{
        public String title;
        public int lv;
        public double exp;
        public float scProb;
        public boolean isMagic;
        public int sanTrial;
        public int sanMax;
        public int weight;

        public BookData copy(){
            BookData bd=new BookData();
            bd.title=title;
            bd.lv=lv;
            bd.exp=exp;
            bd.scProb=scProb;
            bd.isMagic=isMagic;
            bd.sanTrial=sanTrial;
            bd.sanMax=sanMax;
            bd.weight=weight;
            return bd;
        }
        public String getLocalizedTitle(){
            return StatCollector.translateToLocal("book." + title + ".title");
        }
    }
    public static class MagicData{
        public String title;
        public String type;
        public int ariaTick;
        public Class<? extends MagicBase> magic;
        public double exp;
        public int maxUse, minUse;
        public boolean isResonance;

        public MagicData copy(){
            MagicData md=new MagicData();
            md.title=title;
            md.ariaTick=ariaTick;
            md.magic=magic;
            md.exp=exp;
            md.minUse=minUse;
            md.maxUse=maxUse;
            md.type=type;

            return md;
        }
        public String getLocalizedTitle(){
            return StatCollector.translateToLocal("book." + title + ".title");
        }
        public MagicBase getMagic(World world, EntityPlayer player, boolean isSpelled){
            try{
                MagicBase mb = magic.newInstance();
                mb.player =player;
                mb.world=world;
                mb.magicData=copy();
                mb.bookData=Registry.GetBook(title);
                mb.rand=Registry.instance.rand;
                mb.isSpelled=isSpelled;
                mb.property= FBSEntityProperties.get(player);
                mb.usingStaff=false;
                return mb;
            }
            catch (Exception exc){
                FBS.logger.error("Error:initializing MagicBase", exc);
            }
            return null;
        }
    }
    public static class ChestContent{
        public int type;
        public ItemStack itemStack;
        public int weight;

        public ItemStack get(){
            ItemStack ret=itemStack.copy();

            //スタックサイズの決定
            ret.stackSize=1;
            if(itemStack.stackSize>1){
                ret.stackSize+=instance.rand.nextInt(itemStack.stackSize);
            }

            //エンチャント処理
            if(itemStack.getItem()==Items.book && instance.rand.nextFloat()<1.5f){
                ret=EnchantmentHelper.addRandomEnchantment(instance.rand, ret, 30);
                ret.stackSize=1;
            }
            else if(itemStack.getItem()==ItemCore.enchantScroll){
                ItemStack dummy=EnchantmentHelper.addRandomEnchantment(instance.rand, new ItemStack(Items.book), 30);
                Map e=EnchantmentHelper.getEnchantments(dummy);
                while(e.size()>=2){
                    e.remove(e.keySet().iterator().next());
                }
                EnchantmentHelper.setEnchantments(e, ret);
                ret.stackSize=1;
            }
            return ret;
        }
    }
    public static class RecipePair{
        public IRecipe recipe;
        public int mana;

        public RecipePair(IRecipe r, int m){
            recipe=r;
            mana=m;
        }
    }
    public static class MagicCircle{
        public String name;
        public int size;
        public int[][] charms;
        public int[][] charms1;
        public int[][] charms2;
        public int[][] charms3;
        public boolean isEnable=false;

        public boolean parse(String name, String ... params) {
            this.name=name;

            if(params.length<=1 || params.length>5){
                FMLLog.severe("Failed to parse Magic Circle : Magic circle has invalid size");
                isEnable=false;
                return false;
            }

            if (params.length != params[0].length()) {
                FMLLog.severe("Failed to parse Magic Circle : Magic circle is not square");
                isEnable=false;
                return false;
            }
            if (params.length%2==0) {
                FMLLog.severe("Failed to parse Magic Circle : Magic circle do not have center");
                isEnable=false;
                return false;
            }

            size = params.length;
            isEnable=true;

            charms = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int k = 0; k < size; k++) {
                    String c=params[i].substring(k, k+1);
                    if(c.equals(" ")) charms[i][k]=-1;
                    else charms[i][k] = Integer.parseInt(c, 16)^15;
                }
            }
            charms1=new int[size][size];
            for(int i=0;i<size;i++){
                for(int k=0;k<size;k++){
                    charms1[k][size-1-i]=charms[i][k];
                }
            }
            charms2=new int[size][size];
            for(int i=0;i<size;i++){
                for(int k=0;k<size;k++){
                    charms2[k][size-1-i]=charms1[i][k];
                }
            }
            charms3=new int[size][size];
            for(int i=0;i<size;i++){
                for(int k=0;k<size;k++){
                    charms3[k][size-1-i]=charms2[i][k];
                }
            }

            return true;
        }

        public boolean isMatch(World world, int bx, int by, int bz, int size){
            if(!isEnable || size!=this.size) return false;

            return check(charms, world, bx, by, bz, size) || check(charms1, world, bx, by, bz, size) ||
                    check(charms2, world, bx, by, bz, size) || check(charms3, world, bx, by, bz, size);
        }

        private boolean check(int[][] c, World world, int bx, int by, int bz, int size){
            for(int i=0;i<size;i++){
                for(int k=0;k<size;k++){
                    Block b=world.getBlock(bx+k, by, bz+i);
                    if(i==size/2 && i==k && b==BlockCore.magicCore) continue;
                    if(c[i][k]==-1){
                        if(b.getMaterial()!=Material.air) return false;
                    }
                    else{
                        if(b!=BlockCore.charm && b.getMaterial()!=Material.air) return false;
                        if(world.getBlockMetadata(bx+k, by, bz+i)!=c[i][k]) return false;
                    }
                }
            }
            return true;
        }
    }
    public static class ItemSanity{
        public ItemStack item;
        public int trial;
        public int max;

        public ItemSanity(ItemStack i, int t, int m){
            item=i;
            trial=t;
            max=m;
        }

        public boolean equals(ItemStack item){
            return this.item.isItemEqual(item);
        }
        public void sanity(EntityPlayer ep){
            if(max>0) SanityManager.addSanity(ep, trial, max, true);
            else if(max<0) SanityManager.loseSanity(ep, trial, -max, true);
        }
    }
    public static class ItemManaContainer{
        public ItemStack item;
        public int amount;
        public ItemManaContainer(ItemStack i, int a){
            item=i;
            amount=a;
        }
    }
    public static class MobSanity{
        public Class<? extends IMob> mob;
        public int trial;
        public int max;

        public MobSanity(Class<? extends IMob> type, int t, int m){
            mob=type;
            trial=t;
            max=m;
        }

        public boolean equals(Entity e){
            return e.getClass().isAssignableFrom(mob);
        }
        public void sanity(EntityPlayer ep){
            if(max>0) SanityManager.addSanity(ep, trial, max, true);
            else if(max<0) SanityManager.loseSanity(ep, trial, -max, true);
        }
    }

    public static class WeightedTrouble{
        private TroubleBase tb;
        private int w;

        public WeightedTrouble(TroubleBase tb, int weight){
            this.tb=tb;
            this.w=weight;
        }

        public TroubleBase get(){ return tb; }
        public int weight(){ return w; }
    }
}
