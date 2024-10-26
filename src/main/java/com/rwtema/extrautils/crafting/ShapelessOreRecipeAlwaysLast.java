package com.rwtema.extrautils.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessOreRecipeAlwaysLast extends ShapelessOreRecipe {
  public ShapelessOreRecipeAlwaysLast(Block result, Object... recipe) {
    super(result, recipe);
  }
  
  public ShapelessOreRecipeAlwaysLast(Item result, Object... recipe) {
    super(result, recipe);
  }
  
  public ShapelessOreRecipeAlwaysLast(ItemStack result, Object... recipe) {
    super(result, recipe);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\ShapelessOreRecipeAlwaysLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */