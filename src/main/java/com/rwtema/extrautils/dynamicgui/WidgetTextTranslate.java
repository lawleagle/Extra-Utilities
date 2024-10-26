package com.rwtema.extrautils.dynamicgui;

import net.minecraft.util.StatCollector;

public class WidgetTextTranslate extends WidgetText {
  public WidgetTextTranslate(int x, int y, int w, int h, int align, int color, String msg) {
    super(x, y, w, h, align, color, msg);
  }
  
  public WidgetTextTranslate(int i, int j, String invName, int playerInvWidth) {
    super(i, j, invName, playerInvWidth);
  }
  
  public String getMsgClient() {
    return StatCollector.translateToLocal(this.msg);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetTextTranslate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */