package com.rwtema.extrautils.crafting;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.item.ItemGoldenBag;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBagDye implements IRecipe {
  public boolean matches(InventoryCrafting inv, World p_77569_2_) {
    boolean foundBag = false;
    boolean foundDye = false;
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack item = inv.getStackInSlot(i);
      if (item != null)
        if (item.getItem() instanceof ItemGoldenBag) {
          if (foundBag)
            return false; 
          foundBag = true;
        } else {
          if (XUHelper.getDyeFromItemStack(item) == -1)
            return false; 
          foundDye = true;
        }  
    } 
    return (foundBag && foundDye);
  }
  
  public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
    ItemStack result = null;
    int[] color = new int[3];
    int intensity = 0;
    int numDyes = 0;
    ItemGoldenBag itemBag = null;
    for (int k = 0; k < p_77572_1_.getSizeInventory(); k++) {
      ItemStack itemstack = p_77572_1_.getStackInSlot(k);
      if (itemstack != null)
        if (itemstack.getItem() instanceof ItemGoldenBag) {
          itemBag = (ItemGoldenBag)itemstack.getItem();
          if (result != null)
            return null; 
          result = itemstack.copy();
          result.stackSize = 1;
          if (itemBag.hasColor(itemstack)) {
            int j = itemBag.getColor(result);
            float f1 = (j >> 16 & 0xFF) / 255.0F;
            float f2 = (j >> 8 & 0xFF) / 255.0F;
            float f3 = (j & 0xFF) / 255.0F;
            intensity = (int)(intensity + Math.max(f1, Math.max(f2, f3)) * 255.0F);
            color[0] = (int)(color[0] + f1 * 255.0F);
            color[1] = (int)(color[1] + f2 * 255.0F);
            color[2] = (int)(color[2] + f3 * 255.0F);
            numDyes++;
          } 
        } else {
          int c = XUHelper.getDyeFromItemStack(itemstack);
          if (c == -1)
            return null; 
          float[] afloat = EntitySheep.fleeceColorTable[BlockColored.func_150032_b(c)];
          int j = (int)(afloat[0] * 255.0F);
          int m = (int)(afloat[1] * 255.0F);
          int n = (int)(afloat[2] * 255.0F);
          intensity += Math.max(j, Math.max(m, n));
          color[0] = color[0] + j;
          color[1] = color[1] + m;
          color[2] = color[2] + n;
          numDyes++;
        }  
    } 
    if (itemBag == null)
      return null; 
    int r = color[0] / numDyes;
    int g = color[1] / numDyes;
    int b = color[2] / numDyes;
    float i = intensity / numDyes;
    float max = Math.max(r, Math.max(g, b));
    r = (int)(r * i / max);
    g = (int)(g * i / max);
    b = (int)(b * i / max);
    int col = ((r << 8) + g << 8) + b;
    itemBag.setColor(result, col);
    return result;
  }
  
  public int getRecipeSize() {
    return 10;
  }
  
  public ItemStack getRecipeOutput() {
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeBagDye.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */