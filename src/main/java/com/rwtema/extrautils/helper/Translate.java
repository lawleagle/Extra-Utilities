package com.rwtema.extrautils.helper;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

public class Translate {
  public static String get(String id, Object... objects) {
    String s = StatCollector.translateToLocal(id);
    for (int i = 0; i < objects.length; i++)
      s = s.replaceAll("%" + (i + 1), objects[i].toString()); 
    ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(id, objects);
    return chatComponentTranslation.getUnformattedTextForChat();
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\helper\Translate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */