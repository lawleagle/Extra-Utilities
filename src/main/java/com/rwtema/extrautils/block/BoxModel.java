package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtilsMod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

public class BoxModel extends ArrayList<Box> {
  public int invModelRotate = 90;
  
  public String label = "";
  
  public BoxModel() {}
  
  public BoxModel(Box newBox) {
    add(newBox);
  }
  
  public BoxModel(float par1, float par3, float par5, float par7, float par9, float par11) {
    add(new Box(par1, par3, par5, par7, par9, par11));
  }
  
  public static BoxModel newStandardBlock() {
    Box t = new Box(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    t.renderAsNormalBlock = true;
    return new BoxModel(t);
  }
  
  public static BoxModel hollowBox(float minX, float minY, float minZ, float holeMinX, float holeMinZ, float holeMaxX, float holeMaxZ, float maxX, float maxY, float maxZ) {
    BoxModel t = new BoxModel();
    t.add(new Box(minX, minY, minZ, holeMinX, maxY, maxZ));
    t.add(new Box(holeMinX, minY, minZ, holeMaxX, maxY, holeMinZ));
    t.add(new Box(holeMinX, minY, holeMaxZ, holeMaxX, maxY, maxZ));
    t.add(new Box(holeMaxX, minY, minZ, maxX, maxY, maxZ));
    return t;
  }
  
  public static Box boundingBox(List<Box> models) {
    if (models == null)
      return null; 
    if (models.size() == 0)
      return null; 
    Box bounds = ((Box)models.get(0)).copy();
    for (int i = 1; i < models.size(); i++)
      bounds.setBounds(Math.min(bounds.minX, ((Box)models.get(i)).minX), Math.min(bounds.minY, ((Box)models.get(i)).minY), Math.min(bounds.minZ, ((Box)models.get(i)).minZ), Math.max(bounds.maxX, ((Box)models.get(i)).maxX), Math.max(bounds.maxY, ((Box)models.get(i)).maxY), Math.max(bounds.maxZ, ((Box)models.get(i)).maxZ)); 
    return bounds;
  }
  
  public Box addBoxI(int par1, int par3, int par5, int par7, int par9, int par11) {
    return addBox("", par1 / 16.0F, par3 / 16.0F, par5 / 16.0F, par7 / 16.0F, par9 / 16.0F, par11 / 16.0F);
  }
  
  public Box addBox(float par1, float par3, float par5, float par7, float par9, float par11) {
    return addBox("", par1, par3, par5, par7, par9, par11);
  }
  
  public Box addBox(String l, float par1, float par3, float par5, float par7, float par9, float par11) {
    Box b = new Box(l, par1, par3, par5, par7, par9, par11);
    add(b);
    return b;
  }
  
  public BoxModel rotateToSide(ForgeDirection dir) {
    for (Box box : this)
      box.rotateToSide(dir); 
    return this;
  }
  
  public BoxModel rotateY(int numRotations) {
    for (Box box : this)
      box.rotateY(numRotations); 
    return this;
  }
  
  public BoxModel offset(float x, float y, float z) {
    for (Box box : this)
      box.offset(x, y, z); 
    return this;
  }
  
  public BoxModel setColor(int col) {
    for (Box box : this)
      box.setColor(col); 
    return this;
  }
  
  public BoxModel addYRotations() {
    addAll(copy().rotateY(1));
    addAll(copy().rotateY(2));
    return this;
  }
  
  public Box boundingBox() {
    return boundingBox(this);
  }
  
  public BoxModel copy() {
    BoxModel newModel = new BoxModel();
    for (int i = 0; i < size(); i++)
      newModel.add(get(i).copy()); 
    return newModel;
  }
  
  public BoxModel fillIcons(Block block, int meta) {
    if (ExtraUtilsMod.proxy.isClientSideAvailable())
      for (Box b : this)
        b.fillIcons(block, meta);  
    return this;
  }
  
  public BoxModel rotateToSideTex(ForgeDirection dir) {
    for (Box b : this)
      b.rotateToSideTex(dir); 
    return this;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BoxModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */