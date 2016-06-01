package jp.plusplus.fbs.block;

import cpw.mods.fml.common.registry.GameRegistry;
import jp.plusplus.fbs.item.*;
import jp.plusplus.fbs.pottery.*;
import jp.plusplus.fbs.storage.BlockMealCrystal;
import jp.plusplus.fbs.storage.BlockMealInlet;
import jp.plusplus.fbs.storage.BlockMealOutletSingle;
import jp.plusplus.fbs.storage.BlockMealTerminal;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Createdby pluslus_Fon 2015/06/06.
 */
public class BlockCore {
    public static Fluid mana;

    public static Block ore;
    public static Block block;

    public static Block cropLavender;
    public static Block cropRedLily;
    public static Block harvestableHerb;
    public static Block harvestableMushroom;
    public static Block harvestableGrass;

    public static Block fallenLeaves;
    public static Block leaves;
    public static Block plank;
    public static Block bookshelf;
    public static Block charm;
    public static Block magicCore;

    public static Block extractingFurnace;
    public static Block extractingFurnaceActive;
    public static Block fillingTable;
    public static Block workbench;
    public static Block mirror;
    public static Block pottersWheel;
    public static Block kiln;
    public static Block kilnActive;
    public static Block bonfire;

    public static Block barrier;
    public static Block portal1;
    public static Block portal2;

    public static BlockPotteryBase pot;
    public static BlockPotteryBase jar;

    public static Block tableAlchemist;
    public static Block alchemyCauldron;

    public static Block schoolTable;

    public static Block mealCrystal;
    public static Block mealInlet;
    public static Block mealOutletSingle;
    public static Block mealOutlet;
    public static Block mealTerminal;

    public static void Init(){
        mana=new Fluid("fbs.mana").setUnlocalizedName("fbs.mana").setLuminosity(15);
        FluidRegistry.registerFluid(mana);

        ore=new BlockOre().setHardness(3.5f).setResistance(5.0f);
        block=new BlockBlock("block").setHardness(5.0f).setResistance(10.0f);
        GameRegistry.registerBlock(ore, ItemOre.class,"ore");
        GameRegistry.registerBlock(block, ItemOre.class, "block");

        cropLavender=new BlockCropLavender();
        cropRedLily=new BlockCropRedLily();
        harvestableHerb=new BlockHerb();
        harvestableMushroom=new BlockMushroom();
        harvestableGrass=new BlockGrass();
        GameRegistry.registerBlock(cropLavender, "cropLavender");
        GameRegistry.registerBlock(cropRedLily, "cropRedLily");
        GameRegistry.registerBlock(harvestableHerb, ItemBlockMeta.class, "harvestableHerb");
        GameRegistry.registerBlock(harvestableMushroom, ItemBlockMeta.class, "harvestableMushroom");
        GameRegistry.registerBlock(harvestableGrass, ItemBlockMeta.class, "harvestableGrass");

        fallenLeaves=new BlockFallenLeaves();
        leaves=new BlockFBSLeaves();
        plank =new BlockFBSWood();
        bookshelf=new BlockBookshelfDark();
        charm=new BlockCharm();
        magicCore=new BlockMagicCore();
        GameRegistry.registerBlock(fallenLeaves, "fallenLeaves");
        GameRegistry.registerBlock(leaves, ItemBlockMeta.class, "leaves");
        GameRegistry.registerBlock(plank, "plank");
        GameRegistry.registerBlock(bookshelf, "bookshelf");
        GameRegistry.registerBlock(charm, "blockCharm");
        GameRegistry.registerBlock(magicCore, ItemMagicCore.class, "magicCore");

        extractingFurnace=new BlockExtractingFurnace(false);
        extractingFurnaceActive=new BlockExtractingFurnace(true).setCreativeTab(null).setLightLevel(1.0f);
        fillingTable=new BlockFillingTable();
        workbench=new BlockFBSWorkbench();
        mirror=new BlockMirror();
        pottersWheel=new BlockPottersWheel();
        kiln=new BlockKiln(false);
        kilnActive=new BlockKiln(true).setCreativeTab(null).setLightLevel(1.0f);
        bonfire=new BlockBonfire();
        GameRegistry.registerBlock(extractingFurnace, ItemBlockBase.class, "extractingFurnace");
        GameRegistry.registerBlock(extractingFurnaceActive, ItemBlockBase.class, "extractingFurnaceActive");
        GameRegistry.registerBlock(fillingTable, ItemBlockBase.class, "fillingTable");
        GameRegistry.registerBlock(workbench, ItemBlockBase.class, "workbench");
        GameRegistry.registerBlock(mirror, ItemBlockBase.class, "mirror");
        GameRegistry.registerBlock(pottersWheel, ItemBlockBase.class, "pottersWheel");
        GameRegistry.registerBlock(kiln, ItemBlockBase.class, "kiln");
        GameRegistry.registerBlock(kilnActive, ItemBlockBase.class, "kilnActive");
        GameRegistry.registerBlock(bonfire, "bonfire");

        barrier=new BlockBarrier();
        portal1=new BlockPortalWarp();
        portal2=new BlockPortalAutumn();
        GameRegistry.registerBlock(barrier, "barrier");
        GameRegistry.registerBlock(portal1, "portal1");
        GameRegistry.registerBlock(portal2, "portal2");

        pot =new BlockPot(350);
        //jar =new BlockJar(350);
        GameRegistry.registerBlock(pot, ItemBlockPottery.class, "pot");
        //GameRegistry.registerBlock(jar, ItemBlockPottery.class, "jar");

        tableAlchemist=new BlockAlchemistTable();
        alchemyCauldron=new BlockAlchemyCauldron();
        GameRegistry.registerBlock(tableAlchemist, ItemAlchemyTable.class, "tableAlchemist");
        GameRegistry.registerBlock(alchemyCauldron, ItemAlchemyCauldron.class, "alchemyCauldron");

        schoolTable=new BlockSchoolTable();
        GameRegistry.registerBlock(schoolTable, "schoolTable");

        mealCrystal=new BlockMealCrystal();
        mealInlet=new BlockMealInlet();
        mealOutletSingle=new BlockMealOutletSingle();
        mealTerminal=new BlockMealTerminal();
        GameRegistry.registerBlock(mealCrystal, ItemBlockBase.class, "mealCrystal");
        GameRegistry.registerBlock(mealInlet, ItemBlockBase.class, "mealInlet");
        GameRegistry.registerBlock(mealOutletSingle, ItemBlockBase.class, "mealOutletSingle");
        GameRegistry.registerBlock(mealTerminal, ItemBlockBase.class, "mealTerminal");
    }
}
