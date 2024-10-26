package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.util.ArrayList;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeEOF extends PipeBase {
  public PipeEOF() {
    super("eof");
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return false;
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return new ArrayList<ForgeDirection>();
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return BlockTransferPipe.pipes_oneway;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeEOF.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */