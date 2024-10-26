package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeUnsigil implements IRecipe {
  Random rand = (Random)XURandom.getInstance();
  
  public boolean matches(InventoryCrafting inv, World world) {
    boolean hasSigil = false;
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() == ExtraUtils.divisionSigil) {
        if (inv.getStackInSlot(i).hasTagCompound())
          return false; 
        hasSigil = true;
      } 
    } 
    return hasSigil;
  }
  
  public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
    return new ItemStack(Items.iron_ingot, 1 + this.rand.nextInt(1 + this.rand.nextInt(1 + this.rand.nextInt(1 + this.rand.nextInt(1 + this.rand.nextInt(63))))));
  }
  
  public int getRecipeSize() {
    return 1;
  }
  
  public ItemStack getRecipeOutput() {
    return new ItemStack(Items.iron_ingot, 1);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeUnsigil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */