package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeEnergy extends PipeBase {
  public PipeEnergy() {
    super("Energy");
  }
  
  public PipeEnergy(String type) {
    super(type);
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    if (TNHelper.getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null)
      return false; 
    TileEntity tile = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
    return TNHelper.isRFEnergy(tile, dir.getOpposite());
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    IPipe pipe = TNHelper.getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
    return (pipe != null && pipe.getPipeType().startsWith("Energy"));
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_nozzle_energy;
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes_energy;
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return BlockTransferPipe.pipes_energy;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_energy;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */