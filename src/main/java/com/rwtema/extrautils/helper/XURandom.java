package com.rwtema.extrautils.helper;

import java.util.Random;

public class XURandom extends Random {
  private static final XURandom INSTANCE = new XURandom();
  
  private long[] rng = new long[16];
  
  private int i;
  
  public static XURandom getInstance() {
    return INSTANCE;
  }
  
  private synchronized void fillRNG(long seed) {
    this.i = 0;
    seed = (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
    this.rng = new long[16];
    this.rng[0] = seed;
    for (int i = 1; i < this.rng.length; i++) {
      seed ^= seed >> 12L;
      seed ^= seed << 25L;
      seed ^= seed >> 27L;
      this.rng[i] = seed * 2685821657736338717L;
    } 
  }
  
  public XURandom() {
    if (this.rng[0] == 0L)
      setSeed((new Random()).nextLong()); 
  }
  
  public XURandom(long seed) {
    super(seed);
  }
  
  public synchronized void setSeed(long seed) {
    super.setSeed(seed);
    this.i = 0;
    fillRNG(seed);
  }
  
  public synchronized void setRNGArray(long... rngArray) {
    this.i = 0;
    System.arraycopy(rngArray, 0, this.rng, 0, this.rng.length);
  }
  
  public int next(int nbits) {
    long x = nextLong() & (1L << nbits) - 1L;
    return (int)x;
  }
  
  public synchronized long nextLong() {
    if (this.rng == null)
      return 0L; 
    long a = this.rng[this.i];
    this.i = this.i + 1 & 0xF;
    long b = this.rng[this.i];
    b ^= b << 31L;
    b ^= b >> 11L;
    a ^= a >> 30L;
    this.rng[this.i] = a ^ b;
    return this.rng[this.i] * 1181783497276652981L;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\XURandom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */