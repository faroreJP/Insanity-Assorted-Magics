package jp.plusplus.fbs;

import jp.plusplus.fbs.api.IPottery;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.item.ItemCore;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.player.AchievementEvent;

/**
 * Created by plusplus_F on 2015/11/30.
 */
public class AchievementRegistry {
    private static AchievementRegistry instance;

    public static Achievement insanity;
    public static Achievement madness;
    public static Achievement death;

    public static Achievement bonfire;
    public static Achievement extract;
    public static Achievement enchant;

    public static Achievement monocle;
    public static Achievement book;
    public static Achievement workbench;
    public static Achievement circle;
    public static Achievement witch;
    public static Achievement resonance;
    public static Achievement harvest;

    public static Achievement autumn;
    public static Achievement butterfly;
    public static Achievement sublimation;
    public static Achievement eternalAutumn;

    public static Achievement warp;
    public static Achievement crack;
    public static Achievement contract;
    public static Achievement tear;
    public static Achievement evil;
    public static Achievement best;
    public static Achievement infinity;

    public static Achievement beginner;
    public static Achievement appraisal;
    public static Achievement atelier;
    public static Achievement alchemy;
    public static Achievement herbGold;

    public static Achievement potter;
    public static Achievement grade;
    public static Achievement soulful;

    public static Achievement lucky;
    public static Achievement ga;

    private AchievementRegistry(){
    }
    private static AchievementRegistry instance(){
        return instance;
    }

    public static void register(){
        insanity=new AchievementInsanity("san", 7, -1, new ItemStack(ItemCore.lavender), null);
        madness=new AchievementInsanity("madness", 9, -2, new ItemStack(ItemCore.redLily), insanity);
        death=new AchievementInsanity("death", 9, -4, new ItemStack(ItemCore.redLilyDirty), madness);

        bonfire=new AchievementInsanity("bonfire", 7, 1, new ItemStack(BlockCore.bonfire), null);
        extract=new AchievementInsanity("extract", 9, 2, new ItemStack(ItemCore.alchemyMaterial, 1, 20), bonfire);
        enchant=new AchievementInsanity("enchant", 10, 4, new ItemStack(ItemCore.enchantScroll), extract);

        monocle=new AchievementInsanity("monocle", 0, 0, new ItemStack(ItemCore.monocle), null);
        book=new AchievementInsanity("book", 3, 0, new ItemStack(ItemCore.bookOld, 1, 555), monocle);
        workbench=new AchievementInsanity("workbench", 3, -2, new ItemStack(BlockCore.workbench), book);
        circle=new AchievementInsanity("circle", 4, -4, new ItemStack(BlockCore.magicCore), workbench);
        witch=new AchievementInsanity("witch", 5, -2, new ItemStack(ItemCore.membership), workbench);
        resonance=new AchievementInsanity("resonance", 3, 2, new ItemStack(ItemCore.staff2_1), book);
        harvest=new AchievementInsanity("harvest", 5, 1, new ItemStack(ItemCore.mpCoin), book).setSpecial();

        autumn=new AchievementInsanity("autumn", -1, -3, new ItemStack(BlockCore.fallenLeaves), null);
        butterfly=new AchievementInsanity("butterfly", -1, -5, new ItemStack(ItemCore.butterfly), autumn);
        sublimation=new AchievementInsanity("sublimation", 1, -5, new ItemStack(ItemCore.butterfly), butterfly).setSpecial();
        eternalAutumn=new AchievementInsanity("eternalAutumn", -2, -7, new ItemStack(BlockCore.portal2), butterfly);

        warp=new AchievementInsanity("warp", 4, -6, new ItemStack(ItemCore.cloak), circle);
        crack=new AchievementInsanity("crack", 5, -8, new ItemStack(ItemCore.stoneInactive), warp);
        contract=new AchievementInsanity("contract", 7, -8, new ItemStack(ItemCore.spiritSword), crack);
        tear=new AchievementInsanity("tear", 9, -7, new ItemStack(ItemCore.alchemyMaterial, 1, 40), contract);
        evil=new AchievementInsanity("evil", 7, -6, new ItemStack(ItemCore.alchemyPotion, 1, 2), contract);
        best=new AchievementInsanity("best", 8, -10, new ItemStack(ItemCore.spiritSword), contract).setSpecial();
        infinity=new AchievementInsanity("infinity", 11, -7, new ItemStack(ItemCore.infinityArmor), tear).setSpecial();

        beginner=new AchievementInsanity("beginner", -2, 2, new ItemStack(BlockCore.tableAlchemist), null);
        appraisal=new AchievementInsanity("appraisal", -4, 1, new ItemStack(ItemCore.herbUnknown), beginner);
        atelier=new AchievementInsanity("atelier", -4, -1, new ItemStack(BlockCore.alchemyCauldron), appraisal);
        alchemy=new AchievementInsanity("alchemy", -5, -3, new ItemStack(ItemCore.alchemyRecipe), atelier);
        herbGold=new AchievementInsanity("herbGold", -2, -1, new ItemStack(ItemCore.alchemyMaterial, 36), atelier);

        potter=new AchievementInsanity("potter", 1, 2, new ItemStack(BlockCore.pottersWheel), null);
        grade=new AchievementInsanity("grade", 0, 4, BlockCore.pot.getItemStack(IPottery.PotteryState.BAKED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.MEDIUM, (byte)0, false), potter);
        soulful=new AchievementInsanity("soulful", -2, 4, BlockCore.pot.getItemStack(IPottery.PotteryState.BAKED, IPottery.PotteryGrade.NORMAL, IPottery.PotterySize.LARGE, (byte)0, false), grade).setSpecial();

        lucky=new AchievementInsanity("lucky", 1, -2, new ItemStack(ItemCore.mpCoin, 1, 5000), null).setSpecial();
        ga=new AchievementInsanity("ga", 6, -4, new ItemStack(BlockCore.schoolTable), null);

        Achievement[] page={
                insanity,madness, death,
                bonfire, extract, enchant,
                monocle, book, workbench, circle, witch, resonance, harvest,
                autumn, butterfly, sublimation, eternalAutumn,
                warp, crack, contract, tear, evil, best, infinity,
                beginner, appraisal, atelier, alchemy, herbGold,
                potter, grade, soulful,
                lucky, ga
        };
        AchievementPage.registerAchievementPage(new AchievementPage("Insanity", page));
    }
}
