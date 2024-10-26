package com.rwtema.extrautils.modintegration;

import ic2.api.energy.prefab.BasicSink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class IC2EnergyHandler {
  private final TileEntity ic2EnergySink;
  
  public IC2EnergyHandler(TileEntity parent1, int capacity1, int tier1) {
    this.ic2EnergySink = (TileEntity)new BasicSink(parent1, capacity1, tier1);
  }
  
  public void updateEntity() {
    this.ic2EnergySink.updateEntity();
  }
  
  public void useEnergy(double e) {
    ((BasicSink)this.ic2EnergySink).useEnergy(e);
  }
  
  public double getEnergyStored() {
    return ((BasicSink)this.ic2EnergySink).getEnergyStored();
  }
  
  public void onChunkUnload() {
    this.ic2EnergySink.onChunkUnload();
  }
  
  public void writeToNBT(NBTTagCompound tagCompound) {
    try {
      this.ic2EnergySink.writeToNBT(tagCompound);
    } catch (Throwable ignored) {}
  }
  
  public void readFromNBT(NBTTagCompound tagCompound) {
    try {
      this.ic2EnergySink.readFromNBT(tagCompound);
    } catch (Throwable ignored) {}
  }
  
  public void invalidate() {
    this.ic2EnergySink.invalidate();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\IC2EnergyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */