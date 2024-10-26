package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.ExtraUtils;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@ChestContainer
public class ContainerHoldingBag extends Container {
  private EntityPlayer player = null;
  
  private int currentFilter = -1;
  
  private ItemStack itemStack;
  
  public ContainerHoldingBag(EntityPlayer player, int invId) {
    this.player = player;
    this.currentFilter = invId;
    int i = 36;
    int j;
    for (j = 0; j < 6; j++) {
      for (int k = 0; k < 9; k++)
        addSlotToContainer(new SlotItemContainer((IInventory)player.inventory, k + j * 9, 8 + k * 18, 18 + j * 18, this.currentFilter)); 
    } 
    for (j = 0; j < 3; j++) {
      for (int k = 0; k < 9; k++)
        addSlotToContainer(new Slot((IInventory)player.inventory, k + j * 9 + 9, 8 + k * 18, 104 + j * 18 + i)); 
    } 
    for (j = 0; j < 9; j++) {
      if (j == this.currentFilter) {
        addSlotToContainer(new SlotDisabled((IInventory)player.inventory, j, 8 + j * 18, 162 + i));
      } else {
        addSlotToContainer(new Slot((IInventory)player.inventory, j, 8 + j * 18, 162 + i));
      } 
    } 
  }
  
  protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {}
  
  @ContainerSectionCallback
  public Map<ContainerSection, List<Slot>> getSlotType() {
    HashMap<ContainerSection, List<Slot>> hashMap = new HashMap<ContainerSection, List<Slot>>();
    hashMap.put(ContainerSection.CHEST, this.inventorySlots.subList(0, 54));
    hashMap.put(ContainerSection.INVENTORY_NOT_HOTBAR, this.inventorySlots.subList(54, 81));
    hashMap.put(ContainerSection.INVENTORY_HOTBAR, this.inventorySlots.subList(81, 90));
    hashMap.put(ContainerSection.INVENTORY, this.inventorySlots.subList(54, 90));
    return hashMap;
  }
  
  public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
    if (par3 == 2 && par2 == this.currentFilter)
      return null; 
    ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
    if (filter == null || filter.getItem() != ExtraUtils.goldenBag)
      return null; 
    return super.slotClick(par1, par2, par3, par4EntityPlayer);
  }
  
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    ItemStack itemstack = null;
    Slot slot = this.inventorySlots.get(par2);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (par2 < 54) {
        if (!mergeItemStack(itemstack1, 54, this.inventorySlots.size(), true))
          return null; 
      } else if (!mergeItemStack(itemstack1, 0, 54, false)) {
        return null;
      } 
      if (itemstack1.stackSize == 0) {
        slot.putStack(null);
        return null;
      } 
      slot.onSlotChanged();
    } 
    return itemstack;
  }
  
  public boolean canInteractWith(EntityPlayer entityplayer) {
    ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
    return (filter != null && filter.getItem() == ExtraUtils.goldenBag);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\ContainerHoldingBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */