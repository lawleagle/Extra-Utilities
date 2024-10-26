package com.rwtema.extrautils.dynamicgui;

import java.util.List;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;

public abstract class WidgetProgressArrow extends WidgetBase implements IWidget {
  byte curWidth = -1;
  
  public WidgetProgressArrow(int x, int y) {
    super(x, y, 22, 17);
  }
  
  public abstract int getWidth();
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    NBTTagCompound tag = null;
    byte newWidth = getAdjustedWidth(getWidth());
    if (!changesOnly || this.curWidth != newWidth) {
      tag = new NBTTagCompound();
      tag.setByte("a", newWidth);
    } 
    this.curWidth = newWidth;
    return tag;
  }
  
  private byte getAdjustedWidth(int a) {
    if (a < 0) {
      a = 0;
    } else if (a > 22) {
      a = 22;
    } 
    return (byte)a;
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {
    if (packet.hasKey("a"))
      this.curWidth = packet.getByte("a"); 
  }
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    manager.bindTexture(gui.getWidgets());
    gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY(), 98, 16, this.curWidth, 16);
  }
  
  public void renderBackground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    manager.bindTexture(gui.getWidgets());
    gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY(), 98, 0, 22, 16);
  }
  
  public List<String> getToolTip() {
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetProgressArrow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */