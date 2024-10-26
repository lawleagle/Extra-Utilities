package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import net.minecraft.block.Block;

public class TransferNodePartInventoryRemote extends TransferNodePartInventory {
  public TransferNodePartInventoryRemote() {
    super((TileEntityTransferNode)new TileEntityRetrievalNodeInventory());
  }
  
  public TransferNodePartInventoryRemote(int meta, TileEntityRetrievalNodeInventory node) {
    super(meta, (TileEntityTransferNodeInventory)node);
  }
  
  public String getType() {
    return "extrautils:transfer_node_inv_remote";
  }
  
  public Block getBlock() {
    return ExtraUtils.transferNodeRemote;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePartInventoryRemote.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */