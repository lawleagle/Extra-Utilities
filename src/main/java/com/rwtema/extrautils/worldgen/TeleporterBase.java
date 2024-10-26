package com.rwtema.extrautils.worldgen;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class TeleporterBase extends Teleporter {
  protected final WorldServer worldServerInstance;
  
  public TeleporterBase(WorldServer p_i1963_1_) {
    super(p_i1963_1_);
    this.worldServerInstance = p_i1963_1_;
  }
  
  public TileEntity findPortalInChunk(double x, double z) {
    Chunk chunk = this.worldServerInstance.getChunkFromBlockCoords((int)x, (int)z);
    for (Object tile : chunk.chunkTileEntityMap.values()) {
      if (tile instanceof com.rwtema.extrautils.tileentity.TileEntityPortal)
        return (TileEntity)tile; 
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\worldgen\TeleporterBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */