package jp.plusplus.fbs.nei;

import codechicken.nei.api.API;
import jp.plusplus.fbs.gui.GuiExtractingFurnace;
import jp.plusplus.fbs.pottery.GuiPottersWheel;
import jp.plusplus.fbs.gui.GuiWorkbench;

/**
 * Created by plusplus_F on 2015/06/15.
 */
public class NEILoader {
    public static FBSShapedRecipeHandler shapedRecipe;
    public static FBSShapelessRecipeHandler shapelessRecipe;
    public static FBSExtractingHandler extractingRecipe;
    public static PotteryCraftHandler pch;
    public static AppraisalHandler ah;

    public static void LoadNEI(){
        shapedRecipe=new FBSShapedRecipeHandler();
        API.registerRecipeHandler(shapedRecipe);
        API.registerUsageHandler(shapedRecipe);
        API.registerGuiOverlay(GuiWorkbench.class, shapedRecipe.getOverlayIdentifier(), 0, 0);

        shapelessRecipe=new FBSShapelessRecipeHandler();
        API.registerRecipeHandler(shapelessRecipe);
        API.registerUsageHandler(shapelessRecipe);
        API.registerGuiOverlay(GuiWorkbench.class, shapelessRecipe.getOverlayIdentifier(), 0, 0);

        extractingRecipe=new FBSExtractingHandler();
        API.registerRecipeHandler(extractingRecipe);
        API.registerUsageHandler(extractingRecipe);
        API.registerGuiOverlay(GuiExtractingFurnace.class, extractingRecipe.getOverlayIdentifier(), 0, 0);

        pch=new PotteryCraftHandler();
        API.registerRecipeHandler(pch);
        API.registerUsageHandler(pch);
        API.registerGuiOverlay(GuiPottersWheel.class, pch.getOverlayIdentifier(), 0, 0);

        ah=new AppraisalHandler();
        API.registerRecipeHandler(ah);
        API.registerUsageHandler(ah);
    }
}
