package com.rwtema.extrautils.dynamicgui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DynamicGui extends GuiContainer {
  private static final ResourceLocation texBackground = new ResourceLocation("extrautils", "textures/guiBase.png");
  
  private static final ResourceLocation texWidgets = new ResourceLocation("extrautils", "textures/guiWidget.png");
  
  public static int border = 5;
  
  private DynamicContainer container;
  
  public DynamicGui(DynamicContainer container) {
    super(container);
    this.xSize = container.width;
    this.ySize = container.height;
    this.guiLeft = (this.width - this.xSize) / 2;
    this.guiTop = (this.height - this.ySize) / 2;
    this.container = container;
  }
  
  public float getZLevel() {
    return this.zLevel;
  }
  
  public int getXSize() {
    return this.xSize;
  }
  
  public int getYSize() {
    return this.ySize;
  }
  
  public FontRenderer getFontRenderer() {
    return this.fontRendererObj;
  }
  
  public ResourceLocation getBackground() {
    return texBackground;
  }
  
  public ResourceLocation getWidgets() {
    return texWidgets;
  }
  
  protected void drawGuiContainerBackgroundLayer(float f, int a, int b) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(getBackground());
    this.xSize = this.container.width;
    this.ySize = this.container.height;
    drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize - 4, this.ySize - 4);
    drawTexturedModalRect(this.guiLeft + this.xSize - 4, this.guiTop, 252, 0, 4, 4);
    drawTexturedModalRect(this.guiLeft + this.xSize - 4, this.guiTop + 4, 252, 260 - this.ySize, 4, this.ySize - 4);
    drawTexturedModalRect(this.guiLeft, this.guiTop + this.ySize - 4, 0, 252, 4, 4);
    drawTexturedModalRect(this.guiLeft + 4, this.guiTop + this.ySize - 4, 260 - this.xSize, 252, this.xSize - 8, 4);
    this.mc.renderEngine.bindTexture(getWidgets());
    for (int i = 0; i < this.container.widgets.size(); i++)
      ((IWidget)this.container.widgets.get(i)).renderBackground(this.mc.renderEngine, this, this.guiLeft, this.guiTop); 
  }
  
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    List<String> tooltip = null;
    GL11.glDisable(32826);
    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(2896);
    GL11.glDisable(2929);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    for (int i = 0; i < this.container.widgets.size(); i++) {
      this.mc.renderEngine.bindTexture(getWidgets());
      ((IWidget)this.container.widgets.get(i)).renderForeground(this.mc.renderEngine, this, 0, 0);
      if (isInArea(par1 - this.guiLeft, par2 - this.guiTop, this.container.widgets.get(i))) {
        List<String> t = ((IWidget)this.container.widgets.get(i)).getToolTip();
        if (t != null)
          tooltip = t; 
      } 
    } 
    if (tooltip != null) {
      drawHoveringText(tooltip, par1 - this.guiLeft, par2 - this.guiTop, getFontRenderer());
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      RenderHelper.enableStandardItemLighting();
    } 
    RenderHelper.enableGUIStandardItemLighting();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glEnable(32826);
  }
  
  public boolean isInArea(int x, int y, IWidget w) {
    return (x >= w.getX() && x < w.getX() + w.getW() && y >= w.getY() && y < w.getY() + w.getH());
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\DynamicGui.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */