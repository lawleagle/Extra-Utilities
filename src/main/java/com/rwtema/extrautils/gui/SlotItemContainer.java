package com.rwtema.extrautils.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SlotItemContainer extends Slot {
  static boolean checking = false;
  
  static boolean iterating = false;
  
  private static IInventory fakeInv = (IInventory)new InventoryBasic("fakeInventory", true, 54);
  
  int filterIndex = -1;
  
  ItemStack curStack = null;
  
  private IInventory filterInv;
  
  public SlotItemContainer(IInventory par1iInventory, int slot, int x, int y, int filterIndex) {
    super(fakeInv, slot, x, y);
    this.filterInv = par1iInventory;
    this.filterIndex = filterIndex;
  }
  
  public ItemStack getStack() {
    ItemStack filter = this.filterInv.getStackInSlot(this.filterIndex);
    if (filter != null && filter.getTagCompound() != null && 
      filter.getTagCompound().hasKey("items_" + getSlotIndex())) {
      if (!checking) {
        this.curStack = ItemStack.loadItemStackFromNBT(filter.getTagCompound().getCompoundTag("items_" + getSlotIndex()));
        return this.curStack;
      } 
      return ItemStack.loadItemStackFromNBT(filter.getTagCompound().getCompoundTag("items_" + getSlotIndex()));
    } 
    if (!checking) {
      this.curStack = null;
      return null;
    } 
    return null;
  }
  
  public ItemStack decrStackSize(int par1) {
    ItemStack curItem = getStack();
    if (curItem != null) {
      if (curItem.stackSize <= par1) {
        ItemStack itemStack = curItem;
        putStack(null);
        return itemStack;
      } 
      ItemStack itemstack = curItem.splitStack(par1);
      if (curItem.stackSize == 0)
        putStack(null); 
      return itemstack;
    } 
    return null;
  }
  
  public boolean getHasStack() {
    return (getStack() != null);
  }
  
  public void putStack(ItemStack par1ItemStack) {
    ItemStack filter = this.filterInv.getStackInSlot(this.filterIndex);
    if (filter == null)
      return; 
    NBTTagCompound tags = filter.getTagCompound();
    if (par1ItemStack != null) {
      if (tags == null)
        tags = new NBTTagCompound(); 
      if (tags.hasKey("items_" + getSlotIndex()))
        tags.removeTag("items_" + getSlotIndex()); 
      NBTTagCompound itemTags = new NBTTagCompound();
      par1ItemStack.writeToNBT(itemTags);
      tags.setTag("items_" + getSlotIndex(), (NBTBase)itemTags);
      filter.setTagCompound(tags);
    } else if (tags != null) {
      tags.removeTag("items_" + getSlotIndex());
      if (tags.hasNoTags()) {
        filter.setTagCompound(null);
      } else {
        filter.setTagCompound(tags);
      } 
    } 
    if (par1ItemStack != null) {
      this.curStack = par1ItemStack;
    } else {
      this.curStack = null;
    } 
    if (!iterating)
      onSlotChanged(); 
  }
  
  public void onSlotChanged() {
    checking = true;
    ItemStack oldItem = getStack();
    checking = false;
    boolean flag = false;
    if (!ItemStack.areItemStacksEqual(oldItem, this.curStack)) {
      iterating = true;
      putStack(this.curStack);
      iterating = false;
    } 
    this.filterInv.markDirty();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\SlotItemContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */