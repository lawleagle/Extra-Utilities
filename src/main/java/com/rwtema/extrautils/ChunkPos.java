package com.rwtema.extrautils;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ChunkPos {
  public int x;
  
  public int y;
  
  public int z;
  
  public ChunkPos(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public ChunkPos(Vec3 p_i45364_1_) {
    this(MathHelper.floor_double(p_i45364_1_.xCoord), MathHelper.floor_double(p_i45364_1_.yCoord), MathHelper.floor_double(p_i45364_1_.zCoord));
  }
  
  public boolean equals(Object par1Obj) {
    if (!(par1Obj instanceof ChunkPos))
      return false; 
    ChunkPos pos = (ChunkPos)par1Obj;
    return (pos.x == this.x && pos.y == this.y && pos.z == this.z);
  }
  
  public int hashCode() {
    return this.x * 8976890 + this.y * 981131 + this.z;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\ChunkPos.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */