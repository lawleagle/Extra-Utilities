package com.rwtema.extrautils.texture;

import com.rwtema.extrautils.helper.XURandom;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.AnimationMetadataSection;

public class TextureUnstableLava extends TextureDerived {
  public TextureUnstableLava(String p_i1282_1_, String baseIcon) {
    super(p_i1282_1_, baseIcon, TextureDerived.TextureMapType.BLOCK);
  }
  
  public BufferedImage processImage(BufferedImage image, AnimationMetadataSection animationmetadatasection) {
    int w = image.getWidth();
    int h = image.getHeight();
    int[] aint = new int[h * w];
    int[] c = new int[256];
    image.getRGB(0, 0, w, h, aint, 0, w);
    int n1 = 0;
    for (int i = 0; i < aint.length; i++) {
      if (this.rgb.getAlpha(aint[i]) > 10) {
        aint[i] = getLuminosity(aint[i]);
        n1 = Math.max(n1, aint[i]);
      } else {
        aint[i] = 255;
      } 
    } 
    int v = h / w;
    for (int j = 0; j < aint.length; j++) {
      int x = j % w;
      int y = (j - x) / w % w;
      int sn = (j - x) / w / w;
      int p = 1;
      int lum = 256 + (x * 16 / w + y * 16 / w - 16) % 32 * 1 * 2;
      int t = 0;
      while ((lum >= 256 || lum < 240) && t < 100) {
        t++;
        if (lum >= 256)
          lum = 511 - lum; 
        if (lum < 240)
          lum = 480 - lum; 
      } 
      int col = aint[j];
      int l = col + n1;
      l = 255 - (255 - l) * 2;
      if (l < 0)
        l = 0; 
      l = 192 + (l >> 2);
      if (XURandom.getInstance().nextInt(3) != 0)
        l -= XURandom.getInstance().nextInt(4); 
      l = l * lum / 255;
      if (l > 255)
        l = 255; 
      if (l < 128)
        l = 128; 
      aint[j] = 0xFF000000 | l * 65793;
    } 
    image.setRGB(0, 0, w, h, aint, 0, w);
    return image;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\TextureUnstableLava.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */