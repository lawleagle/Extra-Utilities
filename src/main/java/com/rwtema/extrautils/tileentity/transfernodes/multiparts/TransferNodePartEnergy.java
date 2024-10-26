package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.TMultiPart;
import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeEnergy;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.util.ForgeDirection;

@InterfaceList({@Interface(iface = "buildcraft.api.mj.ISidedBatteryProvider", modid = "BuildCraftAPI|power")})
public class TransferNodePartEnergy extends TransferNodePart implements INodeEnergy, IEnergyHandler {
  public TransferNodePartEnergy(TileEntityTransferNode node) {
    super(node);
  }
  
  public TransferNodePartEnergy() {
    super((TileEntityTransferNode)new TileEntityTransferNodeEnergy());
  }
  
  public TransferNodePartEnergy(int meta, TileEntityTransferNodeEnergy node) {
    super(meta, (TileEntityTransferNode)node);
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    List<Cuboid6> s = new ArrayList<Cuboid6>();
    s.add(new Cuboid6(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D));
    return s;
  }
  
  public TileEntityTransferNodeEnergy getNode() {
    return (TileEntityTransferNodeEnergy)this.node;
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return super.occlusionTest(npart);
  }
  
  public String getType() {
    return "extrautils:transfer_node_energy";
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
    if (isBlocked(from))
      return 0; 
    return getNode().receiveEnergy(from, maxReceive, simulate);
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
    if (isBlocked(from))
      return 0; 
    return getNode().extractEnergy(from, maxExtract, simulate);
  }
  
  public boolean canConnectEnergy(ForgeDirection from) {
    return (!isBlocked(from) && getNode().canConnectEnergy(from));
  }
  
  public int getEnergyStored(ForgeDirection from) {
    if (isBlocked(from))
      return 0; 
    return getNode().getEnergyStored(from);
  }
  
  public int getMaxEnergyStored(ForgeDirection from) {
    if (isBlocked(from))
      return 0; 
    return getNode().getMaxEnergyStored(from);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePartEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */