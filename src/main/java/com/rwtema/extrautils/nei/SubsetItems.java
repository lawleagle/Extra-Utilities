package com.rwtema.extrautils.nei;

import codechicken.nei.api.ItemFilter;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SubsetItems implements ItemFilter {
  public ArrayList<Item> items = new ArrayList<Item>();
  
  public SubsetItems(Item... items) {
    for (Item i : items) {
      if (i != null)
        this.items.add(i); 
    } 
  }
  
  public SubsetItems addItem(Item item) {
    this.items.add(item);
    return this;
  }
  
  public boolean matches(ItemStack item) {
    for (Item i : this.items) {
      if (i.equals(item.getItem()))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\SubsetItems.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */