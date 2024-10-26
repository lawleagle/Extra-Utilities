package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBuffer implements INodeBuffer {
  public INode node;
  
  public ItemStack item = null;
  
  public boolean transfer(TileEntity tile, ForgeDirection side, IPipe insertingPipe, int x, int y, int z, ForgeDirection travelDir) {
    if (this.item != null && tile instanceof IInventory && side != ForgeDirection.UNKNOWN) {
      boolean nonSided = !(tile instanceof ISidedInventory);
      IInventory inv = TNHelper.getInventory(tile);
      int empty = -1;
      int filter = -1;
      int maxStack = Math.min(this.item.getMaxStackSize(), inv.getInventoryStackLimit());
      if (insertingPipe != null)
        filter = insertingPipe.limitTransfer(tile, side, this); 
      if (filter < 0) {
        filter = maxStack;
      } else if (filter == 0) {
        return true;
      } 
      boolean flag = true;
      for (int i : InvHelper.getSlots(inv, side.ordinal())) {
        if (inv.getStackInSlot(i) == null) {
          if (empty == -1 && inv.isItemValidForSlot(i, this.item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, this.item, side.ordinal())))
            empty = i; 
        } else if (InvHelper.canStack(this.item, inv.getStackInSlot(i)) && inv.isItemValidForSlot(i, this.item) && (nonSided || ((ISidedInventory)inv).canInsertItem(i, this.item, side.ordinal()))) {
          ItemStack dest = inv.getStackInSlot(i);
          if (maxStack - dest.stackSize > 0 && filter > 0) {
            int l = Math.min(Math.min(this.item.stackSize, maxStack - dest.stackSize), filter);
            if (l > 0) {
              dest.stackSize += l;
              this.item.stackSize -= l;
              filter -= l;
              flag = true;
              if (this.item.stackSize <= 0) {
                this.item = null;
                break;
              } 
              if (filter <= 0)
                break; 
            } 
          } 
        } 
      } 
      if (filter > 0 && this.item != null && empty != -1 && inv.isItemValidForSlot(empty, this.item) && (nonSided || ((ISidedInventory)inv).canInsertItem(empty, this.item, side.ordinal()))) {
        if (filter < this.item.stackSize) {
          inv.setInventorySlotContents(empty, this.item.splitStack(filter));
        } else {
          inv.setInventorySlotContents(empty, this.item);
          this.item = null;
        } 
        flag = true;
      } 
      if (flag)
        inv.markDirty(); 
    } 
    return true;
  }
  
  public ItemStack getBuffer() {
    return this.item;
  }
  
  public String getBufferType() {
    return "items";
  }
  
  public void setBuffer(Object buffer) {
    if (buffer == null || buffer instanceof ItemStack)
      this.item = (ItemStack)buffer; 
  }
  
  public void readFromNBT(NBTTagCompound tags) {
    if (tags.hasKey("bufferItem")) {
      this.item = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("bufferItem"));
    } else {
      this.item = null;
    } 
  }
  
  public void writeToNBT(NBTTagCompound tags) {
    if (this.item != null) {
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      this.item.writeToNBT(nbttagcompound1);
      tags.setTag("bufferItem", (NBTBase)nbttagcompound1);
    } 
  }
  
  public boolean isEmpty() {
    if (this.item == null)
      return true; 
    if (this.item.stackSize == 0) {
      this.item = null;
      return true;
    } 
    return false;
  }
  
  public void setNode(INode node) {
    this.node = node;
  }
  
  public INode getNode() {
    return this.node;
  }
  
  public boolean transferTo(INodeBuffer receptor, int no) {
    ItemStack newbuffer;
    if (this.item == null || this.item.stackSize == 0 || !getBufferType().equals(receptor.getBufferType()))
      return false; 
    ItemStack buffer = (ItemStack)receptor.getBuffer();
    if (buffer == null) {
      newbuffer = this.item.copy();
      newbuffer.stackSize = 0;
    } else {
      newbuffer = buffer.copy();
    } 
    if (receptor.getNode() instanceof IInventory && (
      (IInventory)receptor.getNode()).isItemValidForSlot(0, this.item))
      return false; 
    if (XUHelper.canItemsStack(this.item, newbuffer)) {
      int m = newbuffer.getMaxStackSize() - newbuffer.stackSize;
      if (no < m)
        m = no; 
      if (this.item.stackSize < m)
        m = this.item.stackSize; 
      if (m <= 0)
        return false; 
      newbuffer.stackSize += m;
      receptor.setBuffer(newbuffer);
      receptor.markDirty();
      this.item.stackSize -= m;
      if (this.item.stackSize == 0)
        this.item = null; 
    } 
    return true;
  }
  
  public Object recieve(Object a) {
    if (!(a instanceof ItemStack))
      return a; 
    ItemStack i = (ItemStack)a;
    if (this.item == null) {
      this.item = i;
      return null;
    } 
    if (XUHelper.canItemsStack(i, this.item))
      int m = this.item.getMaxStackSize() - this.item.stackSize; 
    return i;
  }
  
  public void markDirty() {
    this.node.bufferChanged();
  }
  
  public boolean shouldSearch() {
    return !isEmpty();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\nodebuffer\ItemBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */