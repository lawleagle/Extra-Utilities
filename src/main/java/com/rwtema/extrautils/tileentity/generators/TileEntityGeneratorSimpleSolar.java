package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.util.MathHelper;

public class TileEntityGeneratorSimpleSolar extends TileEntityGenerator {
  public int curLevel = -1;
  
  public String getBlurb(double coolDown, double energy) {
    return "PowerLevel:\n" + energy;
  }
  
  public int transferLimit() {
    return 400;
  }
  
  protected int genLevelForTime() {
    float f = this.worldObj.getCelestialAngleRadians(1.0F);
    if (f < 3.1415927F) {
      f += (0.0F - f) * 0.2F;
    } else {
      f += (6.2831855F - f) * 0.2F;
    } 
    int c = Math.min(40, Math.round(40.0F * MathHelper.cos(f)));
    return (c > 0) ? c : 0;
  }
  
  public int getMaxCoolDown() {
    return 240;
  }
  
  public double genLevel() {
    if (this.curLevel == -1 || this.worldObj.getTotalWorldTime() % 20L == 0L)
      this.curLevel = genLevelForTime(); 
    if (this.curLevel == 0)
      return 0.0D; 
    if (!this.worldObj.canBlockSeeTheSky(x(), y() + 1, z()))
      return 0.0D; 
    return this.curLevel;
  }
  
  public boolean processInput() {
    this.coolDown = getMaxCoolDown();
    return true;
  }
  
  public boolean shouldProcess() {
    return (genLevel() > 0.0D);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorSimpleSolar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */