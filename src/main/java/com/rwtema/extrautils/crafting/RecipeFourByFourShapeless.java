package com.rwtema.extrautils.crafting;

import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

public class RecipeFourByFourShapeless extends ShapelessRecipes implements IRecipe {
  public RecipeFourByFourShapeless(ItemStack par1ItemStack, List par2List) {
    super(par1ItemStack, par2List);
  }
  
  public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
    return (par1InventoryCrafting.getSizeInventory() == 4 && super.matches(par1InventoryCrafting, par2World));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeFourByFourShapeless.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */