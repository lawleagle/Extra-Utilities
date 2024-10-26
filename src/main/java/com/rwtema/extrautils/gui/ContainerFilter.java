package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.helper.XUHelper;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;

@InventoryContainer
public class ContainerFilter extends Container {
  private EntityPlayer player = null;
  
  private int currentFilter = -1;
  
  public ContainerFilter(EntityPlayer player, int invId) {
    this.player = player;
    this.currentFilter = invId;
    int k;
    for (k = 0; k < 9; k++)
      addSlotToContainer(new SlotGhostItemContainer((IInventory)player.inventory, k, 8 + k * 18, 18, this.currentFilter)); 
    int j;
    for (j = 0; j < 3; j++) {
      for (k = 0; k < 9; k++)
        addSlotToContainer(new Slot((IInventory)player.inventory, k + j * 9 + 9, 8 + k * 18, 50 + j * 18)); 
    } 
    for (j = 0; j < 9; j++) {
      if (j == this.currentFilter) {
        addSlotToContainer(new SlotDisabled((IInventory)player.inventory, j, 8 + j * 18, 108));
      } else {
        addSlotToContainer(new Slot((IInventory)player.inventory, j, 8 + j * 18, 108));
      } 
    } 
  }
  
  public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
    if (par1 >= 0 && par1 < 9) {
      ItemStack item = par4EntityPlayer.inventory.getItemStack();
      return clickItemStack(par1, item);
    } 
    return super.slotClick(par1, par2, par3, par4EntityPlayer);
  }
  
  public ItemStack clickItemStack(int par1, ItemStack item) {
    if (item != null) {
      item = item.copy();
      item.stackSize = 1;
    } 
    String keyname = "items_" + par1;
    ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
    if (filter == null)
      return item; 
    NBTTagCompound tags = filter.getTagCompound();
    if (item != null) {
      if (tags == null)
        tags = new NBTTagCompound(); 
      if (tags.hasKey(keyname)) {
        if (FluidContainerRegistry.isFilledContainer(item) && ItemStack.areItemStacksEqual(ItemStack.loadItemStackFromNBT(tags.getCompoundTag(keyname)), item)) {
          NBTTagCompound fluidTags = new NBTTagCompound();
          if (tags.hasKey("isLiquid_" + par1)) {
            tags.removeTag("isLiquid_" + par1);
          } else {
            tags.setBoolean("isLiquid_" + par1, true);
          } 
          return item;
        } 
        if (tags.hasKey("isLiquid_" + par1))
          tags.removeTag("isLiquid_" + par1); 
        tags.removeTag(keyname);
      } else if (tags.hasKey("isLiquid_" + par1)) {
        tags.removeTag("isLiquid_" + par1);
      } 
      NBTTagCompound itemTags = new NBTTagCompound();
      item.writeToNBT(itemTags);
      tags.setTag(keyname, (NBTBase)itemTags);
      filter.setTagCompound(tags);
    } else if (tags != null) {
      if (tags.hasKey("isLiquid_" + par1))
        tags.removeTag("isLiquid_" + par1); 
      tags.removeTag(keyname);
      if (tags.hasNoTags()) {
        filter.setTagCompound(null);
      } else {
        filter.setTagCompound(tags);
      } 
    } 
    return item;
  }
  
  public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    Slot slot = this.inventorySlots.get(par2);
    if (slot != null && slot.getHasStack())
      if (slot instanceof SlotGhostItemContainer) {
        slotClick(slot.slotNumber, 0, 0, par1EntityPlayer);
      } else {
        for (int i = 0; i < 9; i++) {
          if (!((SlotGhostItemContainer)this.inventorySlots.get(i)).getHasStack()) {
            clickItemStack(i, slot.getStack());
            return null;
          } 
          if (XUHelper.canItemsStack(slot.getStack(), ((SlotGhostItemContainer)this.inventorySlots.get(i)).getStack()))
            return null; 
        } 
      }  
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


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\ContainerFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */