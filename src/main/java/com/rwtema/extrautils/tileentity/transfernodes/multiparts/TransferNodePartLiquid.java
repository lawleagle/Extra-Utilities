package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeLiquid;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class TransferNodePartLiquid extends TransferNodePart implements INodeLiquid {
  public TransferNodePartLiquid() {
    super((TileEntityTransferNode)new TileEntityTransferNodeLiquid());
  }
  
  public TransferNodePartLiquid(TileEntityTransferNode node) {
    super(node);
  }
  
  public TransferNodePartLiquid(int meta, TileEntityTransferNodeLiquid node) {
    super(meta, (TileEntityTransferNode)node);
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    Box t = new Box(0.125F, 0.0F, 0.125F, 0.875F, 0.375F, 0.875F);
    t.rotateToSide(getNodeDir());
    List<Cuboid6> s = new ArrayList<Cuboid6>();
    s.add(new Cuboid6(t.minX, t.minY, t.minZ, t.maxX, t.maxY, t.maxZ));
    return s;
  }
  
  public TileEntityTransferNodeLiquid getNode() {
    return (TileEntityTransferNodeLiquid)this.node;
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return (!(npart instanceof net.minecraftforge.fluids.IFluidHandler) && super.occlusionTest(npart));
  }
  
  public String getType() {
    return "extrautils:transfer_node_liquid";
  }
  
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
    return getNode().fill(from, resource, doFill);
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    return getNode().drain(from, resource, doDrain);
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    return getNode().drain(from, maxDrain, doDrain);
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid) {
    return getNode().canFill(from, fluid);
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid) {
    return getNode().canDrain(from, fluid);
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from) {
    return getNode().getTankInfo(from);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePartLiquid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */