package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeNonInserting extends PipeBase {
  public PipeNonInserting() {
    super("NonInserting");
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    return true;
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return false;
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes_noninserting;
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    return BlockTransferPipe.pipes_noninserting;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_noninserting;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeNonInserting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */