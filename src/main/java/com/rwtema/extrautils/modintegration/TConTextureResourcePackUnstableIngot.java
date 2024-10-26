package com.rwtema.extrautils.modintegration;

import java.awt.image.BufferedImage;

public class TConTextureResourcePackUnstableIngot extends TConTextureResourcePackBase {
  public TConTextureResourcePackUnstableIngot(String name) {
    super(name);
  }
  
  public BufferedImage modifyImage(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    boolean[][] trans = new boolean[width][height];
    boolean[][] edge = new boolean[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
          edge[x][y] = true; 
        int c = image.getRGB(x, y);
        if (c == 0 || rgb.getAlpha(c) < 64) {
          trans[x][y] = true;
          if (x > 0)
            edge[x - 1][y] = true; 
          if (y > 0)
            edge[x][y - 1] = true; 
          if (x < width - 1)
            edge[x + 1][y] = true; 
          if (y < height - 1)
            edge[x][y + 1] = true; 
        } 
      } 
    } 
    int white = -1;
    for (int i = 0; i < width; i++) {
      for (int y = 0; y < height; y++) {
        if (!trans[i][y]) {
          int c = image.getRGB(i, y);
          int lum = brightness(rgb.getRed(c), rgb.getGreen(c), rgb.getBlue(c));
          if (edge[i][y]) {
            int alpha = 255;
            lum = 256 + (i * 16 / width + y * 16 / height - 16) * 6;
            if (lum >= 256)
              lum = 255 - lum - 256; 
            int col = alpha << 24 | lum << 16 | lum << 8 | lum;
            image.setRGB(i, y, col);
          } else {
            image.setRGB(i, y, 0);
          } 
        } 
      } 
    } 
    return image;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TConTextureResourcePackUnstableIngot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */