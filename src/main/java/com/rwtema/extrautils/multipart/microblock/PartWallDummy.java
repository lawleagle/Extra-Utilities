package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.vec.Cuboid6;

public class PartWallDummy extends PartFenceDummyArm {
  public static final PartWallDummy[] dummyArms = new PartWallDummy[] { null, null, new PartWallDummy(2), new PartWallDummy(3), new PartWallDummy(4), new PartWallDummy(5) };
  
  public PartWallDummy(int dir) {
    this.boxes.add(PartWall.renderCuboids1[dir]);
  }
  
  public Cuboid6 getBounds() {
    return PartWall.partCuboids[this.dir];
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartWallDummy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */