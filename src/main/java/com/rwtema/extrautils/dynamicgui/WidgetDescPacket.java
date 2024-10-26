package com.rwtema.extrautils.dynamicgui;

import java.util.List;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;

public abstract class WidgetDescPacket extends WidgetBase {
  public WidgetDescPacket() {
    super(0, 0, 0, 0);
  }
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {}
  
  public void renderBackground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {}
  
  public List<String> getToolTip() {
    return null;
  }
  
  public abstract NBTTagCompound getDescriptionPacket(boolean paramBoolean);
  
  public abstract void handleDescriptionPacket(NBTTagCompound paramNBTTagCompound);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetDescPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */