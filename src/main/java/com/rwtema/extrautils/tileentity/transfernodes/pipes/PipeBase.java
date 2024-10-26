package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeBase implements IPipe, IPipeCosmetic {
  public String type;
  
  public PipeBase(String type) {
    this.type = type;
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    ArrayList<ForgeDirection> results = new ArrayList<ForgeDirection>();
    for (ForgeDirection d : TNHelper.randomDirections()) {
      if (d != dir.getOpposite() && TNHelper.canOutput(world, x, y, z, d) && 
        TNHelper.canInput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite()))
        results.add(d); 
    } 
    return results;
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    IPipe pipe = TNHelper.getPipe(world, x, y, z);
    if (pipe == null)
      return true; 
    boolean advance = true;
    for (ForgeDirection d : TNHelper.randomDirections()) {
      if (pipe.shouldConnectToTile(world, x, y, z, d) && 
        !buffer.transfer(world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), d.getOpposite(), pipe, x, y, z, dir))
        advance = false; 
    } 
    return advance;
  }
  
  public boolean canInput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return true;
  }
  
  public boolean canOutput(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return true;
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    return -1;
  }
  
  public IInventory getFilterInventory(IBlockAccess world, int x, int y, int z) {
    return null;
  }
  
  public boolean shouldConnectToTile(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return TNHelper.isValidTileEntity(world, x, y, z, dir);
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes;
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    if (blocked)
      return BlockTransferPipe.pipes_1way; 
    return BlockTransferPipe.pipes;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes;
  }
  
  public IIcon socketTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_nozzle;
  }
  
  public String getPipeType() {
    return this.type;
  }
  
  public float baseSize() {
    return 0.125F;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */