package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeSoul implements IRecipe {
  public boolean matches(InventoryCrafting var1, World var2) {
    if (var1.getSizeInventory() != 4)
      return false; 
    boolean foundSword = false;
    for (int i = 0; i < var1.getSizeInventory(); i++) {
      if (var1.getStackInSlot(i) != null) {
        if (foundSword)
          return false; 
        if (var1.getStackInSlot(i).getItem() != ExtraUtils.ethericSword)
          return false; 
        foundSword = true;
      } 
    } 
    return foundSword;
  }
  
  public ItemStack getCraftingResult(InventoryCrafting var1) {
    return new ItemStack((Item)ExtraUtils.soul, 1, 2);
  }
  
  public int getRecipeSize() {
    return 1;
  }
  
  public ItemStack getRecipeOutput() {
    return new ItemStack((Item)ExtraUtils.soul);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeSoul.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */