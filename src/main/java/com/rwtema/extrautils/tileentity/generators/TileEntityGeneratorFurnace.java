package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityGeneratorFurnace extends TileEntityGenerator implements ISidedInventory {
  InventoryGeneric inv = new InventoryGeneric("generatorFurnace", false, 1);
  
  int[] slots = null;
  
  public int transferLimit() {
    return 400;
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
  }
  
  public boolean isItemValidForSlot(int i, ItemStack itemstack) {
    return (getFuelBurn(itemstack) != 0.0D && getInventory().isItemValidForSlot(i, itemstack));
  }
  
  public int getMaxCoolDown() {
    return 0;
  }
  
  public boolean shouldProcess() {
    return (this.coolDown == 0.0D || this.coolDown < getMaxCoolDown());
  }
  
  public boolean processInput() {
    return burnItem();
  }
  
  public double getGenLevelForStack(ItemStack itemStack) {
    return (getFuelBurn(itemStack) != 0.0D) ? genLevel() : 0.0D;
  }
  
  public void adjustGenLevel(ItemStack item) {}
  
  public boolean burnItem() {
    ItemStack itemStack = this.inv.getStackInSlot(0);
    double c = getFuelBurn(itemStack);
    if (c > 0.0D) {
      if (itemStack.getItem().hasContainerItem(itemStack)) {
        if (itemStack.stackSize == 1) {
          addCoolDown(c, false);
          adjustGenLevel(itemStack);
          this.inv.setInventorySlotContents(0, itemStack.getItem().getContainerItem(itemStack));
          markDirty();
          return true;
        } 
        return false;
      } 
      adjustGenLevel(itemStack);
      addCoolDown(c, false);
      this.inv.decrStackSize(0, 1);
      markDirty();
      return true;
    } 
    return false;
  }
  
  public double genLevel() {
    return 40.0D;
  }
  
  public InventoryGeneric getInventory() {
    return this.inv;
  }
  
  public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    return (getFuelBurn(itemstack) == 0.0D || (itemstack != null && itemstack.getItem().hasContainerItem(itemstack) && itemstack.stackSize > 1));
  }
  
  public double getFuelBurn(ItemStack item) {
    return TileEntityGenerator.getFurnaceBurnTime(item) * 12.5D / 40.0D;
  }
  
  public int getSizeInventory() {
    return getInventory().getSizeInventory();
  }
  
  public ItemStack getStackInSlot(int i) {
    return getInventory().getStackInSlot(i);
  }
  
  public ItemStack decrStackSize(int i, int j) {
    return getInventory().decrStackSize(i, j);
  }
  
  public ItemStack getStackInSlotOnClosing(int i) {
    return getInventory().getStackInSlotOnClosing(i);
  }
  
  public void setInventorySlotContents(int i, ItemStack itemstack) {
    getInventory().setInventorySlotContents(i, itemstack);
  }
  
  public String getInventoryName() {
    return getInventory().getInventoryName();
  }
  
  public boolean hasCustomInventoryName() {
    return getInventory().hasCustomInventoryName();
  }
  
  public int getInventoryStackLimit() {
    return getInventory().getInventoryStackLimit();
  }
  
  public boolean isUseableByPlayer(EntityPlayer entityplayer) {
    return getInventory().isUseableByPlayer(entityplayer);
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
  
  public int[] getAccessibleSlotsFromSide(int var1) {
    if (this.slots == null) {
      int t = getSizeInventory();
      this.slots = new int[t];
      for (int i = 0; i < t; i++)
        this.slots[i] = i; 
    } 
    return this.slots;
  }
  
  public boolean canInsertItem(int i, ItemStack itemstack, int j) {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorFurnace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */