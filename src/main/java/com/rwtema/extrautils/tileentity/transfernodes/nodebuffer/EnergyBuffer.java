package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import cofh.api.energy.EnergyStorage;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class EnergyBuffer implements INodeBuffer {
  TileEntityTransferNodeEnergy node;
  
  public boolean transfer(TileEntity tile, ForgeDirection side, IPipe insertingPipe, int x, int y, int z, ForgeDirection travelDir) {
    return false;
  }
  
  public EnergyStorage getBuffer() {
    return this.node.powerHandler;
  }
  
  public String getBufferType() {
    return "energy";
  }
  
  public void setBuffer(Object buffer) {
    this.node.powerHandler = (EnergyStorage)buffer;
  }
  
  public boolean isEmpty() {
    return (this.node.powerHandler.getEnergyStored() == 0);
  }
  
  public boolean shouldSearch() {
    return false;
  }
  
  public void readFromNBT(NBTTagCompound tags) {}
  
  public void writeToNBT(NBTTagCompound tags) {}
  
  public void setNode(INode node) {
    this.node = (TileEntityTransferNodeEnergy)node;
  }
  
  public INode getNode() {
    return (INode)this.node;
  }
  
  public boolean transferTo(INodeBuffer receptor, int no) {
    if (isEmpty() || !getBufferType().equals(receptor.getBufferType()))
      return false; 
    EnergyStorage t = (EnergyStorage)receptor.getBuffer();
    int e = t.receiveEnergy(getBuffer().extractEnergy(no * 240, true), true);
    if (e <= 0)
      return false; 
    t.receiveEnergy(getBuffer().extractEnergy(e, false), false);
    return true;
  }
  
  public synchronized Object recieve(Object a) {
    if (!(a instanceof Integer))
      return a; 
    int c = ((Integer)a).intValue();
    int b = this.node.powerHandler.receiveEnergy(c, false);
    return Integer.valueOf(c - b);
  }
  
  public void markDirty() {
    this.node.bufferChanged();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\EnergyBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */