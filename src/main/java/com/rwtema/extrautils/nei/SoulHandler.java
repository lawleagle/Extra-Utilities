package com.rwtema.extrautils.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class SoulHandler extends TemplateRecipeHandler {
  public String getGuiTexture() {
    return "textures/gui/container/inventory.png";
  }
  
  @SideOnly(Side.CLIENT)
  public void drawForeground(int recipe) {
    int x = (GuiDraw.getStringWidth("+") + 10) / 2;
    GuiDraw.drawString("+", 60 - x, 40, -12566464, false);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GuiDraw.changeTexture(Gui.icons);
    GuiDraw.drawTexturedModalRect(60 + x - 9, 40, 16, 0, 9, 9);
    GuiDraw.drawTexturedModalRect(60 + x - 9, 40, 52, 0, 9, 9);
  }
  
  public void drawBackground(int recipe) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GuiDraw.changeTexture(getGuiTexture());
    GuiDraw.drawTexturedModalRect(44, 0, 85, 23, 78, 40);
  }
  
  public class SoulRecipe extends TemplateRecipeHandler.CachedRecipe {
    public SoulRecipe() {
      super(SoulHandler.this);
    }
    
    public PositionedStack getResult() {
      return new PositionedStack(new ItemStack((Item)ExtraUtils.soul), 103, 13);
    }
    
    public PositionedStack getIngredient() {
      return new PositionedStack(new ItemStack(ExtraUtils.ethericSword), 47, 3);
    }
  }
  
  public void drawExtras(int recipe) {
    super.drawExtras(recipe);
  }
  
  public boolean isValid() {
    return (ExtraUtils.soulEnabled && ExtraUtils.ethericSwordEnabled);
  }
  
  public void loadCraftingRecipes(ItemStack result) {
    if (isValid() && result.getItem() == ExtraUtils.soul)
      this.arecipes.add(new SoulRecipe()); 
  }
  
  public void loadUsageRecipes(ItemStack ingredient) {
    if (isValid() && ingredient.getItem() == ExtraUtils.ethericSword)
      this.arecipes.add(new SoulRecipe()); 
  }
  
  public String getRecipeName() {
    return "Soul Crafting";
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\SoulHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */