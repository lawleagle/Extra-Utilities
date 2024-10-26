package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.EventHandlerServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAntiMobTorch extends TileEntity implements IAntiMobTorch {
  public static int[] getCoord(TileEntity tile) {
    return new int[] { (tile.getWorldObj()).provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord };
  }
  
  public float getHorizontalTorchRangeSquared() {
    if (getBlockType() instanceof com.rwtema.extrautils.block.BlockMagnumTorch)
      return 16384.0F; 
    if (getBlockType() instanceof com.rwtema.extrautils.block.BlockChandelier)
      return 256.0F; 
    return -1.0F;
  }
  
  public float getVerticalTorchRangeSquared() {
    if (getBlockType() instanceof com.rwtema.extrautils.block.BlockMagnumTorch)
      return 1024.0F; 
    if (getBlockType() instanceof com.rwtema.extrautils.block.BlockChandelier)
      return 256.0F; 
    return -1.0F;
  }
  
  public void invalidate() {
    EventHandlerServer.magnumTorchRegistry.remove(getCoord());
    super.invalidate();
  }
  
  public void onChunkUnload() {
    super.onChunkUnload();
    EventHandlerServer.magnumTorchRegistry.remove(getCoord());
  }
  
  public int[] getCoord() {
    return getCoord(this);
  }
  
  public void validate() {
    int[] myCoord = getCoord();
    for (int i = 0; i < EventHandlerServer.magnumTorchRegistry.size(); i++) {
      int[] coord = EventHandlerServer.magnumTorchRegistry.get(i);
      if (myCoord[0] == coord[0] && myCoord[1] == coord[1] && myCoord[2] == coord[2] && myCoord[3] == coord[3])
        return; 
    } 
    EventHandlerServer.magnumTorchRegistry.add(myCoord);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\TileEntityAntiMobTorch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */