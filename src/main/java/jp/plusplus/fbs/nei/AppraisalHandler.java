package jp.plusplus.fbs.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.alchemy.AlchemyRegistry;
import jp.plusplus.fbs.gui.GuiExtractingFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pluslus_F on 2015/06/24.
 */
public class AppraisalHandler extends TemplateRecipeHandler {
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(68, 35, 22, 15), "fbs.appraisal", new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "fbs.appraisal";
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 139-5, 65);
    }
    @Override
    public void drawExtras(int recipe) {
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("fbs.appraisal") && this.getClass() == AppraisalHandler.class) {
            for(AlchemyRegistry.ItemPair ip : AlchemyRegistry.GetAlllAppraisal()) {
                arecipes.add(new CachedAppraisalRecipe(ip.getItem1(), ip.getItem2()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        //登録済みアイテムから探す
        for(AlchemyRegistry.ItemPair ip : AlchemyRegistry.GetAlllAppraisal()){
            if(ip.getItem2().isItemEqual(result)){
                arecipes.add(new CachedAppraisalRecipe(ip.getItem1(), result));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        //登録済みアイテムから探す
        for(AlchemyRegistry.ItemPair ip : AlchemyRegistry.GetAlllAppraisal()){
            if(ip.getItem1().isItemEqual(ingredient)){
                arecipes.add(new CachedAppraisalRecipe(ingredient, ip.getItem2()));
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return FBS.MODID+":textures/gui/appraisal.png";
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.fbs.appraisal", new Object[0]);
    }

    public class CachedAppraisalRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public PositionedStack result;

        public CachedAppraisalRecipe(ItemStack in, ItemStack out){
            ingredients.add(new PositionedStack(in, 56-5, 35-11));
            result=new PositionedStack(out, 115-5,35-11);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(AppraisalHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

    }
}
