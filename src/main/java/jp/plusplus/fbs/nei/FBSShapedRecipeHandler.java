package jp.plusplus.fbs.nei;

import codechicken.core.ReflectionManager;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.gui.GuiWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class FBSShapedRecipeHandler extends TemplateRecipeHandler {
    protected LinkedList<Integer> manas=new LinkedList<Integer>();

    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(90, 35, 22, 15), "fbs.crafting", new Object[0]));
    }

    public Class<? extends GuiContainer> getGuiClass() {
        return GuiWorkbench.class;
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.fbs.shaped", new Object[0]);
    }

    public void drawExtras(int recipe) {
        String str=""+manas.get(recipe)+"mb";
        GuiDraw.fontRenderer.drawString(str, (196-GuiDraw.fontRenderer.getStringWidth(str))/2, 45, 0x404040);
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("fbs.crafting") && this.getClass() == FBSShapedRecipeHandler.class) {
            Iterator i$ = Registry.getCraftingRecipes().iterator();

            while(i$.hasNext()) {
                Registry.RecipePair rp=((Registry.RecipePair)i$.next());
                IRecipe irecipe = rp.recipe;
                FBSShapedRecipeHandler.CachedShapedRecipe recipe = null;
                if(irecipe instanceof ShapedRecipes) {
                    recipe = new FBSShapedRecipeHandler.CachedShapedRecipe((ShapedRecipes)irecipe);
                } else if(irecipe instanceof ShapedOreRecipe) {
                    recipe = this.forgeShapedRecipe((ShapedOreRecipe)irecipe);
                }

                if(recipe != null) {
                    recipe.computeVisuals();
                    this.arecipes.add(recipe);
                    manas.add(rp.mana);
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    public void loadCraftingRecipes(ItemStack result) {
        Iterator i$ = Registry.getCraftingRecipes().iterator();

        while(i$.hasNext()) {
            Registry.RecipePair rp=((Registry.RecipePair)i$.next());
            IRecipe irecipe = rp.recipe;
            if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                FBSShapedRecipeHandler.CachedShapedRecipe recipe = null;
                if(irecipe instanceof ShapedRecipes) {
                    recipe = new FBSShapedRecipeHandler.CachedShapedRecipe((ShapedRecipes)irecipe);
                } else if(irecipe instanceof ShapedOreRecipe) {
                    recipe = this.forgeShapedRecipe((ShapedOreRecipe)irecipe);
                }

                if(recipe != null) {
                    recipe.computeVisuals();
                    this.arecipes.add(recipe);
                    manas.add(rp.mana);
                }
            }
        }

    }

    public void loadUsageRecipes(ItemStack ingredient) {
        Iterator i$ = Registry.getCraftingRecipes().iterator();

        while(i$.hasNext()) {
            Registry.RecipePair rp=((Registry.RecipePair)i$.next());
            IRecipe irecipe = rp.recipe;
            FBSShapedRecipeHandler.CachedShapedRecipe recipe = null;
            if(irecipe instanceof ShapedRecipes) {
                recipe = new FBSShapedRecipeHandler.CachedShapedRecipe((ShapedRecipes)irecipe);
            } else if(irecipe instanceof ShapedOreRecipe) {
                recipe = this.forgeShapedRecipe((ShapedOreRecipe)irecipe);
            }

            if(recipe != null && recipe.contains(recipe.ingredients, ingredient.getItem())) {
                recipe.computeVisuals();
                if(recipe.contains(recipe.ingredients, ingredient)) {
                    recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                    this.arecipes.add(recipe);
                    manas.add(rp.mana);
                }
            }
        }

    }

    public FBSShapedRecipeHandler.CachedShapedRecipe forgeShapedRecipe(ShapedOreRecipe recipe) {
        int width;
        int height;
        try {
            width = ((Integer) ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4)).intValue();
            height = ((Integer)ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5)).intValue();
        } catch (Exception var9) {
            NEIClientConfig.logger.error("Error loading recipe", var9);
            return null;
        }

        Object[] items = recipe.getInput();
        Object[] arr$ = items;
        int len$ = items.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Object item = arr$[i$];
            if(item instanceof java.util.List && ((java.util.List)item).isEmpty()) {
                return null;
            }
        }

        return new FBSShapedRecipeHandler.CachedShapedRecipe(width, height, items, recipe.getRecipeOutput());
    }

    public String getGuiTexture() {
        return "textures/gui/container/crafting_table.png";
    }

    public String getOverlayIdentifier() {
        return "fbs.crafting.shaped";
    }

    public boolean hasOverlay(GuiContainer gui, net.minecraft.inventory.Container container, int recipe) {
        return super.hasOverlay(gui, container, recipe) || this.isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
    }

    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
        if(renderer != null) {
            return renderer;
        } else {
            IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
            return positioner == null?null:new DefaultOverlayRenderer(this.getIngredientStacks(recipe), positioner);
        }
    }

    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
        return handler != null?handler:RecipeInfo.getOverlayHandler(gui, "crafting2x2");
    }

    public boolean isRecipe2x2(int recipe) {
        Iterator i$ = this.getIngredientStacks(recipe).iterator();

        PositionedStack stack;
        do {
            if(!i$.hasNext()) {
                return true;
            }

            stack = (PositionedStack)i$.next();
        } while(stack.relx <= 43 && stack.rely <= 24);

        return false;
    }

    public class CachedShapedRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedShapedRecipe(int width, int height, Object[] items, ItemStack out) {
            //super(FBSShapedRecipeHandler.this);
            this.result = new PositionedStack(out, 119, 24);
            this.ingredients = new ArrayList();
            this.setIngredients(width, height, items);
        }

        public CachedShapedRecipe(ShapedRecipes recipe) {
            this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
        }

        public void setIngredients(int width, int height, Object[] items) {
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    if(items[y * width + x] != null) {
                        PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
                        stack.setMaxSize(1);
                        this.ingredients.add(stack);
                    }
                }
            }

        }

        public java.util.List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(FBSShapedRecipeHandler.this.cycleticks / 20, this.ingredients);
        }

        public PositionedStack getResult() {
            return this.result;
        }

        public void computeVisuals() {
            Iterator i$ = this.ingredients.iterator();

            while(i$.hasNext()) {
                PositionedStack p = (PositionedStack)i$.next();
                p.generatePermutations();
            }

        }
    }
}
