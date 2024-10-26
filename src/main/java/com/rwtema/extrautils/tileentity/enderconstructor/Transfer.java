package com.rwtema.extrautils.tileentity.enderconstructor;

public enum Transfer {
  PERFORM(false),
  SIMULATE(true);
  
  boolean simulate;
  
  Transfer(boolean simulate) {
    this.simulate = simulate;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\Transfer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */