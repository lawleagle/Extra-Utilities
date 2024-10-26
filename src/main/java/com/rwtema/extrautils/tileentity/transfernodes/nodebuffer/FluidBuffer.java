package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

public class FluidBuffer implements INodeBuffer {
  public INode node;
  
  public FluidTank tank = new FluidTank(8000);
  
  public boolean transfer(TileEntity tile, ForgeDirection side, IPipe insertingPipe, int x, int y, int z, ForgeDirection travelDir) {
    if (isEmpty())
      return true; 
    if (tile instanceof IFluidHandler) {
      IFluidHandler destTank = (IFluidHandler)tile;
      int filter = -1;
      boolean eof = false;
      if (insertingPipe != null) {
        filter = insertingPipe.limitTransfer(tile, side, this);
        eof = insertingPipe.getOutputDirections((IBlockAccess)tile.getWorldObj(), x, y, z, travelDir, this).isEmpty();
      } 
      if (filter < 0)
        filter = this.tank.getFluidAmount(); 
      if (!eof && filter > 1)
        filter /= 2; 
      FluidStack b = this.tank.getFluid().copy();
      b.amount = Math.min(b.amount, filter);
      filter = destTank.fill(side, b, false);
      FluidStack c = this.tank.drain(filter, true);
      destTank.fill(side, c, true);
    } 
    return true;
  }
  
  public Object getBuffer() {
    return this.tank;
  }
  
  public String getBufferType() {
    return "fluid";
  }
  
  public void setBuffer(Object buffer) {
    if (buffer instanceof FluidTank)
      this.tank = (FluidTank)buffer; 
  }
  
  public boolean isEmpty() {
    if (this.tank.getFluid() != null) {
      if ((this.tank.getFluid()).amount == 0) {
        this.tank.setFluid(null);
        return true;
      } 
      return false;
    } 
    return true;
  }
  
  public void readFromNBT(NBTTagCompound tags) {
    if (tags.hasKey("buffer")) {
      this.tank.readFromNBT(tags.getCompoundTag("buffer"));
    } else {
      this.tank = new FluidTank(6400);
    } 
  }
  
  public void writeToNBT(NBTTagCompound tags) {
    if (this.tank != null) {
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      this.tank.writeToNBT(nbttagcompound1);
      tags.setTag("buffer", (NBTBase)nbttagcompound1);
    } 
  }
  
  public void setNode(INode node) {
    this.node = node;
  }
  
  public INode getNode() {
    return this.node;
  }
  
  public boolean transferTo(INodeBuffer receptor, int no) {
    if (getBuffer() == null || !getBufferType().equals(receptor.getBufferType()))
      return false; 
    if (!(receptor.getNode() instanceof IFluidHandler))
      return false; 
    ForgeDirection dir = receptor.getNode().getNodeDir();
    IFluidHandler dest = (IFluidHandler)receptor.getNode();
    int k = dest.fill(dir, this.tank.drain(200 * no, false), false);
    if (k <= 0)
      return false; 
    dest.fill(dir, this.tank.drain(k, true), true);
    receptor.setBuffer(dest);
    receptor.markDirty();
    return true;
  }
  
  public synchronized Object recieve(Object a) {
    if (!(a instanceof FluidStack))
      return a; 
    FluidStack c = (FluidStack)a;
    c.amount -= this.tank.fill(c, true);
    return c;
  }
  
  public void markDirty() {
    this.node.bufferChanged();
  }
  
  public boolean shouldSearch() {
    return !isEmpty();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\FluidBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */