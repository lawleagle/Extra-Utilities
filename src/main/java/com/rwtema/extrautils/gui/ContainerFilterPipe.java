package com.rwtema.extrautils.gui;

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
public class ContainerFilterPipe extends Container {
  public ContainerFilterPipe(IInventory player, IInventory pipe) {
    addSlotToContainer(new Slot(pipe, 0, 80, 90));
    addSlotToContainer(new Slot(pipe, 1, 80, 15));
    addSlotToContainer(new Slot(pipe, 2, 43, 33));
    addSlotToContainer(new Slot(pipe, 3, 117, 72));
    addSlotToContainer(new Slot(pipe, 4, 43, 72));
    addSlotToContainer(new Slot(pipe, 5, 117, 33));
    for (int iy = 0; iy < 3; iy++) {
      for (int i = 0; i < 9; i++)
        addSlotToContainer(new Slot(player, i + iy * 9 + 9, 8 + i * 18, 111 + iy * 18)); 
    } 
    for (int ix = 0; ix < 9; ix++)
      addSlotToContainer(new Slot(player, ix, 8 + ix * 18, 169)); 
  }
  
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    return null;
  }
  
  public boolean canInteractWith(EntityPlayer entityplayer) {
    return true;
  }
  
  @ContainerSectionCallback
  public Map<ContainerSection, List<Slot>> getSlots() {
    return InventoryTweaksHelper.getSlots(this, true);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\ContainerFilterPipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */