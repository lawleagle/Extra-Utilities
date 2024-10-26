package com.rwtema.extrautils.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiFilterPipe extends GuiContainer {
  private static final ResourceLocation texture = new ResourceLocation("extrautils", "textures/guiSortingPipe.png");
  
  public GuiFilterPipe(IInventory player, IInventory pipe) {
    super(new ContainerFilterPipe(player, pipe));
    this.xSize = 175;
    this.ySize = 192;
  }
  
  protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(texture);
    int k = (this.width - this.xSize) / 2;
    int l = (this.height - this.ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\gui\GuiFilterPipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */