package com.rwtema.extrautils.tileentity.transfernodes;

public class TileEntityTransferNodeHyperEnergy extends TileEntityTransferNodeEnergy {
  public TileEntityTransferNodeHyperEnergy() {
    int capacity = 1000000;
    this.powerHandler.setCapacity(1000000);
    this.powerHandler.setMaxExtract(1000000);
    this.powerHandler.setMaxReceive(1000000);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\TileEntityTransferNodeHyperEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */