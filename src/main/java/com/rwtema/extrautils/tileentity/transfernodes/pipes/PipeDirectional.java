package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.util.ArrayList;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeDirectional extends PipeBase {
  ForgeDirection outDir;
  
  public PipeDirectional(ForgeDirection outDir) {
    super("Directional_" + outDir);
    this.outDir = outDir;
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    ArrayList<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
    if (canOutput(world, x, y, z, this.outDir) && 
      TNHelper.canInput(world, x + this.outDir.offsetX, y + this.outDir.offsetY, z + this.outDir.offsetZ, this.outDir.getOpposite())) {
      dirs.add(this.outDir);
      return dirs;
    } 
    return dirs;
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (dir == this.outDir);
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return (dir == this.outDir) ? BlockTransferPipe.pipes : BlockTransferPipe.pipes_oneway;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeDirectional.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */