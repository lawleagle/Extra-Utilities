package com.rwtema.extrautils.texture;

import com.rwtema.extrautils.modintegration.TConTextureResourcePackBedrockium;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.MathHelper;

public class TextureBedrockLava extends TextureDerived {
  public TextureBedrockLava(String p_i1282_1_, String baseIcon) {
    super(p_i1282_1_, baseIcon, TextureDerived.TextureMapType.BLOCK);
  }
  
  public BufferedImage processImage(BufferedImage image, AnimationMetadataSection animationmetadatasection) {
    int w = image.getWidth();
    int h = image.getHeight();
    int[] pixels = new int[h * w];
    image.getRGB(0, 0, w, h, pixels, 0, w);
    double mean = 0.0D;
    for (int i = 0; i < pixels.length; i++) {
      pixels[i] = getLuminosity(pixels[i]);
      mean += pixels[i];
    } 
    mean /= pixels.length;
    BufferedImage bedrockImage = TConTextureResourcePackBedrockium.getBedrockImage();
    for (int j = 0; j < pixels.length; j++) {
      int x = j % w;
      int y = (j - x) / w % w;
      int sn = (j - x) / w / w;
      int dx = x * bedrockImage.getWidth() / w;
      int dy = y * bedrockImage.getHeight() / w;
      int col = bedrockImage.getRGB(dx, dy);
      double f = pixels[j] / mean;
      int r = clamp(this.rgb.getRed(col) * f);
      int g = clamp(this.rgb.getGreen(col) * f);
      int b = clamp(this.rgb.getBlue(col) * f);
      pixels[j] = 0xFF000000 | r << 16 | g << 8 | b;
    } 
    image.setRGB(0, 0, w, h, pixels, 0, w);
    return image;
  }
  
  private int clamp(double v) {
    return MathHelper.clamp_int((int)v, 0, 255);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\TextureBedrockLava.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */