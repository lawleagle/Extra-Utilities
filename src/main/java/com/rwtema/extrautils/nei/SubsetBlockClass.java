package com.rwtema.extrautils.nei;

import codechicken.nei.api.ItemFilter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class SubsetBlockClass implements ItemFilter {
  public Class<? extends Block> base;
  
  public SubsetBlockClass(Class<? extends Block> base) {
    this.base = base;
  }
  
  public boolean matches(ItemStack item) {
    return this.base.equals(Block.getBlockFromItem(item.getItem()).getClass());
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\SubsetBlockClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */