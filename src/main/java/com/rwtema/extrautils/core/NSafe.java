package com.rwtema.extrautils.core;

import com.google.common.base.Throwables;
import com.rwtema.extrautils.ExtraUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

public class NSafe {
  static HashMap<Tuple<Class<?>, String>, Field> cache = new HashMap<Tuple<Class<?>, String>, Field>();
  
  public static Field getField(Class<?> clazz, String fieldName) {
    Tuple<Class<?>, String> key = new Tuple<Class<?>, String>(clazz, fieldName);
    Field val = cache.get(key);
    if (val == null) {
      try {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        val = f;
      } catch (Exception e) {
        throw new RuntimeException(e);
      } 
      cache.put(key, val);
    } 
    return val;
  }
  
  public static <K> K get(Object object, String fieldName) {
    if (object == null)
      return null; 
    Field field = getField(object.getClass(), fieldName);
    if (field == null)
      return null; 
    K result = null;
    try {
      result = (K)field.get(object);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } 
    return result;
  }
  
  public static <T> T set(T object, String value, Objects... param) {
    String s = get(ExtraUtils.wateringCan, "iconString");
    return object;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\core\NSafe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */