package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeHyperRationing extends PipeBase {
  public PipeHyperRationing() {
    super("HyperRationing");
  }
  
  public int limitTransfer(TileEntity dest, ForgeDirection side, INodeBuffer buffer) {
    if (buffer.getBuffer() instanceof ItemStack && dest instanceof IInventory) {
      ItemStack item = (ItemStack)buffer.getBuffer();
      IInventory inv = (IInventory)dest;
      int n = 1;
      for (int i : InvHelper.getSlots(inv, side.ordinal())) {
        if (inv.getStackInSlot(i) != null && InvHelper.canStack(inv.getStackInSlot(i), item)) {
          n -= (inv.getStackInSlot(i)).stackSize;
          if (n <= 0)
            return 0; 
        } 
      } 
      return (n < 0) ? 0 : n;
    } 
    return -1;
  }
  
  public IIcon baseTexture() {
    return BlockTransferPipe.pipes_hyperrationing;
  }
  
  public IIcon invPipeTexture(ForgeDirection dir) {
    return BlockTransferPipe.pipes_hyperrationing;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\PipeHyperRationing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */