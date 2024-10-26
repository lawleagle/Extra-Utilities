package com.rwtema.extrautils.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.rwtema.extrautils.tileentity.enderconstructor.DynamicGuiEnderConstructor;
import com.rwtema.extrautils.tileentity.enderconstructor.EnderConstructorRecipesHandler;
import java.awt.Rectangle;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.lwjgl.opengl.GL11;

public class EnderConstructorHandler extends ShapedRecipeHandler {
  private static final ResourceLocation texWidgets = new ResourceLocation("extrautils", "textures/guiQED_NEI.png");
  
  public Class<? extends GuiContainer> getGuiClass() {
    return (Class)DynamicGuiEnderConstructor.class;
  }
  
  public void drawBackground(int recipe) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GuiDraw.changeTexture(texWidgets);
    GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 65);
  }
  
  public String getOverlayIdentifier() {
    return "qedcrafting";
  }
  
  public void drawForeground(int recipe) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glDisable(2896);
    GuiDraw.changeTexture(texWidgets);
    drawExtras(recipe);
  }
  
  public void loadTransferRects() {
    this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(84, 23, 24, 18), getOverlayIdentifier(), new Object[0]));
  }
  
  public String getRecipeName() {
    return "QED Recipes";
  }
  
  public void drawExtras(int recipe) {
    drawProgressBar(85, 24, 176, 0, 22, 15, 48, 0);
  }
  
  public void loadUsageRecipes(ItemStack ingredient) {
    for (IRecipe irecipe : EnderConstructorRecipesHandler.recipes) {
      ShapedRecipeHandler.CachedShapedRecipe recipe = null;
      if (irecipe instanceof ShapedRecipes) {
        recipe = new ShapedRecipeHandler.CachedShapedRecipe(this, (ShapedRecipes)irecipe);
      } else if (irecipe instanceof ShapedOreRecipe) {
        recipe = forgeShapedRecipe((ShapedOreRecipe)irecipe);
      } 
      if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
        continue; 
      recipe.computeVisuals();
      if (recipe.contains(recipe.ingredients, ingredient)) {
        recipe.setIngredientPermutation(recipe.ingredients, ingredient);
        this.arecipes.add(recipe);
      } 
    } 
  }
  
  public void loadCraftingRecipes(String outputId, Object... results) {
    if (outputId.equals(getOverlayIdentifier())) {
      for (IRecipe irecipe : EnderConstructorRecipesHandler.recipes) {
        ShapedRecipeHandler.CachedShapedRecipe recipe = null;
        if (irecipe instanceof ShapedRecipes) {
          recipe = new ShapedRecipeHandler.CachedShapedRecipe(this, (ShapedRecipes)irecipe);
        } else if (irecipe instanceof ShapedOreRecipe) {
          recipe = forgeShapedRecipe((ShapedOreRecipe)irecipe);
        } 
        if (recipe == null)
          continue; 
        recipe.computeVisuals();
        this.arecipes.add(recipe);
      } 
    } else if (outputId.equals("item")) {
      loadCraftingRecipes((ItemStack)results[0]);
    } 
  }
  
  public void loadCraftingRecipes(ItemStack result) {
    for (IRecipe irecipe : EnderConstructorRecipesHandler.recipes) {
      if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
        ShapedRecipeHandler.CachedShapedRecipe recipe = null;
        if (irecipe instanceof ShapedRecipes) {
          recipe = new ShapedRecipeHandler.CachedShapedRecipe(this, (ShapedRecipes)irecipe);
        } else if (irecipe instanceof ShapedOreRecipe) {
          recipe = forgeShapedRecipe((ShapedOreRecipe)irecipe);
        } 
        if (recipe == null)
          continue; 
        recipe.computeVisuals();
        this.arecipes.add(recipe);
      } 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\EnderConstructorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */