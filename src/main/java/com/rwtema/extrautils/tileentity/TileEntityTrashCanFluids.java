package com.rwtema.extrautils.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityTrashCanFluids extends TileEntity implements IFluidHandler {
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    return (resource != null) ? resource.amount : 0;
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    return null;
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    return null;
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return true;
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return false;
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return new FluidTankInfo[] { new FluidTankInfo(null, 16777215) };
  }
  
  public boolean canUpdate() {
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityTrashCanFluids.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */