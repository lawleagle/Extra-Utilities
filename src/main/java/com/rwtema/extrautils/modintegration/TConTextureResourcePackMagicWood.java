package com.rwtema.extrautils.modintegration;

import java.awt.image.BufferedImage;
import net.minecraft.util.MathHelper;

public class TConTextureResourcePackMagicWood extends TConTextureResourcePackBase {
  public TConTextureResourcePackMagicWood(String name) {
    super(name);
  }
  
  public BufferedImage modifyImage(BufferedImage image) {
    int n, w = image.getWidth();
    int h = image.getHeight();
    image.getType();
    int[][] pixels = new int[w][h];
    boolean[][] base = new boolean[w][h];
    int mean = 0;
    int div = 0;
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        int c = image.getRGB(x, y);
        pixels[x][y] = brightness(c);
        boolean nottrans = (c != 0 && rgb.getAlpha(c) > 64);
        if (nottrans) {
          base[x][y] = true;
          mean += pixels[x][y];
          div++;
        } 
      } 
    } 
    mean = mean / div * 2 / 4;
    if (w >= 256) {
      n = 5;
    } else if (w >= 128) {
      n = 4;
    } else if (w >= 64) {
      n = 3;
    } else if (w >= 32) {
      n = 2;
    } else {
      n = 1;
    } 
    boolean[][] baseSilhouette = contract(base, n);
    boolean[][] interior1 = contract(baseSilhouette, n);
    boolean[][] baseCorners = multI(mult(expand(getCorners(baseSilhouette), n), baseSilhouette), interior1);
    boolean[][] baseCornersShift = orwise(orwise(shift(baseCorners, 0, -1), shift(baseCorners, -1, 0)), shift(baseCorners, -1, -1));
    boolean[][] interior2 = contract(interior1, 2 * n);
    boolean[][] interior3 = contract(interior2, n);
    boolean[][] interior4 = contract(interior3, n);
    boolean[][] interiorCorners = multI(mult(expand(getCorners(interior2), n), interior2), interior3);
    boolean[][] interiorCornersShift = orwise(orwise(shift(interiorCorners, -1, 0), shift(interiorCorners, 0, -1)), shift(interiorCorners, -1, -1));
    int trans = 0;
    int gold = -398001;
    int gold_highlight = -117;
    int wood = -6455217;
    int darkwood = -10071758;
    int[][] outpixels = new int[w][w];
    for (int i = 0; i < w; i++) {
      for (int y = 0; y < h; y++) {
        if (!baseSilhouette[i][y]) {
          if (base[i][y]) {
            outpixels[i][y] = multPixel(darkwood, pixels[i][y] / 2);
          } else {
            outpixels[i][y] = trans;
          } 
        } else if (!interior1[i][y]) {
          if (baseCorners[i][y]) {
            if (baseCornersShift[i][y]) {
              outpixels[i][y] = multPixel(gold, Math.max(pixels[i][y], mean));
            } else {
              outpixels[i][y] = multPixel(gold_highlight, Math.max(pixels[i][y], mean) + 5);
            } 
          } else {
            outpixels[i][y] = multPixel(darkwood, pixels[i][y]);
          } 
        } else if (!interior2[i][y] || interior3[i][y]) {
          if (interior3[i][y] && !interior4[i][y]) {
            outpixels[i][y] = multPixel(wood, pixels[i][y] * 3 / 4);
          } else {
            outpixels[i][y] = multPixel(wood, pixels[i][y]);
          } 
        } else if (interiorCorners[i][y]) {
          if (interiorCornersShift[i][y]) {
            outpixels[i][y] = multPixel(gold, Math.max(pixels[i][y], mean));
          } else {
            outpixels[i][y] = multPixel(gold_highlight, Math.max(pixels[i][y], mean) + 5);
          } 
        } else {
          outpixels[i][y] = multPixel(darkwood, pixels[i][y]);
        } 
        image.setRGB(i, y, outpixels[i][y]);
      } 
    } 
    return image;
  }
  
  private boolean[][] orwise(boolean[][] a, boolean[][] b) {
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < (a[i]).length; j++)
        a[i][j] = a[i][j] | b[i][j]; 
    } 
    return a;
  }
  
  private boolean[][] mult(boolean[][] a, boolean[][] b) {
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < (a[i]).length; j++)
        a[i][j] = a[i][j] & b[i][j]; 
    } 
    return a;
  }
  
  private boolean[][] multI(boolean[][] a, boolean[][] b) {
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < (a[i]).length; j++)
        a[i][j] = a[i][j] & (!b[i][j]); 
    } 
    return a;
  }
  
  private boolean[][] expand(boolean[][] base, int n) {
    boolean[][] output = expand(base);
    for (int i = 0; i < n - 1; i++)
      output = expand(output); 
    return output;
  }
  
  private boolean[][] contract(boolean[][] base, int n) {
    boolean[][] output = contract(base);
    for (int i = 0; i < n - 1; i++)
      output = contract(output); 
    return output;
  }
  
  public int multPixel(int col, int b) {
    return 0xFF000000 | clamp(rgb.getRed(col) * b / 255) << 16 | clamp(rgb.getGreen(col) * b / 255) << 8 | clamp(rgb.getBlue(col) * b / 255);
  }
  
  private int clamp(int i) {
    return MathHelper.clamp_int(i, 0, 255);
  }
  
  public boolean get(boolean[][] img, int x, int y) {
    return (x >= 0 && y >= 0 && x < img.length && y < (img[x]).length && img[x][y]);
  }
  
  static int[][] offsets = new int[][] { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };
  
  public boolean[][] shift(boolean[][] img, int dx, int dy) {
    int w = img.length;
    boolean[][] img2 = new boolean[w][w];
    for (int x = Math.max(-dx, 0); x < Math.min(w, w + dx); x++)
      System.arraycopy(img[x + dx], Math.max(-dy, 0) + dy, img2[x], Math.max(-dy, 0), Math.min(w, w + dy) - Math.max(-dy, 0)); 
    return img2;
  }
  
  public boolean[][] getCorners(boolean[][] img) {
    int w = img.length;
    boolean[][] img2 = new boolean[w][w];
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < w; y++) {
        if (img[x][y]) {
          int an = -1;
          int n = 0;
          for (int[] offset : offsets) {
            if (get(img, x + offset[0], y + offset[1])) {
              if (an == -1)
                an = n; 
              n = 0;
            } else {
              n++;
              if (n == 5)
                break; 
            } 
          } 
          if (an != -1)
            n += an; 
          if (n >= 5)
            img2[x][y] = true; 
        } 
      } 
    } 
    return img2;
  }
  
  public boolean[][] contract(boolean[][] img) {
    int w = img.length;
    boolean[][] img2 = new boolean[w][w];
    int x;
    for (x = 0; x < w; x++)
      System.arraycopy(img[x], 0, img2[x], 0, w); 
    for (x = 0; x < w; x++) {
      for (int y = 0; y < w; y++) {
        if (img[x][y]) {
          if (x == 0 || y == 0 || x == w - 1 || y == w - 1)
            img2[x][y] = false; 
        } else {
          if (x > 0)
            img2[x - 1][y] = false; 
          if (y > 0)
            img2[x][y - 1] = false; 
          if (x < w - 1)
            img2[x + 1][y] = false; 
          if (y < w - 1)
            img2[x][y + 1] = false; 
        } 
      } 
    } 
    return img2;
  }
  
  public boolean[][] expand(boolean[][] img) {
    int w = img.length;
    boolean[][] img2 = new boolean[w][w];
    int x;
    for (x = 0; x < w; x++)
      System.arraycopy(img[x], 0, img2[x], 0, w); 
    for (x = 0; x < w; x++) {
      for (int y = 0; y < w; y++) {
        if (img[x][y])
          for (int[] offset : offsets) {
            int dx = x + offset[0];
            int dy = y + offset[1];
            if (dx >= 0 && dy >= 0 && dx < w && dy < w)
              img2[dx][dy] = true; 
          }  
      } 
    } 
    return img2;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\modintegration\TConTextureResourcePackMagicWood.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */