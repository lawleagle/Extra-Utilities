package com.rwtema.extrautils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class LiquidInventory implements ISidedInventory {
  IFluidHandler tank;
  
  ForgeDirection side;
  
  ItemStack[] genericItems;
  
  public LiquidInventory(IFluidHandler tank, ForgeDirection side) {
    this.genericItems = new ItemStack[] { FluidContainerRegistry.EMPTY_BUCKET, FluidContainerRegistry.EMPTY_BOTTLE };
    this.tank = tank;
    this.side = side;
  }
  
  public int[] getAccessibleSlotsFromSide(int var1) {
    if (this.tank.getTankInfo(this.side) == null)
      return new int[0]; 
    int[] t = new int[(this.tank.getTankInfo(this.side)).length];
    for (int i = 0; i < t.length; i++)
      t[i] = i; 
    return t;
  }
  
  public boolean canInsertItem(int var1, ItemStack var2, int var3) {
    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(var2);
    return (f != null && f.getFluid() != null && this.tank.canFill(this.side, f.getFluid()) && this.tank.fill(this.side, f, false) == f.amount);
  }
  
  public boolean canExtractItem(int var1, ItemStack var2, int var3) {
    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(var2);
    return (f != null && f.getFluid() != null && this.tank.canDrain(this.side, f.getFluid()) && f.isFluidStackIdentical(this.tank.drain(this.side, f, false)));
  }
  
  public int getSizeInventory() {
    return (this.tank.getTankInfo(this.side)).length;
  }
  
  public ItemStack getStackInSlot(int var1) {
    FluidStack f = (this.tank.getTankInfo(this.side)[var1]).fluid;
    for (ItemStack item : this.genericItems) {
      ItemStack i = FluidContainerRegistry.fillFluidContainer(f, item);
      if (i != null)
        return i; 
    } 
    return null;
  }
  
  public ItemStack decrStackSize(int var1, int var2) {
    FluidStack f = (this.tank.getTankInfo(this.side)[var1]).fluid;
    for (ItemStack item : this.genericItems) {
      ItemStack i = FluidContainerRegistry.fillFluidContainer(f, item);
      if (i != null) {
        FluidStack t = FluidContainerRegistry.getFluidForFilledItem(i);
        if (t != null && t.isFluidEqual(this.tank.drain(this.side, t, false))) {
          this.tank.drain(this.side, t, true);
          return i;
        } 
      } 
    } 
    return null;
  }
  
  public ItemStack getStackInSlotOnClosing(int var1) {
    return getStackInSlot(var1);
  }
  
  public void setInventorySlotContents(int var1, ItemStack var2) {
    FluidStack f = FluidContainerRegistry.getFluidForFilledItem(var2);
    if (f == null || f.getFluid() == null)
      return; 
    if (!this.tank.canFill(this.side, f.getFluid()))
      return; 
    if (this.tank.fill(this.side, f, false) == f.amount)
      this.tank.fill(this.side, f, true); 
  }
  
  public String getInventoryName() {
    return "fakeTank";
  }
  
  public boolean hasCustomInventoryName() {
    return false;
  }
  
  public int getInventoryStackLimit() {
    return (this.tank.getTankInfo(this.side)).length;
  }
  
  public void markDirty() {
    if (this.tank instanceof TileEntity)
      ((TileEntity)this.tank).markDirty(); 
  }
  
  public boolean isUseableByPlayer(EntityPlayer var1) {
    return false;
  }
  
  public void openInventory() {}
  
  public void closeInventory() {}
  
  public boolean isItemValidForSlot(int var1, ItemStack var2) {
    return canInsertItem(var1, var2, 0);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\inventory\LiquidInventory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */