package com.rwtema.extrautils;

import cpw.mods.fml.client.CustomModLoadingErrorDisplayException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;

@SideOnly(Side.CLIENT)
public class CustomErrorWGui extends CustomModLoadingErrorDisplayException {
  String cause;
  
  String[] message;
  
  public CustomErrorWGui(String cause, String... message) {
    this.cause = cause;
    this.message = message;
  }
  
  public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {}
  
  public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
    errorScreen.drawDefaultBackground();
    List t = new ArrayList();
    for (String m : this.message) {
      if (m != null)
        t.addAll(fontRenderer.listFormattedStringToWidth(m, errorScreen.width)); 
    } 
    int offset = Math.max(85 - t.size() * 10, 10);
    errorScreen.drawCenteredString(fontRenderer, this.cause, errorScreen.width / 2, offset, 16777215);
    offset += 10;
    for (Object aT : t) {
      errorScreen.drawCenteredString(fontRenderer, (String)aT, errorScreen.width / 2, offset, 16777215);
      offset += 10;
    } 
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\CustomErrorWGui.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */