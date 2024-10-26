package com.rwtema.extrautils.dynamicgui;

import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.helper.XUHelperClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

public class WidgetTank implements IWidget {
  public static final int[] ux2 = new int[] { 18, 18, 18 };
  
  public static final int[] uy2 = new int[] { 0, 0, 0 };
  
  public static final int[] uw2 = new int[] { 7, 7, 7 };
  
  public static final int[] uh2 = new int[] { 64, 64, 64 };
  
  public static final int[] ux = new int[] { 32, 0, 50 };
  
  public static final int[] uy = new int[] { 0, 0, 0 };
  
  public static final int[] uw = new int[] { 18, 18, 18 };
  
  public static final int[] uh = new int[] { 33, 18, 65 };
  
  FluidStack curFluid;
  
  int curCapacity;
  
  FluidTankInfo tankInfo;
  
  int shape;
  
  int x;
  
  int y;
  
  public WidgetTank(FluidTankInfo tankInfo, int x, int y) {
    this(tankInfo, x, y, 0);
  }
  
  public WidgetTank(FluidTankInfo tankInfo, int x, int y, int shape) {
    this.curFluid = null;
    this.curCapacity = 0;
    this.tankInfo = tankInfo;
    this.shape = shape;
    this.x = x;
    this.y = y;
  }
  
  @SideOnly(Side.CLIENT)
  public static void renderLiquid(FluidStack fluid, TextureManager manager, DynamicGui gui, int x, int y, int w, int h) {
    if (fluid == null)
      return; 
    if (w == 0 || h == 0)
      return; 
    manager.bindTexture((fluid.getFluid().getSpriteNumber() == 0) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
    int color = fluid.getFluid().getColor(fluid);
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    GL11.glColor4f(red, green, blue, 1.0F);
    IIcon icon = fluid.getFluid().getIcon(fluid);
    Tessellator tessellator = Tessellator.instance;
    for (int dx1 = x; dx1 < x + w; dx1 += 16) {
      for (int dy1 = y; dy1 < y + h; dy1 += 16) {
        int dx2 = Math.min(dx1 + 16, x + w);
        int dy2 = Math.min(dy1 + 16, y + h);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(dx1, dy2, gui.getZLevel(), icon.getMinU(), (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * (dy2 - dy1) / 16.0F));
        tessellator.addVertexWithUV(dx2, dy2, gui.getZLevel(), (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * (dx2 - dx1) / 16.0F), (icon.getMinV() + (icon.getMaxV() - icon.getMinV()) * (dy2 - dy1) / 16.0F));
        tessellator.addVertexWithUV(dx2, dy1, gui.getZLevel(), (icon.getMinU() + (icon.getMaxU() - icon.getMinU()) * (dx2 - dx1) / 16.0F), icon.getMinV());
        tessellator.addVertexWithUV(dx1, dy1, gui.getZLevel(), icon.getMinU(), icon.getMinV());
        tessellator.draw();
      } 
    } 
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public int getW() {
    return uw[this.shape];
  }
  
  public int getH() {
    return uh[this.shape];
  }
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    FluidStack newFluid = this.tankInfo.fluid;
    if (changesOnly && 
      this.curCapacity == this.tankInfo.capacity)
      if (this.curFluid == null) {
        if (newFluid == null)
          return null; 
      } else if (this.curFluid.isFluidEqual(newFluid) && 
        newFluid.amount == this.curFluid.amount) {
        return null;
      }  
    this.curFluid = (newFluid != null) ? newFluid.copy() : null;
    this.curCapacity = this.tankInfo.capacity;
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("capacity", this.curCapacity);
    if (this.curFluid != null)
      this.curFluid.writeToNBT(tag); 
    return tag;
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {
    this.curCapacity = packet.getInteger("capacity");
    this.curFluid = FluidStack.loadFluidStackFromNBT(packet);
  }
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    if (this.curFluid != null && this.curFluid.getFluid() != null && this.curCapacity > 0) {
      int a = this.curFluid.amount * (getH() - 2) / this.curCapacity;
      renderLiquid(this.curFluid, manager, gui, guiLeft + getX() + 1, guiTop + getY() - 1 + getH() - a, getW() - 2, a);
    } 
    manager.bindTexture(gui.getWidgets());
    gui.drawTexturedModalRect(guiLeft + getX() + getW() - uw2[this.shape] - 1, guiTop + getY() + 1, ux2[this.shape] + uw2[this.shape], uy2[this.shape], uw2[this.shape], Math.min(getH() - 2, uh2[this.shape]));
    gui.drawTexturedModalRect(guiLeft + getX() + 1, guiTop + getY() + 1, ux2[this.shape], uy2[this.shape], uw2[this.shape], Math.min(getH() - 2, uh2[this.shape]));
  }
  
  public void renderBackground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    manager.bindTexture(gui.getWidgets());
    gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY(), ux[this.shape], uy[this.shape], uw[this.shape], uh[this.shape]);
  }
  
  public void addToContainer(DynamicContainer container) {}
  
  public List<String> getToolTip() {
    if (this.curCapacity > 0) {
      List<String> l = new ArrayList();
      if (this.curFluid == null) {
        l.add("0 / " + XUHelperClient.commaDelimited(this.curCapacity));
      } else {
        l.add(XUHelper.getFluidName(this.curFluid) + ": " + XUHelperClient.commaDelimited(this.curFluid.amount) + " / " + XUHelperClient.commaDelimited(this.curCapacity));
      } 
      return l;
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetTank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */