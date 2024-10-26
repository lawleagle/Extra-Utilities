package com.rwtema.extrautils.tileentity.generators;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGeneratorMagma extends TileEntityGenerator implements IFluidHandler {
  public FluidTank[] tanks = new FluidTank[] { new FluidTankRestricted(4000, new String[] { "lava" }) };
  
  public int transferLimit() {
    return 160;
  }
  
  public FluidTank[] getTanks() {
    return this.tanks;
  }
  
  public int getMaxCoolDown() {
    return 0;
  }
  
  public boolean shouldProcess() {
    return (this.coolDown == 0.0D || this.coolDown < getMaxCoolDown());
  }
  
  public boolean processInput() {
    for (int i = 0; i < (getTanks()).length; i++) {
      int c = getFuelBurn(getTanks()[i].getFluid());
      if (c > 0 && getTanks()[i].getFluidAmount() >= fluidAmmount() && addCoolDown(c, true)) {
        addCoolDown(c, false);
        getTanks()[i].drain(fluidAmmount(), true);
        return true;
      } 
    } 
    return false;
  }
  
  public double genLevel() {
    return 40.0D;
  }
  
  public int fluidAmmount() {
    return 100;
  }
  
  public int getFuelBurn(FluidStack fluid) {
    return fluidAmmount();
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
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorMagma.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */