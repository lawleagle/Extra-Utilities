package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeHyperEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class RegisterTransferNodeParts implements MultiPartRegistry.IPartFactory, MultiPartRegistry.IPartConverter {
  public void init() {
    MultiPartRegistry.registerConverter(this);
    MultiPartRegistry.registerParts(this, new String[] { "extrautils:transfer_node_inv", "extrautils:transfer_node_liquid", "extrautils:transfer_node_energy", "extrautils:transfer_node_inv_remote", "extrautils:transfer_node_liquid_remote", "extrautils:transfer_node_energy_hyper" });
  }
  
  public Iterable<Block> blockTypes() {
    Set<Block> s = new HashSet<Block>();
    s.add(ExtraUtils.transferNode);
    s.add(ExtraUtils.transferNodeRemote);
    return s;
  }
  
  public TMultiPart convert(World world, BlockCoord pos) {
    Block id = world.getBlock(pos.x, pos.y, pos.z);
    int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
    if (id == ExtraUtils.transferNode)
      if (meta < 6) {
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeInventory)
          return (TMultiPart)new TransferNodePartInventory(meta, (TileEntityTransferNodeInventory)world.getTileEntity(pos.x, pos.y, pos.z)); 
      } else if (meta < 12) {
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeLiquid)
          return (TMultiPart)new TransferNodePartLiquid(meta, (TileEntityTransferNodeLiquid)world.getTileEntity(pos.x, pos.y, pos.z)); 
      } else if (meta == 12) {
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeEnergy)
          return (TMultiPart)new TransferNodePartEnergy(meta, (TileEntityTransferNodeEnergy)world.getTileEntity(pos.x, pos.y, pos.z)); 
      } else if (meta == 13 && 
        world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeHyperEnergy) {
        return (TMultiPart)new TransferNodePartHyperEnergy(meta, (TileEntityTransferNodeHyperEnergy)world.getTileEntity(pos.x, pos.y, pos.z));
      }  
    if (id == ExtraUtils.transferNodeRemote)
      if (meta < 6) {
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityRetrievalNodeInventory)
          return (TMultiPart)new TransferNodePartInventoryRemote(meta, (TileEntityRetrievalNodeInventory)world.getTileEntity(pos.x, pos.y, pos.z)); 
      } else if (meta < 12) {
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityRetrievalNodeLiquid)
          return (TMultiPart)new TransferNodePartLiquidRemote(meta, (TileEntityRetrievalNodeLiquid)world.getTileEntity(pos.x, pos.y, pos.z)); 
      } else if (meta == 12 && 
        world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityTransferNodeEnergy) {
        return (TMultiPart)new TransferNodePartEnergy(meta, (TileEntityTransferNodeEnergy)world.getTileEntity(pos.x, pos.y, pos.z));
      }  
    return null;
  }
  
  public TMultiPart createPart(String name, boolean client) {
    if (name.equals("extrautils:transfer_node_inv"))
      return (TMultiPart)new TransferNodePartInventory(); 
    if (name.equals("extrautils:transfer_node_liquid"))
      return (TMultiPart)new TransferNodePartLiquid(); 
    if (name.equals("extrautils:transfer_node_energy"))
      return (TMultiPart)new TransferNodePartEnergy(); 
    if (name.equals("extrautils:transfer_node_inv_remote"))
      return (TMultiPart)new TransferNodePartInventoryRemote(); 
    if (name.equals("extrautils:transfer_node_liquid_remote"))
      return (TMultiPart)new TransferNodePartLiquidRemote(); 
    if (name.equals("extrautils:transfer_node_energy_hyper"))
      return (TMultiPart)new TransferNodePartHyperEnergy(); 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\RegisterTransferNodeParts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */