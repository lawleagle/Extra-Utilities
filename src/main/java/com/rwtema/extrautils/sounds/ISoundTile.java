package com.rwtema.extrautils.sounds;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public interface ISoundTile {
  boolean shouldSoundPlay();
  
  ResourceLocation getSound();
  
  TileEntity getTile();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\sounds\ISoundTile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */