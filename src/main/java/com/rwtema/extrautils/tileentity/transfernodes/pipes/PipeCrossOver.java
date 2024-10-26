package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.util.ArrayList;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeCrossOver extends PipeBase {
  public PipeCrossOver() {
    super("Crossover");
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes_xover;
  }
  
  public float baseSize() {
    return 0.1875F;
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    ArrayList<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
    if (TNHelper.canOutput(world, x, y, z, dir) && 
      TNHelper.canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite()))
      dirs.add(dir); 
    return dirs;
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    boolean advance = true;
    IPipe pipe = TNHelper.getPipe(world, x, y, z);
    if (pipe == null)
      return true; 
    if (pipe.shouldConnectToTile(world, x, y, z, dir) && 
      !buffer.transfer(world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ), dir.getOpposite(), pipe, x, y, z, dir))
      advance = false; 
    return advance;
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    if (dir == ForgeDirection.UNKNOWN)
      return false; 
    IPipe pipe = TNHelper.getPipe(world, x + (dir.getOpposite()).offsetX, y + (dir.getOpposite()).offsetY, z + (dir.getOpposite()).offsetZ);
    if (pipe == null)
      return false; 
    if (pipe.getPipeType().equals(getPipeType()))
      return true; 
    return pipe.canOutput(world, x + (dir.getOpposite()).offsetX, y + (dir.getOpposite()).offsetY, z + (dir.getOpposite()).offsetZ, dir);
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    if (dir == ForgeDirection.UNKNOWN)
      return false; 
    IPipe pipe = TNHelper.getPipe(world, x + (dir.getOpposite()).offsetX, y + (dir.getOpposite()).offsetY, z + (dir.getOpposite()).offsetZ);
    if (pipe == null)
      return super.shouldConnectToTile(world, x, y, z, dir.getOpposite()); 
    if (pipe.getPipeType().equals(getPipeType()))
      return true; 
    return pipe.canInput(world, x + (dir.getOpposite()).offsetX, y + (dir.getOpposite()).offsetY, z + (dir.getOpposite()).offsetZ, dir);
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (TNHelper.canOutput(world, x + (dir.getOpposite()).offsetX, y + (dir.getOpposite()).offsetY, z + (dir.getOpposite()).offsetZ, dir) && TNHelper.isValidTileEntity(world, x, y, z, dir));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeCrossOver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */