package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.item.ItemStack;

public class TileEntityGeneratorFurnaceOverClocked extends TileEntityGeneratorFurnace {
  private final double multiplier = 10.0D;
  
  public int transferLimit() {
    return 100000;
  }
  
  public double genLevel() {
    return super.genLevel() * 10.0D;
  }
  
  public double getFuelBurn(ItemStack item) {
    return super.getFuelBurn(item) / 10.0D * 0.25D;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorFurnaceOverClocked.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */