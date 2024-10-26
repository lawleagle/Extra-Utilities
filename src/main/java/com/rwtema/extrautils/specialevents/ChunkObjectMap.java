package com.rwtema.extrautils.specialevents;

import com.google.common.base.Function;
import gnu.trove.map.hash.TLongObjectHashMap;

public class ChunkObjectMap<V> {
  TLongObjectHashMap<V> map = new TLongObjectHashMap();
  
  Function<Void, V> init = null;
  
  public static long getKey(int a, int b) {
    return a | b << 32L;
  }
  
  public V putChunk(int x, int z, V value) {
    return (V)this.map.put(getKey(x, z), value);
  }
  
  public V getChunk(int x, int z) {
    if (this.init == null)
      return (V)this.map.get(getKey(x, z)); 
    long key = getKey(x, z);
    V v = (V)this.map.get(key);
    if (v == null) {
      v = (V)this.init.apply(null);
      this.map.put(key, v);
    } 
    return v;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\specialevents\ChunkObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */