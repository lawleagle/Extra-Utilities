package com.rwtema.extrautils.tileentity.enderconstructor;

public interface IEnderFluxHandler {
  boolean isActive();
  
  int recieveEnergy(int paramInt, Transfer paramTransfer);
  
  float getAmountRequested();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\IEnderFluxHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */