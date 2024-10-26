package com.rwtema.extrautils.nei;

import codechicken.nei.api.ItemFilter;
import com.rwtema.extrautils.item.filters.Matcher;
import net.minecraft.item.ItemStack;

public class ItemFilterWrapper implements ItemFilter {
  private final Matcher base;
  
  private final boolean invert;
  
  public ItemFilterWrapper(Matcher base, boolean invert) {
    this.base = base;
    this.invert = invert;
  }
  
  public ItemFilterWrapper(Matcher matcher) {
    this(matcher, false);
  }
  
  public boolean matches(ItemStack itemStack) {
    return (this.base.matchItem(itemStack) != this.invert);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\ItemFilterWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */