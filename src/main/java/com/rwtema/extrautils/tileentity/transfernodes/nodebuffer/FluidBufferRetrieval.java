package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public class FluidBufferRetrieval extends FluidBuffer {
  public boolean shouldSearch() {
    return (this.tank.getFluidAmount() < this.tank.getCapacity());
  }
  
  public boolean transfer(TileEntity tile, ForgeDirection side, IPipe insertingPipe, int x, int y, int z, ForgeDirection travelDir) {
    if (tile instanceof IFluidHandler) {
      IFluidHandler destTank = (IFluidHandler)tile;
      int drainmax = (this.node.getNode().upgradeNo(3) == 0) ? 200 : this.tank.getCapacity();
      if (((TileEntityRetrievalNodeLiquid)this.node.getNode()).fill(this.node.getNodeDir(), destTank.drain(side, drainmax, true), true) > 0 && (
        (TileEntityRetrievalNodeLiquid)this.node.getNode()).fill(this.node.getNodeDir(), destTank.drain(side, drainmax, false), false) > 0)
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\FluidBufferRetrieval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */