package com.rwtema.extrautils.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class SlotGhostItemContainer extends SlotItemContainer {
  public SlotGhostItemContainer(IInventory par1iInventory, int slot, int x, int y, int filterIndex) {
    super(par1iInventory, slot, x, y, filterIndex);
  }
  
  public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\SlotGhostItemContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */