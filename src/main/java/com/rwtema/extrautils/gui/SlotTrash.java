package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTrash extends Slot {
  public SlotTrash(IInventory par1iInventory, int par2, int par3, int par4) {
    super(par1iInventory, par2, par3, par4);
  }
  
  public boolean isItemValid(ItemStack p_75214_1_) {
    return !getHasStack();
  }
  
  public void putStack(ItemStack par1ItemStack) {
    if (par1ItemStack != null)
      for (int i = 0; i < 64; i++) {
        if (this.inventory.getStackInSlot(i) == null) {
          TileEntityTrashCan.instantAdd = true;
          this.inventory.setInventorySlotContents(i, par1ItemStack);
          TileEntityTrashCan.instantAdd = false;
          return;
        } 
      }  
    this.inventory.setInventorySlotContents(getSlotIndex(), par1ItemStack);
    onSlotChanged();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\SlotTrash.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */