package com.rwtema.extrautils.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeSmasher implements IRecipe {
  public boolean matches(InventoryCrafting craft, World p_77569_2_) {
    return (getCraftingResult(craft) != null);
  }
  
  public ItemStack getCraftingResult(InventoryCrafting craft) {
    ItemStack ore = null;
    for (int i = 0; i < craft.getSizeInventory(); i++) {
      ItemStack stackInSlot = craft.getStackInSlot(i);
      if (stackInSlot != null)
        if (ore == null) {
          ore = stackInSlot;
        } else {
          return null;
        }  
    } 
    if (ore == null)
      return null; 
    if (!isOreType(ore, "ore"))
      return null; 
    ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(ore);
    if (smeltingResult == null)
      return null; 
    if (!isOreType(smeltingResult, "ingot"))
      return null; 
    ItemStack result = smeltingResult.copy();
    result.stackSize *= 3;
    if (result.stackSize < result.getMaxStackSize())
      result.stackSize = result.getMaxStackSize(); 
    return result;
  }
  
  public boolean isOreType(ItemStack ore, String type) {
    for (String s : getOreNames(ore)) {
      if (s.startsWith(type))
        return true; 
    } 
    return false;
  }
  
  public String[] getOreNames(ItemStack ore) {
    int[] oreIDs = OreDictionary.getOreIDs(ore);
    String[] result = new String[oreIDs.length];
    for (int i = 0; i < oreIDs.length; i++)
      result[i] = OreDictionary.getOreName(oreIDs[i]); 
    return result;
  }
  
  public int getRecipeSize() {
    return 1;
  }
  
  public ItemStack getRecipeOutput() {
    return new ItemStack(Items.iron_ingot);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeSmasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */