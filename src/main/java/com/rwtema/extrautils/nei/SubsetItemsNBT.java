package com.rwtema.extrautils.nei;

import codechicken.nei.api.ItemFilter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SubsetItemsNBT implements ItemFilter {
  public Item item;
  
  public SubsetItemsNBT(Item item) {
    this.item = item;
  }
  
  public boolean matches(ItemStack item) {
    return (item.hasTagCompound() && this.item.equals(item.getItem()));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\SubsetItemsNBT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */