package com.rwtema.extrautils.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRestrictive extends Slot {
  ItemStack item = null;
  
  public SlotRestrictive(IInventory par1iInventory, int par2, int par3, int par4, ItemStack item) {
    super(par1iInventory, par2, par3, par4);
    this.item = item;
  }
  
  public boolean isItemValid(ItemStack par1ItemStack) {
    if (this.item != null && par1ItemStack != null && 
      par1ItemStack.getItem() == this.item.getItem() && (par1ItemStack.getItemDamage() == this.item.getItemDamage() || this.item.getItemDamage() == 32767))
      return true; 
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\SlotRestrictive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */