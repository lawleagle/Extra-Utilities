package com.rwtema.extrautils.dynamicgui;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

public class WidgetSlotDisablable extends WidgetSlot {
  boolean enabled = true;
  
  String methodName;
  
  public WidgetSlotDisablable(IInventory inv, int slot, int x, int y, String methodName) {
    super(inv, slot, x, y);
    this.methodName = methodName;
  }
  
  public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
    return this.enabled;
  }
  
  public boolean isEnabled() {
    try {
      return ((Boolean)this.inventory.getClass().getMethod(this.methodName, new Class[0]).invoke(this.inventory, new Object[0])).booleanValue();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }
  
  public void renderForeground(TextureManager manager, DynamicGui gui, int guiLeft, int guiTop) {
    if (!this.enabled) {
      boolean blendLevel = GL11.glIsEnabled(3042);
      if (!blendLevel)
        GL11.glEnable(3042); 
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
      gui.drawTexturedModalRect(guiLeft + getX(), guiTop + getY(), 0, 0, 18, 18);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (!blendLevel)
        GL11.glDisable(3042); 
    } 
  }
  
  public void handleDescriptionPacket(NBTTagCompound packet) {
    this.enabled = packet.getBoolean("a");
  }
  
  public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
    boolean newEnabled = isEnabled();
    if (!changesOnly || newEnabled != this.enabled) {
      this.enabled = newEnabled;
      NBTTagCompound tags = new NBTTagCompound();
      tags.setBoolean("a", this.enabled);
      return tags;
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\dynamicgui\WidgetSlotDisablable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */