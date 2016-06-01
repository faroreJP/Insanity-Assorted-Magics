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
import jp.plusplus.fbs.FBS;
import jp.plusplus.fbs.Registry;
import jp.plusplus.fbs.block.BlockCore;
import jp.plusplus.fbs.gui.GuiExtractingFurnace;
import jp.plusplus.fbs.gui.GuiWorkbench;
import jp.plusplus.fbs.item.ItemCore;
import jp.plusplus.fbs.tileentity.TileEntityExtractingFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by pluslus_F on 2015/06/24.
 */
public class FBSExtractingHandler extends TemplateRecipeHandler {
    protected LinkedList<Registry.ItemManaContainer> items=new LinkedList<Registry.ItemManaContainer>();

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(68, 35, 22, 15), "fbs.extracting", new Object[0]));
    }

    @Override
    public String getOverlayIdentifier() {
        return "fbs.extracting";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiExtractingFurnace.class;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 132-5, 65);
    }
    @Override
    public void drawExtras(int recipe) {
        int a=items.get(recipe).amount;
        String str="";
        if(a!=-1) str=""+a+" mb";
        else str="??? mb";
        GuiDraw.fontRenderer.drawString(str, 79 - (GuiDraw.fontRenderer.getStringWidth(str)) / 2 - 5, 51 - 11, 0x404040);

        //流体の描画
        int scale = 52 * a / TileEntityExtractingFurnace.TANK_CAPACITY;
        if(scale<=0) scale=1;

        int x=98-5, y=69-11-scale, w=34, h=scale;
        IIcon icon=BlockCore.mana.getIcon();

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(2.0F, 2.0F, 2.0F, 0.75F);

        GuiDraw.changeTexture(TextureMap.locationBlocksTexture);

        int sx, sy;
        for (sy = 0; h - sy * 16 > 16; sy++) {
            for (sx = 0; w - sx * 16 > 16; sx++) {
                GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, 16);
            }
            GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, 16);
        }
        for (sx = 0; w - sx * 16 > 16; sx++) {
            GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, 16, h - sy * 16);
        }
        GuiDraw.gui.drawTexturedModelRectFromIcon(x + sx * 16, y + sy * 16, icon, w - sx * 16, h - sy * 16);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GuiDraw.changeTexture(this.getGuiTexture());
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("fbs.extracting") && this.getClass() == FBSExtractingHandler.class) {
            Registry.ItemManaContainer rp;
            Iterator i$ = Registry.GetManaContainers().iterator();

            //書物
            rp=new Registry.ItemManaContainer(new ItemStack(ItemCore.bookOld), -1);
            arecipes.add(new FBSExtractingHandler.CachedExtractingRecipe(rp));
            items.add(rp);
            rp=new Registry.ItemManaContainer(new ItemStack(ItemCore.bookSorcery), -1);
            arecipes.add(new FBSExtractingHandler.CachedExtractingRecipe(rp));
            items.add(rp);

            //登録済みアイテム
            while(i$.hasNext()) {
                rp=(Registry.ItemManaContainer)i$.next();
                arecipes.add(new FBSExtractingHandler.CachedExtractingRecipe(rp));
                items.add(rp);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }

    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {}

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if(ingredient.getItem()==ItemCore.bookOld){
            items.add(new Registry.ItemManaContainer(ingredient.copy(), -1));
            return;
        }
        if(ingredient.getItem()==ItemCore.bookSorcery){
            items.add(new Registry.ItemManaContainer(ingredient.copy(), -1));
            return;
        }

        //登録済みアイテムから探す
        Iterator i$ = Registry.GetManaContainers().iterator();
        while(i$.hasNext()) {
            Registry.ItemManaContainer rp=(Registry.ItemManaContainer)i$.next();
            if(rp.item.isItemEqual(ingredient)) {
                this.arecipes.add(new FBSExtractingHandler.CachedExtractingRecipe(rp));
                items.add(rp);
            }
        }
    }

    @Override
    public String getGuiTexture() {
        return FBS.MODID+":textures/gui/extractingFurnace.png";
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.fbs.extracting", new Object[0]);
    }

    public class CachedExtractingRecipe extends CachedRecipe {
        public ArrayList<PositionedStack> ingredients=new ArrayList<PositionedStack>();
        public PositionedStack result;
        public PositionedStack input;
        public int amount;

        public CachedExtractingRecipe(Registry.ItemManaContainer im){
            ingredients.add(new PositionedStack(im.item, 44-5, 35-11));
            amount=im.amount;
        }

        @Override
        public java.util.List<PositionedStack> getIngredients() {
            return getCycledIngredients(FBSExtractingHandler.this.cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

    }
}
