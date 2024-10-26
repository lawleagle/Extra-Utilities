package com.rwtema.extrautils.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotChecksValidity extends Slot {
  public SlotChecksValidity(IInventory par1iInventory, int par2, int par3, int par4) {
    super(par1iInventory, par2, par3, par4);
  }
  
  public boolean isItemValid(ItemStack itemstack) {
    return this.inventory.isItemValidForSlot(this.slotNumber, itemstack);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\SlotChecksValidity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */