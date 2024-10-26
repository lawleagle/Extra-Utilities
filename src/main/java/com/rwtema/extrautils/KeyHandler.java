package com.rwtema.extrautils;

import com.google.common.base.Throwables;
import java.lang.reflect.Field;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IntHashMap;

public class KeyHandler {
  public static KeyHandler INSTANCE = new KeyHandler();
  
  public static KeyBinding CTRL;
  
  public static final String CTRL_DESCRIPTION = "key.xu.special";
  
  public static final int CTRL_CODE = 29;
  
  public static final String CTRL_CATEGORY = "key.categories.gameplay";
  
  private static IntHashMap hash;
  
  static {
    for (Field field : KeyBinding.class.getDeclaredFields()) {
      if (field.getType() == IntHashMap.class) {
        field.setAccessible(true);
        try {
          hash = (IntHashMap)field.get(null);
        } catch (IllegalAccessException e) {
          throw Throwables.propagate(e);
        } 
      } 
    } 
  }
  
  public static boolean getIsKeyPressed(KeyBinding key) {
    KeyBinding lookup = (KeyBinding)hash.lookup(key.getKeyCode());
    return (lookup != null && lookup.getIsKeyPressed());
  }
  
  public void register() {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\KeyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */