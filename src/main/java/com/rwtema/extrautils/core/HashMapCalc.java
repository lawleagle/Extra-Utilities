package com.rwtema.extrautils.core;

import com.google.common.base.Function;
import java.util.HashMap;

public abstract class HashMapCalc<K, V> extends HashMap<K, V> {
  Function<K, V> function;
  
  protected HashMapCalc(Function<K, V> function) {
    this.function = function;
  }
  
  public V getCalc(K key) {
    if (!containsKey(key)) {
      V calcEntry = (V)this.function.apply(key);
      put(key, calcEntry);
    } 
    return get(key);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\HashMapCalc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */