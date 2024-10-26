package com.rwtema.extrautils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ISidedFunction<F, T> {
  @SideOnly(Side.SERVER)
  T applyServer(F paramF);
  
  @SideOnly(Side.CLIENT)
  T applyClient(F paramF);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ISidedFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */