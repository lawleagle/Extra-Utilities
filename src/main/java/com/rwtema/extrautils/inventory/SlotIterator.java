package com.rwtema.extrautils.inventory;

import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;

public class SlotIterator implements Iterator<Integer>, Iterable<Integer> {
  int[] sided = null;
  
  int i = 0;
  
  int size = 0;
  
  public SlotIterator(IInventory inv, int side) {
    if (inv instanceof ISidedInventory) {
      this.sided = ((ISidedInventory)inv).getAccessibleSlotsFromSide(side);
    } else {
      this.size = inv.getSizeInventory();
    } 
  }
  
  public boolean hasNext() {
    return (this.sided == null) ? ((this.i + 1 < this.size)) : ((this.i + 1 < this.sided.length));
  }
  
  public Integer next() {
    this.i++;
    return Integer.valueOf((this.sided == null) ? this.i : this.sided[this.i]);
  }
  
  public void remove() {}
  
  public Iterator<Integer> iterator() {
    return this;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\inventory\SlotIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */