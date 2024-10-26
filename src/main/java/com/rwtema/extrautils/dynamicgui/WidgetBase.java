package com.rwtema.extrautils.dynamicgui;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WidgetBase implements IWidget {
  int x;
  
  int y;
  
  int w;
  
  int h;
  
  public WidgetBase(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public int getW() {
    return this.w;
  }
  
  public int getH() {
    return this.h;
  }
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    return null;
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {}
  
  public void addToContainer(DynamicContainer container) {}
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */