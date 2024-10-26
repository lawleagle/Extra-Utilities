package com.rwtema.extrautils.tileentity.generators;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankRestricted extends FluidTank {
  String[] name;
  
  public FluidTankRestricted(int capacity, String... name) {
    super(capacity);
    this.name = name;
  }
  
  public int fill(FluidStack resource, boolean doFill) {
    if (this.fluid == null) {
      String str = FluidRegistry.getFluidName(resource);
      for (String aName : this.name) {
        if (aName.equals(str))
          return super.fill(resource, doFill); 
      } 
      return 0;
    } 
    return super.fill(resource, doFill);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\FluidTankRestricted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */