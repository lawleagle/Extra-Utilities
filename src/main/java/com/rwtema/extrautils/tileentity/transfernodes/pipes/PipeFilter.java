package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeFilter extends PipeBase {
  public PipeFilter() {
    super("Filter");
  }
  
  public ArrayList<ForgeDirection> getOutputDirections(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    ArrayList<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
    if (TNHelper.getPipe(world, x, y, z) != null) {
      IInventory inv = TNHelper.getPipe(world, x, y, z).getFilterInventory(world, x, y, z);
      if (inv == null)
        return super.getOutputDirections(world, x, y, z, dir, buffer); 
      for (ForgeDirection d : TNHelper.randomDirections()) {
        if (d != dir.getOpposite() && TNHelper.canOutput(world, x, y, z, d) && 
          TNHelper.canInput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite())) {
          ItemStack filter = inv.getStackInSlot(d.ordinal());
          if (filter != null && 
            ItemNodeUpgrade.matchesFilterBuffer(buffer, filter))
            dirs.add(d); 
        } 
      } 
      for (ForgeDirection d : TNHelper.randomDirections()) {
        if (d != dir.getOpposite() && TNHelper.canOutput(world, x, y, z, d) && 
          TNHelper.canInput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite()) && 
          inv.getStackInSlot(d.ordinal()) == null)
          dirs.add(d); 
      } 
    } 
    return dirs;
  }
  
  public boolean transferItems(IBlockAccess world, int x, int y, int z, ForgeDirection dir, INodeBuffer buffer) {
    boolean advance = true;
    IPipe pipe = TNHelper.getPipe(world, x, y, z);
    if (pipe == null)
      return true; 
    IInventory inv = pipe.getFilterInventory(world, x, y, z);
    if (inv == null)
      return super.transferItems(world, x, y, z, dir, buffer); 
    for (ForgeDirection d : TNHelper.randomDirections()) {
      if (d.getOpposite() != dir) {
        ItemStack filter = inv.getStackInSlot(d.ordinal());
        if (filter != null && 
          pipe.shouldConnectToTile(world, x, y, z, d) && 
          ItemNodeUpgrade.matchesFilterBuffer(buffer, filter) && 
          !buffer.transfer(world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), d.getOpposite(), pipe, x, y, z, dir))
          advance = false; 
      } 
    } 
    for (ForgeDirection d : TNHelper.randomDirections()) {
      if (d.getOpposite() != dir) {
        ItemStack filter = inv.getStackInSlot(d.ordinal());
        if (filter == null && 
          pipe.shouldConnectToTile(world, x, y, z, d) && 
          !buffer.transfer(world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), d.getOpposite(), pipe, x, y, z, dir))
          advance = false; 
      } 
    } 
    return advance;
  }
  
  public IIcon pipeTexture(ForgeDirection dir, boolean blocked) {
    if (dir.ordinal() == 6)
      return BlockTransferPipe.pipes; 
    return BlockTransferPipe.pipes_diamond[dir.ordinal()];
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return pipeTexture(dir, false);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */