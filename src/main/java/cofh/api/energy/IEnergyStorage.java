package cofh.api.energy;

public interface IEnergyStorage {
  int receiveEnergy(int paramInt, boolean paramBoolean);
  
  int extractEnergy(int paramInt, boolean paramBoolean);
  
  int getEnergyStored();
  
  int getMaxEnergyStored();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\cofh\api\energy\IEnergyStorage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */