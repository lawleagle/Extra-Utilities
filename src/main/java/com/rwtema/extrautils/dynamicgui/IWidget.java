package com.rwtema.extrautils.dynamicgui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;

public interface IWidget {
  int getX();
  
  int getY();
  
  int getW();
  
  int getH();
  
  NBTTagCompound getDescriptionPacket(boolean paramBoolean);
  
  void handleDescriptionPacket(NBTTagCompound paramNBTTagCompound);
  
  @SideOnly(Side.CLIENT)
  void renderForeground(TextureManager paramTextureManager, DynamicGui paramDynamicGui, int paramInt1, int paramInt2);
  
  @SideOnly(Side.CLIENT)
  void renderBackground(TextureManager paramTextureManager, DynamicGui paramDynamicGui, int paramInt1, int paramInt2);
  
  void addToContainer(DynamicContainer paramDynamicContainer);
  
  List<String> getToolTip();
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\IWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */