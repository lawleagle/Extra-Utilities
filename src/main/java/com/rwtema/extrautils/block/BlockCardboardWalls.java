package com.rwtema.extrautils.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCardboardWalls extends BlockMultiBlock {
  public BlockCardboardWalls() {
    super(Material.cloth);
    setBlockName("extrautils:cardboardwall");
    setBlockTextureName("extrautils:cardboard");
  }
  
  public void prepareForRender(String label) {}
  
  public boolean isCardBoard(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return (this == world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ));
  }
  
  public boolean isSide(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
    return world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite(), false);
  }
  
  public BoxModel getWorldModel(IBlockAccess world, int x, int y, int z) {
    BoxModel model = new BoxModel();
    return model;
  }
  
  public BoxModel getInventoryModel(int metadata) {
    BoxModel box = new BoxModel();
    box.addBoxI(7, 0, 9, 7, 16, 9);
    return box;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\BlockCardboardWalls.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */