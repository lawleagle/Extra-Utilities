package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFilingCabinet extends Slot {
  public static boolean drawing = false;
  
  public SlotFilingCabinet(IInventory par1iInventory, int par2, int par3, int par4) {
    super(par1iInventory, par2, par3, par4);
  }
  
  public ItemStack getStack() {
    if (drawing && getSlotIndex() >= ((TileEntityFilingCabinet)this.inventory).itemSlots.size())
      return null; 
    return super.getStack();
  }
  
  public boolean isItemValid(ItemStack item) {
    return this.inventory.isItemValidForSlot(getSlotIndex(), item);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\SlotFilingCabinet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */