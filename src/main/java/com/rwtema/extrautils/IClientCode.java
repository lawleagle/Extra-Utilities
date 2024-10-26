package com.rwtema.extrautils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IClientCode {
  @SideOnly(Side.CLIENT)
  void exectuteClientCode();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\IClientCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */