package com.rwtema.extrautils.crafting;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeCustomOres extends ShapedOreRecipe {
  public RecipeCustomOres(ItemStack result, ItemStack toReplace, ArrayList<ItemStack> customOres, Object... recipe) {
    super(result, recipe);
    replace(toReplace, customOres);
  }
  
  public static ShapedOreRecipe replace(ShapedOreRecipe recipe, ItemStack toReplace, ArrayList<ItemStack> customOres) {
    Object[] input = recipe.getInput();
    for (int i = 0; i < input.length; i++) {
      if (input[i] instanceof ItemStack && 
        toReplace.isItemEqual((ItemStack)input[i]))
        input[i] = customOres; 
    } 
    return recipe;
  }
  
  public RecipeCustomOres replace(ItemStack toReplace, ArrayList<ItemStack> customOres) {
    return (RecipeCustomOres)replace(this, toReplace, customOres);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeCustomOres.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */