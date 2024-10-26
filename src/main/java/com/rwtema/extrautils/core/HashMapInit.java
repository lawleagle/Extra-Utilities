package com.rwtema.extrautils.core;

import java.util.HashMap;

public class HashMapInit<K, V> extends HashMap<K, V> {
  Class<? extends V> clazz;
  
  public HashMapInit(Class<? extends V> clazz) {
    this.clazz = clazz;
  }
  
  public V getOrInit(K key) {
    V v = get(key);
    if (v == null)
      try {
        v = this.clazz.newInstance();
      } catch (InstantiationException ignore) {
      
      } catch (IllegalAccessException ignore) {} 
    return v;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\HashMapInit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */