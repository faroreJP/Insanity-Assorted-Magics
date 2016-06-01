package jp.plusplus.fbs.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.pottery.GuiPottersWheel;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.pottery.PotteryRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pluslus_F on 2015/06/24.
 */
public class PotteryCraftHandler extends TemplateRecipeHandler {

    @Override
    public int recipiesPerPage(){
        return 1;
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(106, 53, 22, 15), "fbs.pottery", new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "fbs.pottery";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiPottersWheel.class;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 162-5, 102);
    }
    @Override
    public void drawExtras(int recipe) {}

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("fbs.pottery") && this.getClass() == PotteryCraftHandler.class) {
            Iterator i$ = PotteryRegistry.GetPotteryCrafting().iterator();

            while(i$.hasNext()) {
                PotteryRegistry.PotteryPair r=(PotteryRegistry.PotteryPair)i$.next();
                arecipes.add(new CachedPottery(r));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Iterator i$ = PotteryRegistry.GetPotteryCrafting().iterator();

        while(i$.hasNext()) {
            PotteryRegistry.PotteryPair rp=(PotteryRegistry.PotteryPair)i$.next();
            if(rp.product.isItemEqual(result)) arecipes.add(new CachedPottery(rp));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Iterator i$ = PotteryRegistry.GetPotteryCrafting().iterator();

        while(i$.hasNext()) {
            PotteryRegistry.PotteryPair rp=(PotteryRegistry.PotteryPair)i$.next();

            boolean flag=false;
            for(int i=0;i<rp.materials.length;i++){
                ItemStack item=null;
                if(rp.materials[i]=='c') flag=ingredient.getItem()==ItemCore.clayWet;
                else if(rp.materials[i]=='g') flag=ingredient.getItem()==ItemCore.clayGlow;
                else if(rp.materials[i]=='f'){
                    Item it=ingredient.getItem();
                    flag=(it==Item.getItemFromBlock(Blocks.red_flower) || it==Item.getItemFromBlock(Blocks.yellow_flower));
                }
                else if(rp.materials[i]=='s') flag=ingredient.getItem()==Items.stick;
                else if(rp.materials[i]=='b')  flag=ingredient.getItem()==Item.getItemFromBlock(Blocks.iron_bars);

            }
            if(flag) arecipes.add(new CachedPottery(rp));
        }
    }

    @Override
    public String getGuiTexture() {
        return FBS.MODID+":textures/gui/pottersWheel.png";
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.fbs.pottery", new Object[0]);
    }

    public class CachedPottery extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public PositionedStack result;
        public CachedPottery(PotteryRegistry.PotteryPair rp){
            for(int i=0;i<rp.materials.length;i++){
                ItemStack item=null;
                if(rp.materials[i]=='c') item=new ItemStack(ItemCore.clayWet);
                else if(rp.materials[i]=='g') item=new ItemStack(ItemCore.clayGlow);
                else if(rp.materials[i]=='f') item=new ItemStack(Blocks.red_flower);
                else if(rp.materials[i]=='s') item=new ItemStack(Items.stick);
                else if(rp.materials[i]=='b') item=new ItemStack(Blocks.iron_bars);

                if(item!=null) ingredients.add(new PositionedStack(item, 8-5+18*(i%5), 17-11+18*(i/5)));
            }
            result=new PositionedStack(rp.product, 140-5, 54-11);
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(PotteryCraftHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

    }
}
