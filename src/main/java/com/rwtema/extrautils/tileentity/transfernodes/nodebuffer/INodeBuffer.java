package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public interface INodeBuffer {
  boolean transfer(TileEntity paramTileEntity, ForgeDirection paramForgeDirection1, IPipe paramIPipe, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection2);
  
  Object getBuffer();
  
  String getBufferType();
  
  void setBuffer(Object paramObject);
  
  boolean isEmpty();
  
  boolean shouldSearch();
  
  void readFromNBT(NBTTagCompound paramNBTTagCompound);
  
  void writeToNBT(NBTTagCompound paramNBTTagCompound);
  
  void setNode(INode paramINode);
  
  INode getNode();
  
  boolean transferTo(INodeBuffer paramINodeBuffer, int paramInt);
  
  Object recieve(Object paramObject);
  
  void markDirty();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\INodeBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */