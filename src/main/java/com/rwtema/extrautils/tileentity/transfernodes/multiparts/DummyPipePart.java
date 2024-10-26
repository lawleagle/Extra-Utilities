package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.lib.vec.Cuboid6;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.JCuboidPart;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import java.util.Arrays;

public class DummyPipePart extends JCuboidPart implements JNormalOcclusion, ISidedHollowConnect {
  public int dir;
  
  public float h;
  
  public DummyPipePart(int dir, float h) {
    this.dir = dir;
    this.h = h;
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return (npart instanceof com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe || NormalOcclusionTest.apply(this, npart));
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    return Arrays.asList(new Cuboid6[] { getBounds() });
  }
  
  public Cuboid6 getBounds() {
    switch (this.dir) {
      case 0:
        return new Cuboid6(0.375D, 0.0D, 0.375D, 0.625D, this.h, 0.625D);
      case 1:
        return new Cuboid6(0.375D, (1.0F - this.h), 0.375D, 0.625D, 1.0D, 0.625D);
      case 2:
        return new Cuboid6(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, this.h);
      case 3:
        return new Cuboid6(0.375D, 0.375D, (1.0F - this.h), 0.625D, 0.625D, 1.0D);
      case 4:
        return new Cuboid6(0.0D, 0.375D, 0.375D, this.h, 0.625D, 0.625D);
      case 5:
        return new Cuboid6((1.0F - this.h), 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);
    } 
    return new Cuboid6(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  }
  
  public String getType() {
    return "dummyPipe";
  }
  
  public int getHollowSize(int side) {
    return 2;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\multiparts\DummyPipePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */