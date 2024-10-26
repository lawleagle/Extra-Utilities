package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class StdPipes {
  public static IPipe[] pipe = new IPipe[17];
  
  static {
    pipe[0] = new PipeStandard();
    for (int i = 0; i < 6; i++)
      pipe[i + 1] = new PipeDirectional(ForgeDirection.getOrientation(i)); 
    pipe[7] = new PipeNonInserting();
    pipe[8] = new PipeSorting();
    pipe[9] = new PipeFilter();
    pipe[10] = new PipeRationing();
    pipe[11] = new PipeEnergy();
    pipe[12] = new PipeCrossOver();
    pipe[13] = new PipeModSorting();
    pipe[14] = new PipeEnergyExtract();
    pipe[15] = new PipeEOF();
    pipe[16] = new PipeHyperRationing();
  }
  
  public static IPipe getPipeType(int type) {
    if (type < 0 || type >= pipe.length)
      return null; 
    return pipe[type];
  }
  
  public static int getNextPipeType(IBlockAccess world, int x, int y, int z, int metadata) {
    if ((metadata >= 8 && metadata < 15) || metadata > 15)
      return metadata; 
    if (metadata == 7)
      return 0; 
    if (metadata == 15)
      return 7; 
    if (metadata == 6)
      return 15; 
    metadata = (metadata + 1) % 8;
    IPipe pipe = TNHelper.getPipe(world, x, y, z);
    if (pipe == null)
      return metadata; 
    int numNeighbors = 0;
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      if (TNHelper.canInput(world, x, y, z, dir) && TNHelper.canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite()))
        numNeighbors++; 
    } 
    if (numNeighbors <= 1) {
      if (metadata >= 1 && metadata <= 6)
        return 15; 
    } else {
      ForgeDirection dir = ForgeDirection.getOrientation(metadata - 1);
      while (metadata >= 1 && metadata <= 6 && (!TNHelper.canInput(world, x, y, z, dir) || !TNHelper.canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite()))) {
        metadata++;
        dir = ForgeDirection.getOrientation(metadata - 1);
      } 
      if (metadata == 7)
        return 15; 
    } 
    return metadata;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\StdPipes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */