package com.rwtema.extrautils.tileentity.chests;

import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileFullChest extends TileEntity implements IInventory {
  public boolean canUpdate() {
    return false;
  }
  
  public final InventoryBasic inv = new InventoryBasic("tile.extrautils:chestFull.name", false, 27);
  
  public int getSizeInventory() {
    return this.inv.getSizeInventory();
  }
  
  public ItemStack getStackInSlot(int p_70301_1_) {
    return this.inv.getStackInSlot(p_70301_1_);
  }
  
  public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
    return this.inv.decrStackSize(p_70298_1_, p_70298_2_);
  }
  
  public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
    return this.inv.getStackInSlotOnClosing(p_70304_1_);
  }
  
  public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
    this.inv.setInventorySlotContents(p_70299_1_, p_70299_2_);
  }
  
  public String getInventoryName() {
    return this.inv.getInventoryName();
  }
  
  public boolean hasCustomInventoryName() {
    return this.inv.hasCustomInventoryName();
  }
  
  public int getInventoryStackLimit() {
    return this.inv.getInventoryStackLimit();
  }
  
  public void markDirty() {
    this.inv.markDirty();
  }
  
  public boolean isUseableByPlayer(EntityPlayer player) {
    return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D);
  }
  
  public void openInventory() {
    this.inv.openInventory();
  }
  
  public void closeInventory() {
    this.inv.closeInventory();
  }
  
  public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
    return this.inv.isItemValidForSlot(p_94041_1_, p_94041_2_);
  }
  
  public void func_145976_a(String displayName) {
    this.inv.func_110133_a(displayName);
  }
  
  public void writeToNBT(NBTTagCompound tag) {
    super.writeToNBT(tag);
    XUHelper.writeInventoryBasicToNBT(tag, this.inv);
  }
  
  public void readFromNBT(NBTTagCompound tag) {
    super.readFromNBT(tag);
    XUHelper.readInventoryBasicFromNBT(tag, this.inv);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\chests\TileFullChest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */