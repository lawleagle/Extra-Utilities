package com.rwtema.extrautils.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconConnectedTextureFlipped extends IconConnectedTexture {
  public IconConnectedTextureFlipped(IconConnectedTexture icon) {
    super(icon.icons[3], icon.icons[1], icon.icons[2], icon.icons[0], icon.icons[4]);
  }
  
  public float getMinV() {
    return super.getMaxV();
  }
  
  public float getMaxV() {
    return super.getMinV();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\block\IconConnectedTextureFlipped.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */