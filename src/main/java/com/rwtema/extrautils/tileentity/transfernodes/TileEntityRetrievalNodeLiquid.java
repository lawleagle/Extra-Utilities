package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.FluidBufferRetrieval;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityRetrievalNodeLiquid extends TileEntityTransferNodeLiquid {
  public TileEntityRetrievalNodeLiquid() {
    super("liquid_remote", (INodeBuffer)new FluidBufferRetrieval());
    this.pr = 0.001F;
    this.pg = 1.0F;
    this.pb = 1.0F;
  }
  
  public void processBuffer() {
    if (this.worldObj != null && !this.worldObj.isRemote) {
      if (this.coolDown > 0)
        this.coolDown -= this.stepCoolDown; 
      if (checkRedstone())
        return; 
      while (this.coolDown <= 0) {
        this.coolDown += baseMaxCoolDown;
        unloadTank();
        if (handleInventories())
          advPipeSearch(); 
      } 
    } 
  }
  
  public void unloadTank() {
    if (this.buffer.isEmpty())
      return; 
    int dir = getBlockMetadata() % 6;
    ForgeDirection side = ForgeDirection.getOrientation(dir).getOpposite();
    if (this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]) instanceof IFluidHandler) {
      IFluidHandler dest = (IFluidHandler)this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]);
      FluidTank tank = (FluidTank)this.buffer.getBuffer();
      int a = dest.fill(side, tank.getFluid(), initDirection());
      if (a > 0)
        dest.fill(side, tank.drain(a, true), true); 
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityRetrievalNodeLiquid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */