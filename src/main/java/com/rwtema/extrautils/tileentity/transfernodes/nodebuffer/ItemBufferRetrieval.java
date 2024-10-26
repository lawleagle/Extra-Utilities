package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBufferRetrieval extends ItemBuffer {
  public boolean shouldSearch() {
    return (this.item == null || this.item.stackSize < this.item.getMaxStackSize());
  }
  
  public boolean transferBack(TileEntity tile, ForgeDirection side, IPipe insertingPipe, int x, int y, int z) {
    return super.transfer(tile, side, StdPipes.getPipeType(0), x, y, z, side);
  }
  
  public boolean transfer(TileEntity tile, ForgeDirection side, IPipe insertingPipe, int x, int y, int z, ForgeDirection travelDir) {
    if (tile instanceof IInventory && side != ForgeDirection.UNKNOWN && this.node instanceof ISidedInventory) {
      boolean nonSided = !(tile instanceof ISidedInventory);
      IInventory nodeInv = (IInventory)this.node;
      IInventory inv = TNHelper.getInventory(tile);
      boolean extracted = false;
      for (int i : InvHelper.getSlots(inv, side.ordinal())) {
        if (inv.getStackInSlot(i) != null && ((
          this.item == null && nodeInv.isItemValidForSlot(0, inv.getStackInSlot(i))) || InvHelper.canStack(this.item, inv.getStackInSlot(i)))) {
          if (extracted)
            return false; 
          if (this.item == null || this.item.stackSize < this.item.getMaxStackSize()) {
            int n = nodeInv.getInventoryStackLimit();
            if (this.node.getNode().upgradeNo(3) == 0)
              n = 1; 
            if (this.item != null)
              n = Math.min(n, this.item.getMaxStackSize() - this.item.stackSize); 
            if (n > 0 && (nonSided || ((ISidedInventory)inv).canExtractItem(i, inv.getStackInSlot(i), side.ordinal()))) {
              ItemStack r = inv.decrStackSize(i, n);
              if (r != null && r.stackSize > 0) {
                if (this.item == null) {
                  this.item = r;
                } else {
                  this.item.stackSize += r.stackSize;
                } 
                inv.markDirty();
                if (n == r.stackSize && inv.getStackInSlot(i) != null)
                  return false; 
                extracted = true;
              } 
            } 
          } 
        } 
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\ItemBufferRetrieval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */