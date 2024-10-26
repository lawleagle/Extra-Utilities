package com.rwtema.extrautils.dynamicgui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

public class WidgetText implements IWidget {
  public int x;
  
  public int y;
  
  public int w;
  
  public int h;
  
  public int align;
  
  public int color;
  
  public String msg;
  
  public WidgetText(int x, int y, String msg, int w) {
    this(x, y, w, 9, 1, 4210752, msg);
  }
  
  public WidgetText(int x, int y, int align, int color, String msg) {
    this(x, y, msg.length() * 12, 9, align, color, msg);
  }
  
  public WidgetText(int x, int y, int w, int h, int align, int color, String msg) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.align = align;
    this.color = color;
    this.msg = msg;
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
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {}
  
  public String getMsgClient() {
    return this.msg;
  }
  
  @SideOnly(Side.CLIENT)
  public void renderBackground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    int x = getX() + (1 - this.align) * (getW() - gui.getFontRenderer().getStringWidth(getMsgClient())) / 2;
    gui.getFontRenderer().drawString(getMsgClient(), guiLeft + x, guiTop + getY(), 4210752);
    manager.bindTexture(gui.getWidgets());
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public void addToContainer(DynamicContainer container) {}
  
  public List<String> getToolTip() {
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */