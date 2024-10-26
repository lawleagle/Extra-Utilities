package com.rwtema.extrautils.helper;

public class SplineHelper {
  public static double[] splineParams(double p0, double p1, double d0, double d1) {
    return new double[] { 2.0D * p0 - 2.0D * p1 + d0 + d1, -3.0D * p0 + 3.0D * p1 - 2.0D * d0 - d1, d0, p0 };
  }
  
  public static double evalSpline(double t, double[] p) {
    return ((p[0] * t + p[1]) * t + p[2]) * t + p[3];
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\SplineHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */