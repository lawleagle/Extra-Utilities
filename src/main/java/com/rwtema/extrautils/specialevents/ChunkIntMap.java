package com.rwtema.extrautils.specialevents;

import gnu.trove.map.hash.TLongIntHashMap;

public class ChunkIntMap extends TLongIntHashMap {
  public static long getKey(int a, int b) {
    return a | b << 32L;
  }
  
  public int put(int a, int b, int value) {
    return put(getKey(a, b), value);
  }
  
  public int get(int a, int b) {
    return get(getKey(a, b));
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\specialevents\ChunkIntMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */