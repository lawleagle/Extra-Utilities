package com.rwtema.extrautils.dynamicgui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class WidgetSlotRespectsInsertExtract extends WidgetSlot {
  public WidgetSlotRespectsInsertExtract(ISidedInventory inv, int slot, int x, int y) {
    super((IInventory)inv, slot, x, y);
  }
  
  public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    if (getStack() == null)
      return false; 
    return ((ISidedInventory)this.inventory).canExtractItem(this.slotNumber, getStack(), 0);
  }
  
  public boolean isItemValid(ItemStack par1ItemStack) {
    if (getHasStack() || !super.isItemValid(par1ItemStack))
      return false; 
    return ((ISidedInventory)this.inventory).canInsertItem(this.slotNumber, par1ItemStack, 0);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetSlotRespectsInsertExtract.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */