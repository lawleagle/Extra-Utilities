package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGeneratorRedFlux extends TileEntityGeneratorFurnace implements IFluidHandler {
  public FluidTank[] tanks = new FluidTank[] { new FluidTankRestricted(4000, new String[] { "redstone", "lava" }) };
  
  public int curLevel = 0;
  
  public FluidTank[] getTanks() {
    return this.tanks;
  }
  
  public int getMaxCoolDown() {
    return 0;
  }
  
  public boolean processInput() {
    double c = getFuelBurn(getTanks()[0].getFluid());
    if (c > 0.0D && getTanks()[0].getFluidAmount() >= fluidAmmount() && addCoolDown(c, true)) {
      if ("lava".equals(FluidRegistry.getFluidName(getTanks()[0].getFluid()))) {
        double d = getFuelBurn(this.inv.getStackInSlot(0));
        if (d > 0.0D) {
          this.inv.decrStackSize(0, 1);
          this.curLevel = 80;
        } else {
          return false;
        } 
      } else {
        this.curLevel = 80;
      } 
      addCoolDown(c, false);
      getTanks()[0].drain(fluidAmmount(), true);
      markDirty();
      return true;
    } 
    return false;
  }
  
  public double genLevel() {
    return this.curLevel;
  }
  
  public int fluidAmmount() {
    return 125;
  }
  
  public double getFuelBurn(FluidStack fluid) {
    return fluidAmmount() * 2.5D;
  }
  
  public double getFuelBurn(ItemStack item) {
    return (item != null && item.getItem() == Items.redstone) ? 1.0D : 0.0D;
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    return super.fill(from, resource, doFill);
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    return super.drain(from, resource, doDrain);
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    return super.drain(from, maxDrain, doDrain);
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return super.canFill(from, fluid);
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return super.canDrain(from, fluid);
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return super.getTankInfo(from);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    this.curLevel = nbt.getInteger("curLevel");
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setInteger("curLevel", this.curLevel);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorRedFlux.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */