package com.rwtema.extrautils.dynamicgui;

import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.helper.XUHelperClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class WidgetEnergy implements IWidget {
  int curEnergy;
  
  int curMax;
  
  IEnergyHandler tile;
  
  ForgeDirection dir;
  
  int x;
  
  int y;
  
  public WidgetEnergy(IEnergyHandler tile, ForgeDirection dir, int x, int y) {
    this.dir = dir;
    this.tile = tile;
    this.x = x;
    this.y = y;
    this.curEnergy = 0;
    this.curMax = 0;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public int getW() {
    return 18;
  }
  
  public int getH() {
    return 53;
  }
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    NBTTagCompound tag = null;
    if (!changesOnly || this.curEnergy != this.tile.getEnergyStored(this.dir)) {
      tag = new NBTTagCompound();
      tag.setInteger("cur", this.tile.getEnergyStored(this.dir));
    } 
    if (!changesOnly || this.curMax != this.tile.getMaxEnergyStored(this.dir)) {
      if (tag == null)
        tag = new NBTTagCompound(); 
      tag.setInteger("max", this.tile.getMaxEnergyStored(this.dir));
    } 
    this.curEnergy = this.tile.getEnergyStored(this.dir);
    this.curMax = this.tile.getMaxEnergyStored(this.dir);
    return tag;
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {
    if (packet.hasKey("cur"))
      this.curEnergy = packet.getInteger("cur"); 
    if (packet.hasKey("max"))
      this.curMax = packet.getInteger("max"); 
  }
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {}
  
  @SideOnly(Side.CLIENT)
  public void renderBackground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    manager.bindTexture(gui.getWidgets());
    int y = 0;
    if (this.curMax > 0 && this.curEnergy > 0) {
      y = 54 * this.curEnergy / this.curMax;
      if (y < 0)
        y = 0; 
    } 
    gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY(), 160, 0, 18, 54 - y);
    gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY() + 54 - y, 178, 54 - y, 18, y);
  }
  
  public void addToContainer(DynamicContainer container) {}
  
  @SideOnly(Side.CLIENT)
  public List<String> getToolTip() {
    if (this.curMax > 0) {
      List<String> l = new ArrayList();
      l.add(XUHelperClient.commaDelimited(this.curEnergy) + " / " + XUHelperClient.commaDelimited(this.curMax) + " RF");
      return l;
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetEnergy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */