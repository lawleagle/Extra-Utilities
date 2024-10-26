package com.rwtema.extrautils.item;

import net.minecraft.block.Block;

public class ItemFilingCabinet extends ItemBlockMetadata {
  public ItemFilingCabinet(Block par1) {
    super(par1);
  }
  
  public boolean getShareTag() {
    return false;
  }
  
  public boolean isDamageable() {
    return false;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ItemFilingCabinet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */