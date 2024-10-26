package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeHyperEnergy;

public class TransferNodePartHyperEnergy extends TransferNodePartEnergy {
  public TransferNodePartHyperEnergy() {
    super((TileEntityTransferNode)new TileEntityTransferNodeHyperEnergy());
  }
  
  public TransferNodePartHyperEnergy(int meta, TileEntityTransferNodeHyperEnergy node) {
    super(meta, (TileEntityTransferNodeEnergy)node);
  }
  
  public String getType() {
    return "extrautils:transfer_node_energy_hyper";
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\TransferNodePartHyperEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */