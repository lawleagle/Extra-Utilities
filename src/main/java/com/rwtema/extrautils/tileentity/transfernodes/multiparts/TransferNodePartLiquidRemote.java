package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import net.minecraft.block.Block;

public class TransferNodePartLiquidRemote extends TransferNodePartLiquid {
  public TransferNodePartLiquidRemote() {
    super((TileEntityTransferNode)new TileEntityRetrievalNodeLiquid());
  }
  
  public TransferNodePartLiquidRemote(int meta, TileEntityRetrievalNodeLiquid node) {
    super(meta, (TileEntityTransferNodeLiquid)node);
  }
  
  public String getType() {
    return "extrautils:transfer_node_liquid_remote";
  }
  
  public Block getBlock() {
    return ExtraUtils.transferNodeRemote;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePartLiquidRemote.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */