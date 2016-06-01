package jp.plusplus.fbs.item;

import cpw.mods.fml.common.registry.GameRegistry;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.alchemy.*;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.item.enchant.EnchantmentCleverness;
import jp.plusplus.fbs.item.enchant.EnchantmentSanityProtect;
import jp.plusplus.fbs.item.enchant.EnchantmentWealth;
import jp.plusplus.fbs.spirit.ItemSwordSpirit;
import jp.plusplus.fbs.storage.ItemMealFragment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemSeeds;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Createdby pluslus_Fon 2015/06/05.
 */
public class ItemCore {
    public static ItemArmor.ArmorMaterial materialMonocleWood;
    public static ItemArmor.ArmorMaterial materialMonocle;
    public static ItemArmor.ArmorMaterial materialMonocleGold;
    public static ItemArmor.ArmorMaterial materialInifinity;

    public static int eIdSanity;
    public static int eIdCleverness;
    public static int eIdWealth;
    public static Enchantment enchantmentSanity;
    public static Enchantment enchantmentCleverness;
    public static Enchantment enchantmentWealth;

    public static Item gem;

    public static Item seedLavender;
    public static Item lavender;
    public static Item seedRedLily;
    public static Item redLily;
    public static Item redLilyDirty;

    public static Item stick;
    public static Item butterfly;
    public static Item instantMana;
    public static Item membership;
    public static Item enchantScroll;
    public static Item mpCoin;
    public static Item potionBless;
    public static Item cookieFortune;

    public static Item monocleWood;
    public static Item monocle;
    public static Item monocleGold;
    public static Item cloak;
    public static Item infinityHelm;
    public static Item infinityArmor;
    public static Item infinityLegs;
    public static Item infinityBoots;
    public static Item luckyDagger;

    public static Item bucketMana;
    public static Item bookmark;
    public static Item net;
    public static Item shovel;
    public static Item foldingFan;
    public static Item tableware;

    public static Item potionOblivion;
    public static Item potionSan;

    public static Item stoneInactive;
    public static Item stoneActive;
    public static Item stoneActiveMale;
    public static Item stoneActiveFemale;
    public static Item spiritSword;

    public static Item staffHead1;
    public static Item staffHead2;
    public static Item staffHead3;
    public static Item staffHead4;
    public static Item staffHead5;

    public static Item staff1_1;
    public static Item staff1_2;
    public static Item staff1_3;
    public static Item staff1_4;
    public static Item staff1_5;
    public static Item staff2_1;
    public static Item staff2_2;
    public static Item staff2_3;
    public static Item staff2_4;
    public static Item staff2_5;

    public static Item charm;

    public static ItemBookNoDecoded bookNoDecoded;
    public static ItemOldBook bookOld;
    public static ItemBookSorcery bookSorcery;
    public static ItemBookBroken bookBroken;
    public static ItemBookWhite bookWhite;

    public static Item clayWet;
    public static Item clayGlow;

    public static Item basket;
    public static Item alchemyMaterial;
    public static Item alchemyIntermediateMaterial;
    public static Item alchemyMaterialEatable;
    public static Item alchemyPotion;
    public static Item alchemyRecipe;

    public static Item herbUnknown;
    public static Item seedsUnknown;
    public static Item fruitsUnknown;
    public static Item flowerUnknown;
    public static Item grassUnknown;
    public static Item mushroomUnknown;

    public static Item mealFragment;

    public static Item potMagic;


    public static void Init(){
        materialMonocleWood=EnumHelper.addArmorMaterial("MonocleWood", 1, new int[]{0,0,0,0}, 0);
        materialMonocle=EnumHelper.addArmorMaterial("Monocle", 5, new int[]{0,0,0,0}, 0);
        materialMonocleGold=EnumHelper.addArmorMaterial("MonocleGold", 3, new int[]{0,0,0,0}, 0);
        materialInifinity=EnumHelper.addArmorMaterial("Infinity", 60, new int[]{3,8,6,3}, 0);

        enchantmentSanity=new EnchantmentSanityProtect(eIdSanity, 8);
        enchantmentCleverness=new EnchantmentCleverness(eIdCleverness, 2);
        enchantmentWealth=new EnchantmentWealth(eIdWealth, 2);

        gem=new ItemGem();
        GameRegistry.registerItem(gem, "gem");

        seedLavender=new ItemSeeds(BlockCore.cropLavender, Blocks.farmland).setCreativeTab(FBS.tab).setUnlocalizedName("fbs.seedLavender").setTextureName(FBS.MODID+":seedLavender");
        lavender=new ItemLavender();
        seedRedLily=new ItemSeedRedLily();
        redLily=new ItemBase().setCreativeTab(FBS.tab).setUnlocalizedName("redLily").setTextureName("redLily");
        redLilyDirty=new ItemBase().setCreativeTab(FBS.tab).setUnlocalizedName("redLilyDirty").setTextureName("redLilyDirty");
        GameRegistry.registerItem(seedLavender, "seedLavender");
        GameRegistry.registerItem(lavender, "lavender");
        GameRegistry.registerItem(seedRedLily, "seedRedLily");
        GameRegistry.registerItem(redLily, "redLily");
        GameRegistry.registerItem(redLilyDirty, "redLilyDirty");

        stick=new ItemBase().setUnlocalizedName("stick").setTextureName("stick").setFull3D();
        butterfly=new ItemButterfly();
        instantMana=new ItemBase().setUnlocalizedName("instantMana").setTextureName("instantMana");
        membership=new ItemBase().setUnlocalizedName("membership").setTextureName("membership");
        enchantScroll=new ItemEnchantScroll();
        mpCoin=new ItemMPCoin();
        potionBless=new ItemBase().setUnlocalizedName("potionBless").setTextureName("potionBless");
        cookieFortune=new ItemFortuneCookie();
        GameRegistry.registerItem(stick, "stick");
        GameRegistry.registerItem(butterfly, "butterfly");
        GameRegistry.registerItem(instantMana, "instantMana");
        GameRegistry.registerItem(membership, "membership");
        GameRegistry.registerItem(enchantScroll, "enchantScroll");
        GameRegistry.registerItem(mpCoin, "mpCoin");
        GameRegistry.registerItem(potionBless, "potionBless");
        GameRegistry.registerItem(cookieFortune, "cookieFortune");

        monocleWood=new ItemMonocle(materialMonocleWood).setUnlocalizedName("fbs.monocleWood").setTextureName(FBS.MODID+":monocleWood");
        monocle=new ItemMonocle(materialMonocle).setUnlocalizedName("fbs.monocle").setTextureName(FBS.MODID+":monocle");
        monocleGold=new ItemMonocle(materialMonocleGold).setUnlocalizedName("fbs.monocleGold").setTextureName(FBS.MODID+":monocleGold");
        cloak=new ItemCloak(materialMonocle);
        infinityHelm=new ItemArmorInfinity(materialInifinity, 0).setUnlocalizedName("fbs.infinityHelm").setTextureName(FBS.MODID+":infinityHelm");
        infinityArmor=new ItemArmorInfinity(materialInifinity, 1).setUnlocalizedName("fbs.infinityArmor").setTextureName(FBS.MODID+":infinityArmor");
        infinityLegs=new ItemArmorInfinity(materialInifinity, 2).setUnlocalizedName("fbs.infinityLegs").setTextureName(FBS.MODID+":infinityLegs");
        infinityBoots=new ItemArmorInfinity(materialInifinity, 3).setUnlocalizedName("fbs.infinityBoots").setTextureName(FBS.MODID+":infinityBoots");
        luckyDagger=new ItemLuckyDagger();
        GameRegistry.registerItem(monocleWood, "monocleWood");
        GameRegistry.registerItem(monocle, "monocle");
        GameRegistry.registerItem(monocleGold, "monocleGold");
        GameRegistry.registerItem(cloak, "cloak");
        GameRegistry.registerItem(infinityHelm, "infinityHelm");
        GameRegistry.registerItem(infinityArmor, "infinityArmor");
        GameRegistry.registerItem(infinityLegs, "infinityLegs");
        GameRegistry.registerItem(infinityBoots, "infinityBoots");
        GameRegistry.registerItem(luckyDagger, "luckyDagger");

        bucketMana=new Item().setCreativeTab(FBS.tab).setTextureName(FBS.MODID+":bucketMana").setUnlocalizedName("fbs.bucketMana").setMaxStackSize(1);
        bookmark=new ItemBookmark();
        net=new ItemNet();
        shovel=new ItemShovel();
        foldingFan=new ItemFoldingFan();
        tableware=new ItemPlaceable();
        GameRegistry.registerItem(bucketMana, "bucketMana");
        GameRegistry.registerItem(bookmark, "bookmark");
        GameRegistry.registerItem(net, "net");
        GameRegistry.registerItem(shovel, "shovel");
        GameRegistry.registerItem(foldingFan, "foldingFan");
        GameRegistry.registerItem(tableware, "tableware");

        potionOblivion=new ItemPotionOblivion();
        potionSan=new ItemPotionSanity();
        GameRegistry.registerItem(potionOblivion, "potionOblivion");
        GameRegistry.registerItem(potionSan, "potionSan");

        stoneInactive=new ItemBase().setCreativeTab(FBS.tabSpirit).setUnlocalizedName("stoneInactive").setTextureName("stoneInactive").setMaxStackSize(1);
        stoneActive=new ItemStoneSpirit().setUnlocalizedName("stoneActivated").setTextureName("stoneActivated");
        stoneActiveMale=new ItemStoneSpirit().setUnlocalizedName("stoneActivatedMale").setTextureName("stoneActivatedMale");
        stoneActiveFemale=new ItemStoneSpirit().setUnlocalizedName("stoneActivatedFemale").setTextureName("stoneActivatedFemale");
        spiritSword=new ItemSwordSpirit();
        GameRegistry.registerItem(stoneInactive, "stoneInactive");
        GameRegistry.registerItem(stoneActive, "stoneActive");
        GameRegistry.registerItem(stoneActiveMale, "stoneActiveMale");
        GameRegistry.registerItem(stoneActiveFemale, "stoneActiveFemale");
        GameRegistry.registerItem(spiritSword, "spiritSword");

        staffHead1=new ItemBase().setTextureName("staffhead1").setUnlocalizedName("staffHead1");
        staffHead2=new ItemBase().setTextureName("staffhead2").setUnlocalizedName("staffHead2");
        staffHead3=new ItemBase().setTextureName("staffhead3").setUnlocalizedName("staffHead3");
        staffHead4=new ItemBase().setTextureName("staffhead4").setUnlocalizedName("staffHead4");
        staffHead5=new ItemBase().setTextureName("staffhead5").setUnlocalizedName("staffHead5");
        GameRegistry.registerItem(staffHead1, "staffHead1");
        GameRegistry.registerItem(staffHead2, "staffHead2");
        GameRegistry.registerItem(staffHead3, "staffHead3");
        GameRegistry.registerItem(staffHead4, "staffHead4");
        GameRegistry.registerItem(staffHead5, "staffHead5");

        staff1_1 =new ItemStaff(1, 1);
        staff1_2 =new ItemStaff(1, 2);
        staff1_3 =new ItemStaff(1, 3);
        staff1_4 =new ItemStaff(1, 4);
        staff1_5 =new ItemStaff(1, 5);
        GameRegistry.registerItem(staff1_1, "staff1_1");
        GameRegistry.registerItem(staff1_2, "staff1_2");
        GameRegistry.registerItem(staff1_3, "staff1_3");
        GameRegistry.registerItem(staff1_4, "staff1_4");
        GameRegistry.registerItem(staff1_5, "staff1_5");

        staff2_1 =new ItemStaff(2, 1);
        staff2_2 =new ItemStaff(2, 2);
        staff2_3 =new ItemStaff(2, 3);
        staff2_4 =new ItemStaff(2, 4);
        staff2_5 =new ItemStaff(2, 5);
        GameRegistry.registerItem(staff2_1, "staff2_1");
        GameRegistry.registerItem(staff2_2, "staff2_2");
        GameRegistry.registerItem(staff2_3, "staff2_3");
        GameRegistry.registerItem(staff2_4, "staff2_4");
        GameRegistry.registerItem(staff2_5, "staff2_5");

        charm=new ItemCharm();
        GameRegistry.registerItem(charm, "charm");

        bookWhite=new ItemBookWhite();
        bookBroken=new ItemBookBroken();
        bookNoDecoded=new ItemBookNoDecoded();
        bookOld=new ItemOldBook();
        bookSorcery=new ItemBookSorcery();
        GameRegistry.registerItem(bookWhite, "bookWhite");
        GameRegistry.registerItem(bookBroken, "bookBroken");
        GameRegistry.registerItem(bookNoDecoded, "bookNoDecoded");
        GameRegistry.registerItem(bookOld, "bookOld");
        GameRegistry.registerItem(bookSorcery, "bookSorcery");

        clayWet=new ItemBase().setUnlocalizedName("clayWet").setTextureName("clayWet").setCreativeTab(FBS.tabPottery);
        clayGlow=new ItemBase().setUnlocalizedName("clayGlow").setTextureName("clayGlow").setCreativeTab(FBS.tabPottery);
        GameRegistry.registerItem(clayWet, "clayWet");
        GameRegistry.registerItem(clayGlow, "clayGlow");

        basket=new ItemBasket();
        alchemyRecipe=new ItemRecipeBook();
        GameRegistry.registerItem(basket, "basket");
        GameRegistry.registerItem(alchemyRecipe, "alchemyRecipe");

        herbUnknown=new ItemBase().setCreativeTab(FBS.tabAlchemy).setUnlocalizedName("in.herb").setTextureName("herbUnknown");
        seedsUnknown=new ItemBase().setCreativeTab(FBS.tabAlchemy).setUnlocalizedName("in.seeds").setTextureName("seedsUnknown");
        flowerUnknown=new ItemBase().setCreativeTab(FBS.tabAlchemy).setUnlocalizedName("in.flower").setTextureName("flowerUnknown");
        fruitsUnknown=new ItemBase().setCreativeTab(FBS.tabAlchemy).setUnlocalizedName("in.fruits").setTextureName("fruitsUnknown");
        grassUnknown=new ItemBase().setCreativeTab(FBS.tabAlchemy).setUnlocalizedName("in.grass").setTextureName("grassUnknown");
        mushroomUnknown=new ItemBase().setCreativeTab(FBS.tabAlchemy).setUnlocalizedName("in.mushroom").setTextureName("mushroomUnknown");
        GameRegistry.registerItem(herbUnknown, "herbUnknown");
        GameRegistry.registerItem(seedsUnknown, "seedsUnknown");
        GameRegistry.registerItem(flowerUnknown, "flowerUnknown");
        GameRegistry.registerItem(fruitsUnknown, "fruitsUnknown");
        GameRegistry.registerItem(grassUnknown, "grassUnknown");
        GameRegistry.registerItem(mushroomUnknown, "mushroomUnknown");

        alchemyMaterial =new ItemAlchemyMaterial();
        alchemyIntermediateMaterial=new ItemAlchemyIntermediateMaterial();
        alchemyMaterialEatable=new ItemEatableAlchemyMaterial();
        alchemyPotion =new ItemAlchemyPotion();
        GameRegistry.registerItem(alchemyMaterial, "materials0");
        GameRegistry.registerItem(alchemyIntermediateMaterial, "materials1");
        GameRegistry.registerItem(alchemyMaterialEatable, "materials2");
        GameRegistry.registerItem(alchemyPotion, "potions0");

        mealFragment=new ItemMealFragment();
        GameRegistry.registerItem(mealFragment, "mealFragment");

        /*
        potMagic=new ItemPotteryUsableBase();
        GameRegistry.registerItem(potMagic, "potMagic");
        */
    }
}
