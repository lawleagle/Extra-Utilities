package com.rwtema.extrautils.tileentity.generators;

public class TileEntityGeneratorSolar extends TileEntityGeneratorSimpleSolar {
  public int curLevel = -1;
  
  public String getBlurb(double coolDown, double energy) {
    if (isPowered()) {
      if (coolDown == 0.0D)
        return "Transmitting: Deactivate redstone signal to resume charging"; 
      return "Time Remaining until transmittion starts:\n" + getCoolDownString(coolDown);
    } 
    if (coolDown == 0.0D)
      return "\n\n\nCharging: Apply redstone signal to transmit energy"; 
    return "PowerLevel:\n" + energy + "\n\nCharging: Apply redstone signal to transmit energy";
  }
  
  public int transferLimit() {
    return 20 + (int)(genLevel() / 2.0D);
  }
  
  public int getMaxCoolDown() {
    return 200;
  }
  
  public double genLevel() {
    if (this.curLevel == -1 || this.worldObj.getTotalWorldTime() % 20L == 0L)
      this.curLevel = genLevelForTime(); 
    if (this.curLevel == 0)
      return 0.0D; 
    if (!this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord))
      return 0.0D; 
    if (isPowered())
      return 0.0D; 
    return this.curLevel;
  }
  
  public boolean processInput() {
    if (genLevel() > 0.0D) {
      this.coolDown = getMaxCoolDown();
      return true;
    } 
    return false;
  }
  
  public boolean shouldTransmit() {
    return (this.coolDown == 0.0D);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\generators\TileEntityGeneratorSolar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */