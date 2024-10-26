package com.rwtema.extrautils.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class IconMultiIcon implements IIcon {
  public int grid_x;
  
  public int grid_y;
  
  public int grid_w;
  
  public int grid_h;
  
  public IIcon icon;
  
  public IconMultiIcon(IIcon icon, int grid_x, int grid_y, int grid_w, int grid_h) {
    this.grid_x = grid_x;
    this.grid_y = grid_y;
    this.grid_w = grid_w;
    this.grid_h = grid_h;
    this.icon = icon;
  }
  
  @SideOnly(Side.CLIENT)
  public int getIconWidth() {
    return this.icon.getIconWidth() / this.grid_w;
  }
  
  @SideOnly(Side.CLIENT)
  public int getIconHeight() {
    return this.icon.getIconHeight() / this.grid_h;
  }
  
  @SideOnly(Side.CLIENT)
  public float getMinU() {
    return this.icon.getInterpolatedU(this.grid_x / this.grid_w * 16.0D);
  }
  
  @SideOnly(Side.CLIENT)
  public float getMaxU() {
    return this.icon.getInterpolatedU((this.grid_x + 1) / this.grid_w * 16.0D);
  }
  
  @SideOnly(Side.CLIENT)
  public float getInterpolatedU(double par1) {
    float f = getMaxU() - getMinU();
    return getMinU() + f * (float)par1 / 16.0F;
  }
  
  @SideOnly(Side.CLIENT)
  public float getMinV() {
    return this.icon.getInterpolatedV(this.grid_y / this.grid_h * 16.0D);
  }
  
  @SideOnly(Side.CLIENT)
  public float getMaxV() {
    return this.icon.getInterpolatedV((this.grid_y + 1) / this.grid_h * 16.0D);
  }
  
  @SideOnly(Side.CLIENT)
  public float getInterpolatedV(double par1) {
    float f = getMaxV() - getMinV();
    return getMinV() + f * (float)par1 / 16.0F;
  }
  
  @SideOnly(Side.CLIENT)
  public String getIconName() {
    return this.icon.getIconName();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\IconMultiIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */