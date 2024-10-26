package com.rwtema.extrautils.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockMultiBlockSelection extends BlockMultiBlock {
  public Box boundsOveride = null;
  
  public BlockMultiBlockSelection(Material xMaterial) {
    super(xMaterial);
  }
  
  public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
    MovingObjectPosition result = null;
    for (Box box : getWorldModel((IBlockAccess)world, x, y, z)) {
      this.boundsOveride = box;
      MovingObjectPosition r = super.collisionRayTrace(world, x, y, z, start, end);
      if (r != null && (
        result == null || start.distanceTo(r.hitVec) < start.distanceTo(result.hitVec)))
        result = r; 
    } 
    this.boundsOveride = null;
    return result;
  }
  
  public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int x, int y, int z) {
    Box bounds;
    if (this.boundsOveride != null) {
      bounds = this.boundsOveride;
    } else {
      bounds = BoxModel.boundingBox(getWorldModel(par1IBlockAccess, x, y, z));
    } 
    if (bounds != null)
      setBlockBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ); 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockMultiBlockSelection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */