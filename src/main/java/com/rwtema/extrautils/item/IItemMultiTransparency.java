package com.rwtema.extrautils.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IItemMultiTransparency {
  int numIcons(ItemStack paramItemStack);
  
  IIcon getIconForTransparentRender(ItemStack paramItemStack, int paramInt);
  
  float getIconTransparency(ItemStack paramItemStack, int paramInt);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\IItemMultiTransparency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */