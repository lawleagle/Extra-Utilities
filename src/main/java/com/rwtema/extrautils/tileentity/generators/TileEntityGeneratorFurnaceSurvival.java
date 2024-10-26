package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.item.ItemStack;

public class TileEntityGeneratorFurnaceSurvival extends TileEntityGeneratorFurnace {
  public int transferLimit() {
    return 160;
  }
  
  public double genLevel() {
    return 5.0D;
  }
  
  public double getFuelBurn(ItemStack item) {
    return (10 * TileEntityGenerator.getFurnaceBurnTime(item));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorFurnaceSurvival.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */