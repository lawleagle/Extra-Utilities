package com.rwtema.extrautils.item;

public class ColorHelper {
  public static int colorFromHue(long h) {
    float x = (float)(h % 60L) / 60.0F;
    int k = (int)(h % 360L) / 60;
    float r = 0.0F, g = 0.0F, b = 0.0F;
    switch (k) {
      case 0:
        r = 1.0F;
        g = x;
        break;
      case 1:
        r = 1.0F - x;
        g = 1.0F;
        break;
      case 2:
        g = 1.0F;
        b = x;
        break;
      case 3:
        b = 1.0F;
        g = 1.0F - x;
        break;
      case 4:
        b = 1.0F;
        r = x;
        break;
      case 5:
        r = 1.0F;
        b = 1.0F - x;
        break;
    } 
    return (int)(r * 255.0F) << 16 | (int)(g * 255.0F) << 8 | (int)(b * 255.0F);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\item\ColorHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */