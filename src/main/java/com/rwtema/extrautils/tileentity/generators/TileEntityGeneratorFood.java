package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityGeneratorFood extends TileEntityGeneratorFurnace {
  double curLevel = 0.0D;
  
  public int transferLimit() {
    return 160;
  }
  
  public double genLevel() {
    return this.curLevel;
  }
  
  public double getGenLevelForStack(ItemStack item) {
    if (item != null) {
      if (item.getItem() instanceof ItemFood)
        return scale(((ItemFood)item.getItem()).func_150905_g(item), 8.0D) * 4.0D; 
      if (item.getItem() == Items.cake)
        return 64.0D; 
    } 
    return 0.0D;
  }
  
  public double scale(double a, double h) {
    if (a < h)
      return a; 
    double b = 0.0D;
    double s = 1.0D;
    double i;
    for (i = 0.0D; i <= a; i += h) {
      double da = Math.min(h, a - i);
      b += da * s;
      s *= 0.75D;
    } 
    return b;
  }
  
  public void adjustGenLevel(ItemStack item) {
    this.curLevel = getGenLevelForStack(item);
  }
  
  public double getFuelBurn(ItemStack item) {
    if (item != null) {
      if (item.getItem() instanceof ItemFood) {
        if (((ItemFood)item.getItem()).func_150905_g(item) > 0)
          return Math.ceil(scale(((ItemFood)item.getItem()).func_150906_h(item), 0.8D) * 1800.0D); 
        return 0.0D;
      } 
      if (item.getItem() == Items.cake)
        return 1500.0D; 
    } 
    return 0.0D;
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    super.readFromNBT(nbt);
    this.curLevel = nbt.getDouble("curLevel");
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    nbt.setDouble("curLevel", this.curLevel);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorFood.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */