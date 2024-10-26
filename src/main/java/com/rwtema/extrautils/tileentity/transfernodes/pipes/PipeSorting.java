package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeSorting extends PipeBase {
  public PipeSorting() {
    super("Sorting");
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    if (buffer.getBuffer() instanceof ItemStack && 
      dest instanceof IInventory) {
      ItemStack item = (ItemStack)buffer.getBuffer();
      IInventory inv = TNHelper.getInventory(dest);
      boolean empty = true;
      for (int i : InvHelper.getSlots(inv, side.ordinal())) {
        if (inv.getStackInSlot(i) != null) {
          empty = false;
          if (InvHelper.sameType(inv.getStackInSlot(i), item))
            return -1; 
        } 
      } 
      return empty ? -1 : 0;
    } 
    return -1;
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes_grouping;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_grouping;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeSorting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */