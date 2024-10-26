package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPipeCosmetic {
  IIcon baseTexture();
  
  IIcon pipeTexture(ForgeDirection paramForgeDirection, boolean paramBoolean);
  
  IIcon invPipeTexture(ForgeDirection paramForgeDirection);
  
  IIcon socketTexture(ForgeDirection paramForgeDirection);
  
  float baseSize();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\transfernodes\pipes\IPipeCosmetic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */