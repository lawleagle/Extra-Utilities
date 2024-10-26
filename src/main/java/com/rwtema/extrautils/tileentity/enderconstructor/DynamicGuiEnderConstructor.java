package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import com.rwtema.extrautils.dynamicgui.DynamicGui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class DynamicGuiEnderConstructor extends DynamicGui {
  private static final ResourceLocation texBackground = new ResourceLocation("extrautils", "textures/guiBase2.png");
  
  private static final ResourceLocation texWidgets = new ResourceLocation("extrautils", "textures/guiWidget2.png");
  
  public DynamicGuiEnderConstructor(DynamicContainer container) {
    super(container);
  }
  
  public ResourceLocation getBackground() {
    return texBackground;
  }
  
  public ResourceLocation getWidgets() {
    return texWidgets;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\tileentity\enderconstructor\DynamicGuiEnderConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */