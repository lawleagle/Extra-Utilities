package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.item.ItemStack;

public class Frequency {
  public final String freq;
  
  public final String owner;
  
  public Frequency(String freq, String owner) {
    this.freq = freq;
    this.owner = owner;
  }
  
  public Frequency(ItemStack item) {
    this(XUHelper.getAnvilName(item), XUHelper.getPlayerOwner(item));
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    Frequency that = (Frequency)o;
    if (!this.freq.equals(that.freq))
      return false; 
    if (!this.owner.equals(that.owner))
      return false; 
    return true;
  }
  
  public int hashCode() {
    int result = this.freq.hashCode();
    result = 31 * result + this.owner.hashCode();
    return result;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\Frequency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */