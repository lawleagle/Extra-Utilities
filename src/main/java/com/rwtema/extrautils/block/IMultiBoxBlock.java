package com.rwtema.extrautils.block;

import net.minecraft.world.IBlockAccess;

public interface IMultiBoxBlock {
  void prepareForRender(String paramString);
  
  BoxModel getWorldModel(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3);
  
  BoxModel getInventoryModel(int paramInt);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\IMultiBoxBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */