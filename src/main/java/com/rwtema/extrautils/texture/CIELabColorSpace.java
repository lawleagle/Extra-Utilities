package com.rwtema.extrautils.texture;

import java.awt.color.ColorSpace;

public class CIELabColorSpace extends ColorSpace {
  private final float INV255 = 0.003921569F;
  
  private static final long serialVersionUID = 5027741380892134289L;
  
  public static CIELabColorSpace getInstance() {
    return Holder.INSTANCE;
  }
  
  public float[] fromCIEXYZ(float[] colorvalue) {
    double l = f(colorvalue[1] * 1.0D);
    double L = 116.0D * l - 16.0D;
    double a = 500.0D * (f(colorvalue[0] * 1.0521110608435826D) - l);
    double b = 200.0D * (l - f(colorvalue[2] * 0.9184170164304805D));
    return new float[] { (float)L, (float)a, (float)b };
  }
  
  public float[] fromRGB(float[] rgbvalue) {
    float[] xyz = CIEXYZ.fromRGB(rgbvalue);
    return fromCIEXYZ(xyz);
  }
  
  public float[] fromRGB(int r, int g, int b) {
    return fromRGB(new float[] { r * 0.003921569F, g * 0.003921569F, b * 0.003921569F });
  }
  
  public float[] fromRGB(int col) {
    return fromRGB(new float[] { ((col & 0xFF0000) >> 16) * 0.003921569F, ((col & 0xFF00) >> 8) * 0.003921569F, (col & 0xFF) * 0.003921569F });
  }
  
  public float getMaxValue(int component) {
    return 128.0F;
  }
  
  public float getMinValue(int component) {
    return (component == 0) ? 0.0F : -128.0F;
  }
  
  public String getName(int idx) {
    return String.valueOf("Lab".charAt(idx));
  }
  
  public float[] toCIEXYZ(float[] colorvalue) {
    double i = (colorvalue[0] + 16.0D) * 0.008620689655172414D;
    double X = fInv(i + colorvalue[1] * 0.002D) * 0.95047D;
    double Y = fInv(i) * 1.0D;
    double Z = fInv(i - colorvalue[2] * 0.005D) * 1.08883D;
    return new float[] { (float)X, (float)Y, (float)Z };
  }
  
  public float[] toRGB(float[] colorvalue) {
    float[] xyz = toCIEXYZ(colorvalue);
    return CIEXYZ.toRGB(xyz);
  }
  
  CIELabColorSpace() {
    super(1, 3);
  }
  
  private static double f(double x) {
    if (x > 0.008856451679035631D)
      return Math.cbrt(x); 
    return 7.787037037037037D * x + 0.13793103448275862D;
  }
  
  private static double fInv(double x) {
    if (x > 0.20689655172413793D)
      return x * x * x; 
    return 0.12841854934601665D * (x - 0.13793103448275862D);
  }
  
  private Object readResolve() {
    return getInstance();
  }
  
  private static class Holder {
    static final CIELabColorSpace INSTANCE = new CIELabColorSpace();
  }
  
  private static final ColorSpace CIEXYZ = ColorSpace.getInstance(1001);
  
  private static final double N = 0.13793103448275862D;
  
  private static final double X0 = 0.95047D;
  
  private static final double XI = 1.0521110608435826D;
  
  private static final double Y0 = 1.0D;
  
  private static final double YI = 1.0D;
  
  private static final double Z0 = 1.08883D;
  
  private static final double ZI = 0.9184170164304805D;
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\texture\CIELabColorSpace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */