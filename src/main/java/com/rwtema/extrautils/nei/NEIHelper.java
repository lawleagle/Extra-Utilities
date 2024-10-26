package com.rwtema.extrautils.nei;

import codechicken.nei.recipe.DefaultOverlayHandler;
import codechicken.nei.recipe.RecipeInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;

public class NEIHelper {
  public static boolean isCraftingGUI(GuiContainer gui) {
    if (gui.getClass() == GuiCrafting.class)
      return true; 
    if (RecipeInfo.hasOverlayHandler(gui, "crafting") && 
      RecipeInfo.getOverlayHandler(gui, "crafting").getClass() == DefaultOverlayHandler.class)
      return true; 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\NEIHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */