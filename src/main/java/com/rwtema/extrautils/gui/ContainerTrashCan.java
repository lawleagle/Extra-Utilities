package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import invtweaks.api.container.InventoryContainer;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@InventoryContainer
public class ContainerTrashCan extends Container {
  private TileEntityTrashCan trashCan;
  
  private IInventory player;
  
  public ContainerTrashCan(IInventory player, TileEntityTrashCan trashCan) {
    this.trashCan = trashCan;
    addSlotToContainer(new SlotTrash((IInventory)trashCan, 0, 80, 42));
    for (int iy = 0; iy < 3; iy++) {
      for (int i = 0; i < 9; i++)
        addSlotToContainer(new Slot(player, i + iy * 9 + 9, 8 + i * 18, 90 + iy * 18)); 
    } 
    for (int ix = 0; ix < 9; ix++)
      addSlotToContainer(new Slot(player, ix, 8 + ix * 18, 148)); 
  }
  
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    ItemStack itemstack = null;
    Slot slot = this.inventorySlots.get(par2);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (par2 == 0) {
        if (!mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true))
          return null; 
      } else {
        TileEntityTrashCan.instantAdd = true;
        if (!mergeItemStack(itemstack1, 0, 1, false)) {
          TileEntityTrashCan.instantAdd = false;
          return null;
        } 
        TileEntityTrashCan.instantAdd = false;
      } 
      if (itemstack1.stackSize == 0) {
        slot.putStack(null);
      } else {
        slot.onSlotChanged();
      } 
    } 
    return itemstack;
  }
  
  public boolean canInteractWith(EntityPlayer entityplayer) {
    return this.trashCan.isUseableByPlayer(entityplayer);
  }
  
  @ContainerSectionCallback
  public Map<ContainerSection, List<Slot>> getSlots() {
    return InventoryTweaksHelper.getSlots(this, true);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\ContainerTrashCan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */