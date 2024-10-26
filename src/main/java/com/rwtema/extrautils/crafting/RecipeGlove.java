package com.rwtema.extrautils.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeGlove extends ShapedOreRecipe {
  public RecipeGlove(Item glove) {
    super(glove, new Object[] { "sW", "Ws", Character.valueOf('W'), new ItemStack(Blocks.wool, 1, 32767), Character.valueOf('s'), Items.string });
  }
  
  public ItemStack getCraftingResult(InventoryCrafting var1) {
    int a = -1, b = -1;
    for (int i = 0; i < var1.getSizeInventory(); i++) {
      ItemStack stackInSlot = var1.getStackInSlot(i);
      if (stackInSlot != null && stackInSlot.getItem() == Item.getItemFromBlock(Blocks.wool))
        if (a != -1) {
          b = stackInSlot.getItemDamage();
        } else {
          a = stackInSlot.getItemDamage();
        }  
    } 
    if (a < 0 || b < 0 || b >= 16 || a >= 16)
      return super.getCraftingResult(var1); 
    ItemStack craftingResult = super.getCraftingResult(var1);
    craftingResult.setItemDamage(a << 4 | b);
    return craftingResult;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\crafting\RecipeGlove.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */