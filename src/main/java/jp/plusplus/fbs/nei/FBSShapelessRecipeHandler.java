package jp.plusplus.fbs.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Createdby pluslus_Fon 2015/06/15.
 */
public class FBSShapelessRecipeHandler  extends FBSShapedRecipeHandler {
    public int[][] stackorder = new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}, {1, 2}, {2, 0}, {2, 1}, {2, 2}};

    public FBSShapelessRecipeHandler() {
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.fbs.shapeless", new Object[0]);
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("fbs.crafting") && this.getClass() == FBSShapelessRecipeHandler.class) {
            Iterator i$ = Registry.getCraftingRecipes().iterator();

            while(i$.hasNext()) {
                Registry.RecipePair rp=((Registry.RecipePair)i$.next());
                IRecipe irecipe = rp.recipe;
                FBSShapelessRecipeHandler.CachedShapelessRecipe recipe = null;
                if(irecipe instanceof ShapelessRecipes) {
                    recipe = this.shapelessRecipe((ShapelessRecipes)irecipe);
                } else if(irecipe instanceof ShapelessOreRecipe) {
                    recipe = this.forgeShapelessRecipe((ShapelessOreRecipe)irecipe);
                }

                if(recipe != null) {
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
                FBSShapelessRecipeHandler.CachedShapelessRecipe recipe = null;
                if(irecipe instanceof ShapelessRecipes) {
                    recipe = this.shapelessRecipe((ShapelessRecipes)irecipe);
                } else if(irecipe instanceof ShapelessOreRecipe) {
                    recipe = this.forgeShapelessRecipe((ShapelessOreRecipe)irecipe);
                }

                if(recipe != null) {
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
            FBSShapelessRecipeHandler.CachedShapelessRecipe recipe = null;
            if(irecipe instanceof ShapelessRecipes) {
                recipe = this.shapelessRecipe((ShapelessRecipes)irecipe);
            } else if(irecipe instanceof ShapelessOreRecipe) {
                recipe = this.forgeShapelessRecipe((ShapelessOreRecipe)irecipe);
            }

            if(recipe != null && recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                this.arecipes.add(recipe);
                manas.add(rp.mana);
            }
        }

    }

    private FBSShapelessRecipeHandler.CachedShapelessRecipe shapelessRecipe(ShapelessRecipes recipe) {
        return recipe.recipeItems == null?null:new FBSShapelessRecipeHandler.CachedShapelessRecipe(recipe.recipeItems, recipe.getRecipeOutput());
    }

    public FBSShapelessRecipeHandler.CachedShapelessRecipe forgeShapelessRecipe(ShapelessOreRecipe recipe) {
        ArrayList items = recipe.getInput();
        Iterator i$ = items.iterator();

        Object item;
        do {
            if(!i$.hasNext()) {
                return new FBSShapelessRecipeHandler.CachedShapelessRecipe(items, recipe.getRecipeOutput());
            }

            item = i$.next();
        } while(!(item instanceof List) || !((List)item).isEmpty());

        return null;
    }

    public boolean isRecipe2x2(int recipe) {
        return this.getIngredientStacks(recipe).size() <= 4;
    }

    public class CachedShapelessRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedShapelessRecipe() {
            //super(FBSShapelessRecipeHandler.this);
            this.ingredients = new ArrayList();
        }

        public CachedShapelessRecipe(ItemStack output) {
            this();
            this.setResult(output);
        }

        public CachedShapelessRecipe(Object[] input, ItemStack output) {
            this((List) Arrays.asList(input), output);
        }

        public CachedShapelessRecipe(List input, ItemStack output) {
            this(output);
            this.setIngredients(input);
        }

        public void setIngredients(List<?> items) {
            this.ingredients.clear();

            for(int ingred = 0; ingred < items.size(); ++ingred) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 25 + FBSShapelessRecipeHandler.this.stackorder[ingred][0] * 18, 6 + FBSShapelessRecipeHandler.this.stackorder[ingred][1] * 18);
                stack.setMaxSize(1);
                this.ingredients.add(stack);
            }

        }

        public void setResult(ItemStack output) {
            this.result = new PositionedStack(output, 119, 24);
        }

        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(FBSShapelessRecipeHandler.this.cycleticks / 20, this.ingredients);
        }

        public PositionedStack getResult() {
            return this.result;
        }
    }
}