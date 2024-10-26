package com.rwtema.extrautils.tileentity;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityTrashCanEnergy extends TileEntity implements IEnergyReceiver {
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    return (int)Math.ceil(maxReceive * 0.9D);
  }
  
  public int getEnergyStored(ForgeDirection from) {
    return 0;
  }
  
  public int getMaxEnergyStored(ForgeDirection from) {
    return 268435455;
  }
  
  public boolean canConnectEnergy(ForgeDirection from) {
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityTrashCanEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */