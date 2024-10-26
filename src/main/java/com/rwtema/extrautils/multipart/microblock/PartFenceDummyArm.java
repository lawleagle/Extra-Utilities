package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.JCuboidPart;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import java.util.ArrayList;

public class PartFenceDummyArm extends JCuboidPart implements JNormalOcclusion {
  public static final PartFenceDummyArm[] dummyArms = new PartFenceDummyArm[] { null, null, new PartFenceDummyArm(2), new PartFenceDummyArm(3), new PartFenceDummyArm(4), new PartFenceDummyArm(5) };
  
  protected final ArrayList<Cuboid6> boxes = new ArrayList<Cuboid6>();
  
  public int dir;
  
  public PartFenceDummyArm() {}
  
  public PartFenceDummyArm(int dir) {
    this.dir = dir;
    this.boxes.add(PartFence.renderCuboids1[dir]);
    this.boxes.add(PartFence.renderCuboids2[dir]);
  }
  
  public Cuboid6 getBounds() {
    return PartFence.partCuboids[this.dir];
  }
  
  public String getType() {
    return "extrautils:fence_dummy_part_should_never_actually_be_created_(if_it_is,_it_is_bug)";
  }
  
  public boolean occlusionTest(TMultiPart npart) {
    return NormalOcclusionTest.apply(this, npart);
  }
  
  public Iterable<Cuboid6> getOcclusionBoxes() {
    return this.boxes;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\multipart\microblock\PartFenceDummyArm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */